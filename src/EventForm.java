import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.time.LocalTime;


import static javax.swing.SwingConstants.BOTTOM;

public class EventForm extends JFrame {
    private JPanel buttonsPanel;
    private JPanel spMainPanel;
    private JPanel invitePanel;
    private JPanel memberListPanel;
    private JPanel adminEditPanel;
    private JPanel chatPanel;
    private JPanel eventDetailsPanel;
    private JButton adminEditButton;
    private JButton closeEventButton;
    private JButton memberListButton;
    private JButton chatButton;
    private JButton invitePageButton;
    private JLabel eventNameLabel;
    private JLabel eventOrganiserLabel;
    private JLabel eventDateLabel;
    private JLabel phoneLabel;
    private JList<EventMember> memberList;
    private JLabel codeLabel;
    private JScrollPane memberListPane;
    private JPanel inviteUsersPanel;
    private JTextField userToInviteField;
    private JButton inviteMemberButton;
    private JPanel changeEventPanel;
    private JPanel changeDatePanel;
    private JPanel changeOtherPanel;
    private JTextField eventDateField;
    private JButton updateEventDateButton;
    private JTextField eventNameField;
    private JButton updateEventNameButton;
    private JSpinner eventTimeSpinner;
    private static EventForm lastOpenedForm;

    private final Event selectedEvent;

    private static loggedUser loggedUser;

    public EventForm(Event currentEvent, loggedUser loggedUser) {
        JFrame frame = new JFrame(currentEvent.getName());
        frame.setContentPane(spMainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize(750, 560);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        showEventDetails(currentEvent);
        adminEditButton.setVisible(false);
        frame.setVisible(true);

        ButtonsStyle.applyButtonStyles(adminEditButton);
        ButtonsStyle.applyButtonStyles(chatButton);
        ButtonsStyle.applyButtonStyles(invitePageButton);
        ButtonsStyle.applyButtonStyles(memberListButton);
        ButtonsStyle.applyButtonStyles(closeEventButton);
        ButtonsStyle.applyButtonStyles(inviteMemberButton);
        ButtonsStyle.applyButtonStyles(updateEventDateButton);
        ButtonsStyle.applyButtonStyles(updateEventNameButton);

        FieldsStyle.applyStyle(userToInviteField);
        FieldsStyle.applyStyle(eventDateField);
        FieldsStyle.applyStyle(eventTimeSpinner);
        FieldsStyle.applyStyle(eventNameField);

        memberListPane.getVerticalScrollBar().setUI(new ScrollBarStyle());

        selectedEvent = currentEvent;
        adminEditButton.setVisible(loggedUser.getId() == currentEvent.getOrganizer_id());

        SpinnerDateModel timeModel = new SpinnerDateModel();
        eventTimeSpinner.setModel(timeModel);

        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(eventTimeSpinner, "HH:mm");
        eventTimeSpinner.setEditor(timeEditor);

        EventForm.loggedUser = loggedUser;
        codeLabel.setText("Invite code: " + selectedEvent.getJoin_code());


        memberListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                memberListPanel.setVisible(true);
                chatPanel.setVisible(false);
                invitePanel.setVisible(false);
                adminEditPanel.setVisible(false);

                populateMembers(currentEvent);
            }
        });

        memberListButton.doClick();

        adminEditButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                // Change the background color when the mouse enters the butto
                adminEditButton.setBackground(adminEditButton.getBackground().darker());
            }

            public void mouseExited(MouseEvent e) {
                // No need to change the background color when the mouse exits the button
                adminEditButton.setBackground(new Color(220, 180, 20));
            }

            public void mouseClicked(MouseEvent e) {
                // Perform the desired action when the button is clicked
                adminEditButton.setBackground(new Color(220, 180, 20));
            }
        });

// Set the desired background color outside the MouseListener events
        adminEditButton.setBackground(new Color(220, 180, 20));

        adminEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                memberListPanel.setVisible(false);
                chatPanel.setVisible(false);
                invitePanel.setVisible(false);
                adminEditPanel.setVisible(true);

                LocalDateTime currentDate = currentEvent.getDate();

                // Format the date as "yyyy/MM/dd" and set it to the eventDateField
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String formattedDate = dateFormatter.format(currentDate.toLocalDate());
                eventDateField.setText(formattedDate);

                // Extract the time part from the currentDate
                LocalTime time = currentDate.toLocalTime();
                eventTimeSpinner.setValue(Date.from(time.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()));
            }
        });

        updateEventDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the new date and time values from the fields
                String newDate = eventDateField.getText();
                java.util.Date newTime = (java.util.Date) eventTimeSpinner.getValue();

                // Combine the date and time values
                Calendar selectedDateTime = Calendar.getInstance();
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date selectedDate = dateFormat.parse(newDate);

                    selectedDateTime.setTime(selectedDate);
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, newTime.getHours());
                    selectedDateTime.set(Calendar.MINUTE, newTime.getMinutes());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    return;
                }

                // Check if the new date is different from the previous date
                LocalDateTime currentDateTime = currentEvent.getDate();
                LocalDateTime newDateTime = selectedDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                if (newDateTime.equals(currentDateTime)) {
                    JOptionPane.showMessageDialog(adminEditPanel, "The new date is the same as the current date.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if the new date is in the future
                LocalDateTime now = LocalDateTime.now();
                if (newDateTime.isBefore(now)) {
                    JOptionPane.showMessageDialog(adminEditPanel, "The new date has already passed.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update the event date in the database
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

                    // Update the event date
                    String updateSql = "UPDATE events SET date = ? WHERE id = ?";
                    PreparedStatement updateStatement = conn.prepareStatement(updateSql);
                    updateStatement.setTimestamp(1, Timestamp.valueOf(newDateTime));
                    updateStatement.setInt(2, currentEvent.getId());
                    updateStatement.executeUpdate();
                    updateStatement.close();
                    conn.close();

                    JOptionPane.showMessageDialog(adminEditPanel, "Event date updated successfully!", "Date Updated", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });



        chatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                memberListPanel.setVisible(false);
                chatPanel.setVisible(true);
                invitePanel.setVisible(false);
                adminEditPanel.setVisible(false);
            }
        });

        invitePageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                memberListPanel.setVisible(false);
                chatPanel.setVisible(false);
                invitePanel.setVisible(true);
                adminEditPanel.setVisible(false);

                userToInviteField.setText("");
            }
        });

        class EnterKeyListener implements KeyListener {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Check if the Enter key was pressed
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    inviteMemberButton.doClick();
                }
            }
        }

        userToInviteField.addKeyListener(new EnterKeyListener());

        closeEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeEventForm();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                lastOpenedForm = null;
            }
        });

        inviteMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberToInvite = userToInviteField.getText();
                int eventId = currentEvent.getId();

                int userId = retrieveUserIdByUsername(memberToInvite);
                if (userId != -1) {
                    inviteMember(userId, eventId);
                } else {
                    JOptionPane.showMessageDialog(invitePanel, "Member not found!", "User Not Found", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



    }


    private int retrieveUserIdByUsername(String username) {
        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Prepare the SQL statement to retrieve the user ID
            String sql = "SELECT id FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);

            // Execute the query and obtain the result set
            ResultSet resultSet = statement.executeQuery();

            // Check if a row is returned and retrieve the user ID
            if (resultSet.next()) {
                int userId = resultSet.getInt("id");

                // Close the result set, statement, and connection
                resultSet.close();
                statement.close();
                conn.close();

                return userId;
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Return -1 if the user ID is not found
    }


    private void inviteMember(int userId, int eventId) {
        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Check if the user has a "request" relation in the event
            String checkSql = "SELECT relation FROM event_members WHERE user_id = ? AND event_id = ?";
            PreparedStatement checkStatement = conn.prepareStatement(checkSql);
            checkStatement.setInt(1, userId);
            checkStatement.setInt(2, eventId);
            ResultSet checkResult = checkStatement.executeQuery();

            // If a row is returned, check the relation column
            if (checkResult.next()) {
                String relation = checkResult.getString("relation");
                switch (relation) {
                    case "participant", "creator" -> {
                        JOptionPane.showMessageDialog(invitePanel, "The user is already a participant or creator in this event.", "Already a Participant/Creator", JOptionPane.INFORMATION_MESSAGE);
                        invitePanel.requestFocusInWindow();
                        return;
                    }
                    case "invited" -> {
                        JOptionPane.showMessageDialog(invitePanel, "The user has already been invited to this event.", "Already Invited", JOptionPane.INFORMATION_MESSAGE);
                        invitePanel.requestFocusInWindow();
                        return;
                    }
                    case "request" -> {
                        // Delete the existing request row
                        String deleteSql = "DELETE FROM event_members WHERE user_id = ? AND event_id = ? AND relation = 'request'";
                        PreparedStatement deleteStatement = conn.prepareStatement(deleteSql);
                        deleteStatement.setInt(1, userId);
                        deleteStatement.setInt(2, eventId);
                        deleteStatement.executeUpdate();
                        deleteStatement.close();
                    }
                }
            }

            // Prepare the SQL statement to insert the new row
            String insertSql = "INSERT INTO event_members (event_id, user_id, relation) VALUES (?, ?, 'invited')";
            PreparedStatement insertStatement = conn.prepareStatement(insertSql);
            insertStatement.setInt(1, eventId);
            insertStatement.setInt(2, userId);

            // Execute the insert statement
            insertStatement.executeUpdate();

            // Close the result set, statements, and connection
            checkResult.close();
            checkStatement.close();
            insertStatement.close();
            conn.close();

            JOptionPane.showMessageDialog(invitePanel, "Member invited successfully!", "Invitation Sent", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }




    public static void openEventForm(Event event) {
        if (lastOpenedForm != null) {
            lastOpenedForm.dispose();
        }

        lastOpenedForm = new EventForm(event, loggedUser);
        lastOpenedForm.setVisible(true);
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    private void showEventDetails(Event event) {
        eventNameLabel.setText(event.getName());
        eventOrganiserLabel.setText("Organizer: " + event.getOrganizer());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        eventDateLabel.setText("Date: " + event.getDate().format(formatter));

        if(event.getPhone_number() == null){
            phoneLabel.setVisible(false);
        }
        else {
            phoneLabel.setText(event.getPhone_number());
            eventOrganiserLabel.setVerticalAlignment(BOTTOM);
        }
    }

    public void populateMembers(Event event){

        List<EventMember> eventMembers = showEventMembers(event);
        DefaultListModel<EventMember> memberListModel = new DefaultListModel<>();

        for (EventMember member : eventMembers) {
            memberListModel.addElement(member);
        }

        memberList.setModel(memberListModel);
        memberList.setCellRenderer(new MemberCellRenderer());
        memberList.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
    }

    public List<EventMember> showEventMembers(Event event) {
        List<EventMember> eventMembers = new ArrayList<>();

        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Prepare the SQL statement for retrieving event_ids
            String sql = "SELECT user_id, relation FROM event_members WHERE event_id = ? AND relation IN ('creator', 'participant')";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, event.getId());


            // Execute the query to get event_ids
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and retrieve events for each event_id
            while (resultSet.next()) {
                int user_id = resultSet.getInt("user_id");
                String relation = resultSet.getString("relation");

                // Prepare the SQL statement for retrieving event details
                String eventSQL = "SELECT * FROM users WHERE id = ?";
                PreparedStatement eventStatement = conn.prepareStatement(eventSQL);
                eventStatement.setInt(1, user_id);

                // Execute the query to get event details
                ResultSet eventResultSet = eventStatement.executeQuery();

                if (eventResultSet.next()) {
                    String memberName = eventResultSet.getString("username");
                    String email = eventResultSet.getString("email");
                    String phone_number = eventResultSet.getString("phone_number");

                    // Create an Event object and add it to the list
                    EventMember eventMember = new EventMember(user_id, memberName, email, phone_number, relation);
                    eventMembers.add(eventMember);
                }

                // Close the event result set and statement
                eventResultSet.close();
                eventStatement.close();
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eventMembers;
    }

    private void closeEventForm() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(spMainPanel);
        frame.dispose();
    }


}
