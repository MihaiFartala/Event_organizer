import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.awt.event.KeyListener;
import java.util.Random;

public class NewEventPopup extends JDialog {
    private JTextField dateField;
    private JTextField creatorField;
    private JTextField eventNameField;
    private JPanel popupPanel;
    private JSpinner timeSpinner;
    private JButton cancelButton;
    private JButton createButton;

    public NewEventPopup(Frame owner, int day, int month, int year, loggedUser creator) {
        super(owner, "Create Event", true);

        setContentPane(popupPanel);
        pack();
        setSize(250, 300);
        setLocationRelativeTo(owner);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        eventNameField.requestFocusInWindow();

        FieldsStyle.applyStyle(creatorField);
        FieldsStyle.applyStyle(eventNameField);
        FieldsStyle.applyStyle(dateField);
        FieldsStyle.applyStyle(timeSpinner);

        ButtonsStyle.applyButtonStyles(cancelButton);
        ButtonsStyle.applyButtonStyles(createButton);

        // Convert the selected date to a formatted string
        String date = String.format("%02d/%02d/%04d", day, month, year);

        dateField.setText(date);
        dateField.setEditable(false);

        creatorField.setText(creator.getUsername());
        creatorField.setEditable(false);

        // Crearea modelului pentru selectorul de ore și minute
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeSpinner.setModel(timeModel);

        // Setarea formatului afișat în selector
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        KeyListener enterKeyListenerCreate = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    createButton.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };

        eventNameField.addKeyListener(enterKeyListenerCreate);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Get the event name and organizer
                String eventName = eventNameField.getText();
                String organizer = creatorField.getText();

                // Check if the same user already has an event with the same name
                if (isEventNameTaken(eventName, organizer)) {
                    JOptionPane.showMessageDialog(NewEventPopup.this, "You already have an event with the same name. You can delete that event if you want to create a new one.", "Duplicate Event Name", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (eventName.isEmpty()) {
                    JOptionPane.showMessageDialog(NewEventPopup.this, "Please introduce a name for the event", "Invalid Event Name", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(NewEventPopup.this, "Are you sure you want to create this event?", "Confirm Event Creation", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        // Get the current date and time
                        Calendar currentDateTime = Calendar.getInstance();


                        // Get the selected date from the dateField
                        String dateString = dateField.getText();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        java.util.Date selectedDate = dateFormat.parse(dateString);

                        // Get the selected time from the spinner
                        java.util.Date selectedTime = (java.util.Date) timeSpinner.getValue();

                        // Combine the date and time
                        Calendar selectedDateTime = Calendar.getInstance();
                        selectedDateTime.setTime(selectedDate);
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, selectedTime.getHours());
                        selectedDateTime.set(Calendar.MINUTE, selectedTime.getMinutes());


                        // Check if the selected date and time is in the future
                        if (selectedDateTime.before(currentDateTime)) {
                            JOptionPane.showMessageDialog(NewEventPopup.this, "Cannot add event to a past date or time.", "Invalid Date/Time", JOptionPane.ERROR_MESSAGE);
                            return;
                        }


                        // Prepare the SQL statement for inserting into the events table
                        String sql = "INSERT INTO events (name, organizer, date, organizer_id, organizer_number, join_code) VALUES (?, ?, ?, ?, ?, ?)";
                        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");
                             PreparedStatement insertEventStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                            // Set the values in the prepared statement
                            insertEventStatement.setString(1, eventName);
                            insertEventStatement.setString(2, creator.getUsername());
                            insertEventStatement.setTimestamp(3, new java.sql.Timestamp(selectedDateTime.getTimeInMillis()));
                            insertEventStatement.setInt(4, creator.getId());
                            insertEventStatement.setString(5,creator.getPhoneNumber());
                            insertEventStatement.setString(6,generateRandomCode());

                            // Execute the statement
                            insertEventStatement.executeUpdate();

                            // Get the auto-generated event ID
                            ResultSet generatedKeys = insertEventStatement.getGeneratedKeys();
                            int eventID = -1;
                            if (generatedKeys.next()) {
                                eventID = generatedKeys.getInt(1);
                            }

                            String insertMemberSQL = "INSERT INTO event_members (event_id, user_id, relation) VALUES (?, ?, 'creator')";
                            try (PreparedStatement insertMemberStatement = conn.prepareStatement(insertMemberSQL)) {
                                insertMemberStatement.setInt(1, eventID);
                                insertMemberStatement.setInt(2, creator.getId());
                                insertMemberStatement.executeUpdate();
                            }

                            // Close the result set and prepared statements
                            generatedKeys.close();
                        }

                        JOptionPane.showMessageDialog(NewEventPopup.this, "Event created successfully!", "", JOptionPane.INFORMATION_MESSAGE);

                        dispose();

                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }
            }
        });
    }

    private String generateRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomCode = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(characters.length());
            randomCode.append(characters.charAt(index));
        }

        if(isCodeAlreadyUsed(randomCode.toString())){
            return generateRandomCode();
        }
        else {
            return randomCode.toString();
        }
    }

    private boolean isCodeAlreadyUsed(String code) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "")) {
            String sql = "SELECT COUNT(*) FROM events WHERE join_code = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, code);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    private boolean isEventNameTaken(String eventName, String organizer) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "")) {
            String sql = "SELECT COUNT(*) FROM events WHERE name = ? AND organizer = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, eventName);
                statement.setString(2, organizer);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
