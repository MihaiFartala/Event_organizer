import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class startingPageForm {
    private JButton logoutButton;
    private JPanel buttonsPanel;
    private JPanel spMainPanel;
    private JButton joinButton;
    private JButton calendarButton;
    private JButton eventsButton;
    private JButton profileButton;
    private JPanel profilePanel;
    private JPanel calendarPanel;
    private JButton profileButton1;
    private JButton joinButton1;
    private JPanel joinPanel;
    private JPanel eventsPanel;
    private JLabel monthLabel;
    private JButton prevButton, nextButton;
    private JTable calendarTable;
    private JScrollPane calendarScroll;
    private JTextField monthTextField;
    private JTextField dayTextField;
    private JTextField yearTextField;
    private JButton createEventButton;
    private JList<Event> eventsList;
    public static int month;
    public static int year;

    public startingPageForm(loggedUser loggedUser) //  loggedUser
    {

        JFrame frame = new JFrame("Organizer");
        frame.setContentPane(spMainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1200,800);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        calendarButton.setFocusable(false);
        joinButton.setFocusable(false);
        eventsButton.setFocusable(false);
        profileButton.setFocusable(false);
        logoutButton.setFocusable(false);

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinPanel.setVisible(true);
                calendarPanel.setVisible(false);
                eventsPanel.setVisible(false);
                profilePanel.setVisible(false);
            }
        });
        calendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                month = java.time.MonthDay.now().getMonthValue(); // Set the initial month to the current month
                year = java.time.LocalDate.now().getYear(); // Set the initial year to the current year

                monthLabel.setText(java.time.Month.of(month).toString() + " " + year);

                CalendarTableModel tableModel = new CalendarTableModel(month, year);
//
//
                // Set the calendar table's properties
                calendarTable.setModel(tableModel);
                calendarTable.setRowHeight(48);
                calendarTable.setShowGrid(true);
                calendarTable.setGridColor(Color.BLACK);
                calendarTable.setDefaultRenderer(Object.class, new CalendarTableRenderer());


                joinPanel.setVisible(false);
                calendarPanel.setVisible(true);
                eventsPanel.setVisible(false);
                profilePanel.setVisible(false);

                calendarTable.addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent e) {
                        int selectedRow = calendarTable.getSelectedRow();
                        int selectedColumn = calendarTable.getSelectedColumn();

                        // Verificați dacă selecția este validă și conține o zi selectată
                        Object selectedValue = calendarTable.getValueAt(selectedRow, selectedColumn);
                        if (selectedRow != -1 && selectedColumn != -1 && selectedValue != "") {
                            // Actualizați câmpurile de dată
                            dayTextField.setText(selectedValue.toString());
                            monthTextField.setText(String.valueOf(month));
                            yearTextField.setText(String.valueOf(year));
                        }
                    }
                });


                // Add the listeners for the buttons
                prevButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        month--;
                        if (month == 0) {
                            month = 12;
                            year--;
                        }
                        monthLabel.setText(java.time.Month.of(month).toString() + " " + year);
                        calendarTable.setModel(new CalendarTableModel(month, year));
                    }
                });
                nextButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        month++;
                        if (month == 13) {
                            month = 1;
                            year++;
                        }
                        monthLabel.setText(java.time.Month.of(month).toString() + " " + year);
                        calendarTable.setModel(new CalendarTableModel(month, year));
                    }
                });

                createEventButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Check if a date is selected
                        if (!dayTextField.getText().isEmpty()) {
                            // Get the selected date
                            int selectedDay = Integer.parseInt(dayTextField.getText());

                            // Create a new instance of NewEventForm and pass the selected date and loggedUser object
                            NewEventPopup newEventPopup = new NewEventPopup(frame, selectedDay, month, year, loggedUser.getUsername());
                            newEventPopup.setVisible(true);

                        } else {
                            // Display an error message
                            JOptionPane.showMessageDialog(frame, "No date selected","", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });


            }
        });
        eventsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinPanel.setVisible(false);
                calendarPanel.setVisible(false);
                eventsPanel.setVisible(true);
                profilePanel.setVisible(false);

                // Retrieve the user's events from the database
                DefaultListModel<Event> userEvents = retrieveUserEvents(loggedUser.getUsername());
                eventsList.setCellRenderer(new EventCellRenderer());

                // Set the list model to the eventsList
                eventsList.setModel(userEvents);
                eventsList.setCellRenderer(new EventCellRenderer());
            }
        });


        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinPanel.setVisible(false);
                calendarPanel.setVisible(false);
                eventsPanel.setVisible(false);
                profilePanel.setVisible(true);
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                signUpForm form = new signUpForm();
            }
        });
    }
    private DefaultListModel<Event> retrieveUserEvents(String creator) {
        DefaultListModel<Event> listModel = new DefaultListModel<>();

        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Prepare the SQL statement for retrieving events
            String sql = "SELECT * FROM events WHERE organizer = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, creator);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and create Event objects
            while (resultSet.next()) {
                int eventId = resultSet.getInt("id");
                String eventName = resultSet.getString("name");
                String organizer = resultSet.getString("organizer");
                Date date = resultSet.getDate("date");

                // Create an Event object and add it to the list model
                Event event = new Event(eventId, eventName, organizer, date);
                listModel.addElement(event);
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listModel;
    }


}


