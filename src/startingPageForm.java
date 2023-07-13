import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    private JPanel datePanel;
    private JPanel calendarTablePanel;
    private JList<Event> ownEventsList;
    private JPasswordField oldPassField;
    private JPasswordField newPassField;
    private JPasswordField confNewPassField;
    private JButton changePasswordButton;
    private JPanel passwordPanel;
    private JTextField updatePhoneNumberField;
    private JTextField updateEmailField;
    private JTextField updateUsernameField;
    private JButton updateUsernameButton;
    private JButton updateEmailButton;
    private JButton updatePhoneNumberButton;
    private JPanel personalDataPanel;
    private JLabel phoneLabel;
    private JLabel emailLabel;
    private JLabel usernameLabel;
    private JPanel userInfoPanel;
    private JScrollPane memberScrollPane;
    private JScrollPane ownScrollPane;
    private JPanel enterPanel;
    private JPanel searchPanel;
    private JScrollPane searchPane;
    private JTextField enterCodeField;
    private JButton joinEventButton;
    private JList<Event> searchList;
    private JLabel phoneCheckLabel;
    private JPanel userUpdatePanel;
    private JPanel userInvitesPanel;
    private JScrollPane requestsPane;
    private JScrollPane invitesPane;
    private JList<Event> invitesList;
    private JList<Event> requestsList;
    public static int month;
    public static int year;
    private boolean eventFormOpen = false;
    private boolean eventCreateOpen = false;
    private boolean profileOpen = false;
    private int joinCounter = 0;

    private List<EventForm> openEventForms = new ArrayList<>();

    public startingPageForm(loggedUser loggedUser) //  loggedUser
    {

        JFrame frame = new JFrame("Organizer");
        frame.setContentPane(spMainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);


        ButtonsStyle.applyButtonStyles(calendarButton);
        ButtonsStyle.applyButtonStyles(joinButton);
        ButtonsStyle.applyButtonStyles(eventsButton);
        ButtonsStyle.applyButtonStyles(profileButton);
        ButtonsStyle.applyButtonStyles(logoutButton);
        ButtonsStyle.applyButtonStyles(createEventButton);
        ButtonsStyle.applyButtonStyles(nextButton);
        ButtonsStyle.applyButtonStyles(prevButton);
        ButtonsStyle.applyButtonStyles(changePasswordButton);
        ButtonsStyle.applyButtonStyles(updateUsernameButton);
        ButtonsStyle.applyButtonStyles(updateEmailButton);
        ButtonsStyle.applyButtonStyles(updatePhoneNumberButton);
        ButtonsStyle.applyButtonStyles(joinEventButton);

        FieldsStyle.applyStyle(dayTextField);
        FieldsStyle.applyStyle(monthTextField);
        FieldsStyle.applyStyle(yearTextField);
        FieldsStyle.applyStyle(newPassField);
        FieldsStyle.applyStyle(oldPassField);
        FieldsStyle.applyStyle(confNewPassField);
        FieldsStyle.applyStyle(updateUsernameField);
        FieldsStyle.applyStyle(updateEmailField);
        FieldsStyle.applyStyle(updatePhoneNumberField);
        FieldsStyle.applyStyle(enterCodeField);

        memberScrollPane.getVerticalScrollBar().setUI(new ScrollBarStyle());
        ownScrollPane.getVerticalScrollBar().setUI(new ScrollBarStyle());
        searchPane.getVerticalScrollBar().setUI(new ScrollBarStyle());
        invitesPane.getVerticalScrollBar().setUI(new ScrollBarStyle());
        requestsPane.getVerticalScrollBar().setUI(new ScrollBarStyle());



        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinPanel.setVisible(true);
                calendarPanel.setVisible(false);
                eventsPanel.setVisible(false);
                profilePanel.setVisible(false);

                enterCodeField.setText("");

                populateSearchList(loggedUser.getId());

            }
        });


        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinPanel.setVisible(false);
                calendarPanel.setVisible(false);
                eventsPanel.setVisible(false);
                profilePanel.setVisible(true);


                usernameLabel.setText(loggedUser.getUsername());
                emailLabel.setText(loggedUser.getEmail());
                if (loggedUser.getPhoneNumber() == null) {
                    phoneLabel.setText("-no phone number was set-");
                } else {
                    phoneLabel.setText(loggedUser.getPhoneNumber());
                }


                populateRequestList(loggedUser.getId());
                populateInviteList(loggedUser.getId());
            }
        });


        profileButton.doClick();
        calendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (loggedUser.getPhoneNumber() != null) {
                    createEventButton.setEnabled(true);
                    phoneCheckLabel.setVisible(false);
                } else {
                    createEventButton.setEnabled(false);
                    phoneCheckLabel.setVisible(true);
                    phoneCheckLabel.setText("<html>You need a phone number to<br>be able to create events!<br>Go to profile and set one!</html>");
                }

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
                // Check if the event form is already open

                if (eventCreateOpen) {
                    return; // Do nothing if the form is already open
                }

                // Set the flag to indicate that the event form is now open
                eventCreateOpen = true;

                // Check if a date is selected
                if (!dayTextField.getText().isEmpty()) {
                    // Get the selected date
                    int selectedDay = Integer.parseInt(dayTextField.getText());

                    // Create a new instance of NewEventForm and pass the selected date and loggedUser object
                    NewEventPopup newEventPopup = new NewEventPopup(frame, selectedDay, month, year, loggedUser);

                    // Add a window listener to the event form
                    newEventPopup.addWindowListener(new WindowAdapter() {
                        public void windowClosed(WindowEvent e) {
                            // Reset the flag when the event form is closed
                            eventCreateOpen = false;
                        }
                    });

                    newEventPopup.setVisible(true);
                } else {
                    // Display an error message
                    JOptionPane.showMessageDialog(frame, "No date selected", "", JOptionPane.ERROR_MESSAGE);

                    // Reset the flag since the event form was not opened
                    eventCreateOpen = false;
                }
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
                    changePasswordButton.doClick();
                }
            }
        }

        oldPassField.addKeyListener(new EnterKeyListener());
        newPassField.addKeyListener(new EnterKeyListener());
        confNewPassField.addKeyListener(new EnterKeyListener());

        class EnterKeyListener2 implements KeyListener {
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
                    joinEventButton.doClick();
                }
            }
        }

        enterCodeField.addKeyListener(new EnterKeyListener2());


        eventsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinPanel.setVisible(false);
                calendarPanel.setVisible(false);
                eventsPanel.setVisible(true);
                profilePanel.setVisible(false);


                List<Event> userEvents = retrieveUserEvents(loggedUser.getId());
                DefaultListModel<Event> eventsListModel = new DefaultListModel<>();

                for (Event event : userEvents) {
                    eventsListModel.addElement(event);
                }

                eventsList.setModel(eventsListModel);
                eventsList.setCellRenderer(new EventCellRenderer());

                eventsList.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));


                eventsList.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            int selectedIndex = eventsList.getSelectedIndex();
                            if (selectedIndex != -1) {
                                Event selectedEvent = eventsList.getModel().getElementAt(selectedIndex);

                                // Check if an event form is already open
                                for (EventForm openEventForm : openEventForms) {
                                    if (openEventForm.getSelectedEvent().equals(selectedEvent)) {
                                        // Event form is already open, bring it to front and return
                                        openEventForm.toFront();
                                        eventsButton.doClick();
                                        return;
                                    }
                                }

                                EventForm eventForm = new EventForm(selectedEvent, loggedUser);
                                openEventForms.add(eventForm);

                                eventForm.addWindowListener(new WindowAdapter() {
                                    public void windowClosed(WindowEvent e) {
                                        openEventForms.remove(eventForm);
                                    }
                                });
                            }
                        }
                    }
                });


                List<Event> ownEvents = retrieveOwnEvents(loggedUser.getId());
                DefaultListModel<Event> ownEventsListModel = new DefaultListModel<>();

                for (Event event : ownEvents) {
                    ownEventsListModel.addElement(event);
                }

                ownEventsList.setModel(ownEventsListModel);
                ownEventsList.setCellRenderer(new EventCellRenderer());

                ownEventsList.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));

                ownEventsList.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            int selectedIndex = ownEventsList.getSelectedIndex();
                            if (selectedIndex != -1) {
                                Event selectedEvent = ownEventsList.getModel().getElementAt(selectedIndex);

                                // Check if an event form is already open
                                for (EventForm openEventForm : openEventForms) {
                                    if (openEventForm.getSelectedEvent().equals(selectedEvent)) {
                                        // Event form is already open, bring it to front and return
                                        openEventForm.toFront();
                                        eventsButton.doClick();
                                        return;
                                    }
                                }

                                EventForm eventForm = new EventForm(selectedEvent, loggedUser);
                                openEventForms.add(eventForm);

                                eventForm.addWindowListener(new WindowAdapter() {
                                    public void windowClosed(WindowEvent e) {
                                        openEventForms.remove(eventForm);
                                    }
                                });
                            }
                        }
                    }
                });

                eventsPanel.revalidate();
                eventsPanel.repaint();
            }
        });

        updateUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Username = updateUsernameField.getText();

                if (Username.equals(loggedUser.getUsername())) {
                    JOptionPane.showMessageDialog(profilePanel, "Same username was introduced", "Username Change Failed", JOptionPane.ERROR_MESSAGE);
                    frame.requestFocusInWindow();
                    return;
                }

                if (checkUsernameValidity(Username) && checkUsernameDuplicity(Username)) {
                    try {
                        // Establish a database connection
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

                        // Prepare the SQL statement for updating the password
                        String sql = "UPDATE users SET username = ? WHERE id = ?";
                        PreparedStatement statement = conn.prepareStatement(sql);
                        statement.setString(1, Username);
                        statement.setInt(2, loggedUser.getId());

                        // Execute the update
                        statement.executeUpdate();

                        // Close the statement and connection
                        statement.close();
                        conn.close();

                        JOptionPane.showMessageDialog(profilePanel, "Username was updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                        frame.requestFocusInWindow();

                        loggedUser.setUsername(Username);
                        updateUsernameField.setText("");
                        profileButton.doClick();
                        // Return true if the update affected at least one row
                    } catch (SQLException f) {
                        f.printStackTrace();
                    }
                } else {
                    return;
                }
            }
        });

        updateEmailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Email = updateEmailField.getText();

                if (Email.equals(loggedUser.getEmail())) {
                    JOptionPane.showMessageDialog(profilePanel, "Same email was introduced", "Email Change Failed", JOptionPane.ERROR_MESSAGE);
                    frame.requestFocusInWindow();
                    return;
                }

                if (checkEmailValidity(Email) && checkEmailDuplicity(Email)) {
                    try {
                        // Establish a database connection
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

                        // Prepare the SQL statement for updating the password
                        String sql = "UPDATE users SET email = ? WHERE id = ?";
                        PreparedStatement statement = conn.prepareStatement(sql);
                        statement.setString(1, Email);
                        statement.setInt(2, loggedUser.getId());

                        // Execute the update
                        statement.executeUpdate();

                        // Close the statement and connection
                        statement.close();
                        conn.close();

                        JOptionPane.showMessageDialog(profilePanel, "Email was updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                        frame.requestFocusInWindow();

                        loggedUser.setEmail(Email);
                        updateEmailField.setText("");
                        profileButton.doClick();
                        // Return true if the update affected at least one row
                    } catch (SQLException f) {
                        f.printStackTrace();
                    }
                } else {
                    return;
                }
            }
        });

        updatePhoneNumberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Phone = updatePhoneNumberField.getText();

                if (Phone.equals(loggedUser.getPhoneNumber())) {
                    JOptionPane.showMessageDialog(profilePanel, "Same phone number was introduced", "Phone Number Change Failed", JOptionPane.ERROR_MESSAGE);
                    frame.requestFocusInWindow();
                    return;
                }

                if (Phone.length() == 10 && Phone.startsWith("07") && Phone.matches("\\d{10}")) {
                    try {
                        // Establish a database connection
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

                        // Prepare the SQL statement for updating the password
                        String sql = "UPDATE users SET phone_number = ? WHERE id = ?";
                        PreparedStatement statement = conn.prepareStatement(sql);
                        statement.setString(1, Phone);
                        statement.setInt(2, loggedUser.getId());

                        // Execute the update
                        statement.executeUpdate();

                        // Close the statement and connection
                        statement.close();
                        conn.close();

                        JOptionPane.showMessageDialog(profilePanel, "Phone number was updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                        frame.requestFocusInWindow();

                        loggedUser.setPhoneNumber(Phone);
                        updatePhoneNumberField.setText("");
                        profileButton.doClick();
                        // Return true if the update affected at least one row
                    } catch (SQLException f) {
                        f.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(profilePanel, "Please introduce a valid phone number", "Phone Number Change Failed", JOptionPane.ERROR_MESSAGE);
                    frame.requestFocusInWindow();
                    return;
                }
            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oldPassword = new String(oldPassField.getPassword());
                String newPassword = new String(newPassField.getPassword());
                String confirmPassword = new String(confNewPassField.getPassword());

                // Verify if the entered password matches the current password for the user
                boolean isPasswordCorrect = verifyPassword(loggedUser.getId(), oldPassword);

                if (!isPasswordCorrect) {
                    JOptionPane.showMessageDialog(profilePanel, "Incorrect password.", "Password Change Failed", JOptionPane.ERROR_MESSAGE);
                    frame.requestFocusInWindow();
                    return;
                }

                // Check if the new password and confirmation password match
                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(profilePanel, "New password and confirmation password do not match.", "Password Change Failed", JOptionPane.ERROR_MESSAGE);
                    frame.requestFocusInWindow();
                    return;
                }

                if (!checkPasswordValidity(newPassword)) {
                    return;
                }
                // Update the user's password in the database with the new password
                boolean isPasswordUpdated = updatePassword(loggedUser.getId(), newPassword);

                if (isPasswordUpdated) {
                    JOptionPane.showMessageDialog(passwordPanel, "Password has been successfully updated.", "Password Change Successful", JOptionPane.INFORMATION_MESSAGE);
                    // Clear the password fields
                    oldPassField.setText("");
                    newPassField.setText("");
                    confNewPassField.setText("");
                    frame.requestFocusInWindow();
                } else {
                    JOptionPane.showMessageDialog(passwordPanel, "Failed to update the password The password must have between 8 and 25 characters, have letters, numbers, symbols and no spaces.", "Password Change Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        joinEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = enterCodeField.getText();

                int eventId = retrieveEventIdByJoinCode(code);
                if (eventId != -1) {
                    joinEvent(eventId, loggedUser.getId());
                    enterCodeField.setText("");
                } else {
                    JOptionPane.showMessageDialog(joinPanel, "Event does not exist!", "Password error", JOptionPane.INFORMATION_MESSAGE);
                    joinPanel.requestFocusInWindow();
                }
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

    private int retrieveEventIdByJoinCode(String joinCode) {
        int eventId = -1; // Default value if event not found

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Prepare the SQL statement for retrieving the event ID
            String sql = "SELECT id FROM events WHERE join_code = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, joinCode);

            // Execute the query to get the event ID
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                eventId = resultSet.getInt("id");
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eventId;
    }


    private boolean verifyPassword(int userId, String password) {
        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Prepare the SQL statement for retrieving the current password
            String sql = "SELECT password FROM users WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String currentPassword = resultSet.getString("password");

                return Objects.equals(PasswordHash.hashPassword(password), currentPassword);

            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean updatePassword(int userId, String newPassword) {
        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Prepare the SQL statement for updating the password
            String sql = "UPDATE users SET password = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, PasswordHash.hashPassword(newPassword));
            statement.setInt(2, userId);

            // Execute the update
            int rowsAffected = statement.executeUpdate();

            // Close the statement and connection
            statement.close();
            conn.close();

            // Return true if the update affected at least one row
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Boolean checkPasswordValidity(String password) {
        if (password.contains(" ")) {
            JOptionPane.showMessageDialog(this.profilePanel, "The password can not contain spaces", "Password error", JOptionPane.WARNING_MESSAGE);
            profilePanel.requestFocusInWindow();
            return false;
        }
        if (password.length() < 8 || password.length() > 25) {
            JOptionPane.showMessageDialog(this.profilePanel, "The new password must have between 8 and 25 characters", "Password error", JOptionPane.WARNING_MESSAGE);
            profilePanel.requestFocusInWindow();
            return false;
        } else {
            Pattern lowercase = Pattern.compile("[a-z]");
            Pattern uppercase = Pattern.compile("[A-Z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasLowercase = lowercase.matcher(password);
            Matcher hasUppercase = uppercase.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            if (!(hasLowercase.find() && hasDigit.find() && hasSpecial.find() && hasUppercase.find())) {
                JOptionPane.showMessageDialog(this.profilePanel, "The new password must be a combination of uppercase letters, lowercase letters, numbers and symbols", "Password error", JOptionPane.WARNING_MESSAGE);
                profilePanel.requestFocusInWindow();
                return false;
            }
        }
        return true;
    }


    private List<Event> retrieveUserEvents(int id) {
        List<Event> eventsList = new ArrayList<>();

        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Prepare the SQL statement for retrieving event_ids
            String sql = "SELECT event_id FROM event_members WHERE user_id = ? AND relation NOT IN ('creator', 'request', 'invited', 'banned', 'kicked')";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);


            // Execute the query to get event_ids
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and retrieve events for each event_id
            while (resultSet.next()) {
                int eventId = resultSet.getInt("event_id");

                // Prepare the SQL statement for retrieving event details
                String eventSQL = "SELECT * FROM events WHERE id = ? ORDER BY date";
                PreparedStatement eventStatement = conn.prepareStatement(eventSQL);
                eventStatement.setInt(1, eventId);

                // Execute the query to get event details
                ResultSet eventResultSet = eventStatement.executeQuery();

                if (eventResultSet.next()) {
                    String eventName = eventResultSet.getString("name");
                    String organizer = eventResultSet.getString("organizer");
                    Timestamp timestamp = eventResultSet.getTimestamp("date"); // Use Timestamp instead of Date
                    String phone_number = eventResultSet.getString("organizer_number");
                    int organizer_id = eventResultSet.getInt("organizer_id");
                    String joinCode = eventResultSet.getString("join_code");

                    LocalDateTime date = timestamp.toLocalDateTime(); // Convert Timestamp to LocalDateTime
                    // Create an Event object and add it to the list
                    Event event = new Event(eventId, eventName, organizer, date, phone_number, organizer_id, joinCode);
                    eventsList.add(event);
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

        return eventsList;
    }

    private void populateSearchList(int user_id) {
        // Clear existing data and listeners
        DefaultListModel<Event> eventsListModel = new DefaultListModel<>();
        searchList.setModel(eventsListModel);
        searchList.setCellRenderer(new EventCellRenderer());
        searchList.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
        searchList.clearSelection();
        searchList.addMouseListener(null);

        // Populate the list with new data
        List<Event> events = retrieveEvents(user_id);
        for (Event event : events) {
            eventsListModel.addElement(event);
        }

        // Add a new mouse listener to searchList
        searchList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = searchList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        Event selectedEvent = searchList.getModel().getElementAt(selectedIndex);

                        joinCounter++;

                        if (joinCounter == 1) {
                            if (requestJoinEvent(selectedEvent.getId(), user_id)) {
                                joinCounter = 0;
                            }
                        }
                    }
                }
            }
        });
    }

    private void populateRequestList(int user_id) {
        // Clear existing data and listeners
        DefaultListModel<Event> eventsRequestListModel = new DefaultListModel<>();
        requestsList.setModel(eventsRequestListModel);
        requestsList.setCellRenderer(new EventCellRenderer());
        requestsList.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
        requestsList.clearSelection();
        requestsList.addMouseListener(null);

        // Populate the list with new data
        List<Event> events = retrieveRequestEvents(user_id);
        for (Event event : events) {
            eventsRequestListModel.addElement(event);
        }

        requestsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = requestsList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        Event selectedEvent = requestsList.getModel().getElementAt(selectedIndex);

                        int confirm = JOptionPane.showConfirmDialog(requestsList, "Do you want to cancel the request?", "Cancel Request", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            cancelRequest(selectedEvent.getId(), user_id);
                            populateRequestList(user_id);
                        }
                    }
                }
            }
        });


    }

    private void cancelRequest(int eventId, int userId) {
        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Prepare the SQL statement to delete the row
            String sql = "DELETE FROM event_members WHERE event_id = ? AND user_id = ? AND relation = 'request'";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, eventId);
            statement.setInt(2, userId);

            // Execute the delete statement
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(requestsList, "Request canceled successfully!", "Request Canceled", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(requestsList, "Unable to cancel request. It may have already been accepted.", "Request Cancellation Failed", JOptionPane.WARNING_MESSAGE);
            }

            // Close the statement and connection
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void populateInviteList(int user_id) {
        // Clear existing data and listeners
        DefaultListModel<Event> eventsInvitesListModel = new DefaultListModel<>();
        invitesList.setModel(eventsInvitesListModel);
        invitesList.setCellRenderer(new EventCellRenderer());
        invitesList.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
        invitesList.clearSelection();
        invitesList.addMouseListener(null);

        // Populate the list with new data
        List<Event> events = retrieveInviteEvents(user_id);
        for (Event event : events) {
            eventsInvitesListModel.addElement(event);
        }

        invitesList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = invitesList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        Event selectedEvent = invitesList.getModel().getElementAt(selectedIndex);

                        int confirm = JOptionPane.showConfirmDialog(invitesList, "Do you want to accept the request?", "Event Accept", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            acceptInvite(selectedEvent.getId(), user_id);
                            populateInviteList(user_id);
                        } else if (confirm == JOptionPane.NO_OPTION){
                            deleteInvite(selectedEvent.getId(), user_id);
                            JOptionPane.showMessageDialog(profilePanel, "You have declined the invitation.", "Invitation deleted", JOptionPane.INFORMATION_MESSAGE);
                            populateInviteList(user_id);
                            profilePanel.requestFocusInWindow();
                        }
                    }
                }
            }
        });

    }


    private void deleteInvite(int eventId, int userId) {
        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Prepare the SQL statement to delete the row
            String sql = "DELETE FROM event_members WHERE event_id = ? AND user_id = ? AND relation = 'invited'";
            PreparedStatement deleteStatement = conn.prepareStatement(sql);
            deleteStatement.setInt(1, eventId);
            deleteStatement.setInt(2, userId);

            deleteStatement.executeUpdate();


            deleteStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void acceptInvite(int eventId, int userId) {
        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Prepare the SQL statement to delete the row
            String sql = "DELETE FROM event_members WHERE event_id = ? AND user_id = ? AND relation = 'invited'";
            PreparedStatement deleteStatement = conn.prepareStatement(sql);
            deleteStatement.setInt(1, eventId);
            deleteStatement.setInt(2, userId);

            deleteStatement.executeUpdate();


            deleteStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        joinEvent(eventId, userId);
    }


    private boolean requestJoinEvent(int event_id, int user_id) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Check if the user already has a row with the given event ID
            String checkSql = "SELECT COUNT(*) FROM event_members WHERE user_id = ? AND event_id = ?";
            PreparedStatement checkStatement = conn.prepareStatement(checkSql);
            checkStatement.setInt(1, user_id);
            checkStatement.setInt(2, event_id);
            ResultSet checkResult = checkStatement.executeQuery();
            checkResult.next();
            int rowCount = checkResult.getInt(1);
            checkResult.close();
            checkStatement.close();

            if (rowCount > 0) {
                JOptionPane.showMessageDialog(profilePanel, "You have already requested to join this event.", "Request already sent!", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }

            // Insert the new row
            String insertSql = "INSERT INTO event_members (event_id, user_id, relation) VALUES (?, ?, ?)";
            PreparedStatement insertStatement = conn.prepareStatement(insertSql);
            insertStatement.setInt(1, event_id);
            insertStatement.setInt(2, user_id);
            insertStatement.setString(3, "request");
            insertStatement.executeUpdate();
            insertStatement.close();

            conn.close();

            JOptionPane.showMessageDialog(profilePanel, "Request was sent!", "Requested!", JOptionPane.INFORMATION_MESSAGE);

            // Manually update the search list
            populateSearchList(user_id);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    private void joinEvent(int event_id, int user_id) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Check if the user already has a row with the given event ID
            String checkSql = "SELECT COUNT(*) AS count, relation FROM event_members WHERE user_id = ? AND event_id = ?";
            PreparedStatement checkStatement = conn.prepareStatement(checkSql);
            checkStatement.setInt(1, user_id);
            checkStatement.setInt(2, event_id);
            ResultSet checkResult = checkStatement.executeQuery();
            checkResult.next();
            int rowCount = checkResult.getInt("count");
            String relation = checkResult.getString("relation");

            if (rowCount > 0) {
                switch (relation) {
                    case "invited" -> {
                        JOptionPane.showMessageDialog(profilePanel, "You have already been invited to this event. You can accept the invite from your Profile page!", "Already Invited", JOptionPane.INFORMATION_MESSAGE);
                        joinPanel.requestFocusInWindow();
                        return;
                    }
                    case "participant", "creator" -> {
                        JOptionPane.showMessageDialog(profilePanel, "You have already joined this event.", "Already Joined", JOptionPane.INFORMATION_MESSAGE);
                        joinPanel.requestFocusInWindow();
                        return;
                    }
                    case "request" -> {
                        cancelRequest(event_id, user_id);
                        joinPanel.requestFocusInWindow();
                    }
                    case "kicked" -> {
                        JOptionPane.showMessageDialog(profilePanel, "You have been kicked from this event. You can join again via invite only!", "Kicked", JOptionPane.INFORMATION_MESSAGE);
                        joinPanel.requestFocusInWindow();
                        return;
                    }
                    case "banned" -> {
                        JOptionPane.showMessageDialog(profilePanel, "You have been banned from this event.", "Banned", JOptionPane.INFORMATION_MESSAGE);
                        joinPanel.requestFocusInWindow();
                        return;
                    }
                }
            }

            checkResult.close();
            checkStatement.close();

            // Insert the new row
            String insertSql = "INSERT INTO event_members (event_id, user_id) VALUES (?, ?)";
            PreparedStatement insertStatement = conn.prepareStatement(insertSql);
            insertStatement.setInt(1, event_id);
            insertStatement.setInt(2, user_id);
            insertStatement.executeUpdate();
            insertStatement.close();


            conn.close();

            JOptionPane.showMessageDialog(profilePanel, "Successfully joined the event!", "Joined!", JOptionPane.INFORMATION_MESSAGE);
            joinPanel.requestFocusInWindow();

            // Manually update the search list
            populateSearchList(user_id);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<Event> retrieveRequestEvents(int id) {
        List<Event> eventsList = new ArrayList<>();

        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            String sql = "SELECT e.id, e.name, e.organizer, e.date, e.organizer_number, e.organizer_id, e.join_code " +
                    "FROM events e " +
                    "JOIN event_members m ON e.id = m.event_id " +
                    "WHERE m.user_id = ? AND m.relation = 'request' " +
                    "ORDER BY e.date ASC";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);


            // Execute the query to get event details
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and retrieve events
            while (resultSet.next()) {
                int eventId = resultSet.getInt("id");
                String eventName = resultSet.getString("name");
                String organizer = resultSet.getString("organizer");
                Timestamp timestamp = resultSet.getTimestamp("date");
                String phone_number = resultSet.getString("organizer_number");
                int organizer_id = resultSet.getInt("organizer_id");
                String joinCode = resultSet.getString("join_code");

                LocalDateTime date = timestamp.toLocalDateTime(); // Convert Timestamp to LocalDateTime

                // Create an Event object and add it to the list
                LocalDateTime currentDateTime = LocalDateTime.now(); // Use LocalDateTime instead of Calendar
                if (!date.isBefore(currentDateTime)) {
                    Event event = new Event(eventId, eventName, organizer, date, phone_number, organizer_id, joinCode);
                    eventsList.add(event);
                }
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eventsList;
    }


    private List<Event> retrieveInviteEvents(int id) {
        List<Event> eventsList = new ArrayList<>();

        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            String sql = "SELECT e.id, e.name, e.organizer, e.date, e.organizer_number, e.organizer_id, e.join_code " +
                    "FROM events e " +
                    "JOIN event_members m ON e.id = m.event_id " +
                    "WHERE m.user_id = ? AND m.relation = 'invited' " +
                    "ORDER BY e.date ASC";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);


            // Execute the query to get event details
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and retrieve events
            while (resultSet.next()) {
                int eventId = resultSet.getInt("id");
                String eventName = resultSet.getString("name");
                String organizer = resultSet.getString("organizer");
                Timestamp timestamp = resultSet.getTimestamp("date");
                String phone_number = resultSet.getString("organizer_number");
                int organizer_id = resultSet.getInt("organizer_id");
                String joinCode = resultSet.getString("join_code");

                LocalDateTime date = timestamp.toLocalDateTime(); // Convert Timestamp to LocalDateTime

                // Create an Event object and add it to the list
                LocalDateTime currentDateTime = LocalDateTime.now(); // Use LocalDateTime instead of Calendar
                if (!date.isBefore(currentDateTime)) {
                    Event event = new Event(eventId, eventName, organizer, date, phone_number, organizer_id, joinCode);
                    eventsList.add(event);
                }
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eventsList;
    }


    private List<Event> retrieveEvents(int id) {
        List<Event> eventsList = new ArrayList<>();

        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Prepare the SQL statement for retrieving event_ids where there is no matching row in event_members
            String sql = "SELECT e.id, e.name, e.organizer, e.date, e.organizer_number, e.organizer_id, e.join_code " +
                    "FROM events e " +
                    "LEFT JOIN event_members m ON e.id = m.event_id AND m.user_id = ? " +
                    "WHERE m.event_id IS NULL " +
                    "ORDER BY e.date ASC";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            // Execute the query to get event details
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and retrieve events
            while (resultSet.next()) {
                int eventId = resultSet.getInt("id");
                String eventName = resultSet.getString("name");
                String organizer = resultSet.getString("organizer");
                Timestamp timestamp = resultSet.getTimestamp("date");
                String phone_number = resultSet.getString("organizer_number");
                int organizer_id = resultSet.getInt("organizer_id");
                String joinCode = resultSet.getString("join_code");

                LocalDateTime date = timestamp.toLocalDateTime(); // Convert Timestamp to LocalDateTime

                // Create an Event object and add it to the list
                LocalDateTime currentDateTime = LocalDateTime.now(); // Use LocalDateTime instead of Calendar
                if (!date.isBefore(currentDateTime)) {
                    Event event = new Event(eventId, eventName, organizer, date, phone_number, organizer_id, joinCode);
                    eventsList.add(event);
                }
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eventsList;
    }


    private List<Event> retrieveOwnEvents(int id) {
        List<Event> eventsList = new ArrayList<>();

        try {
            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            // Prepare the SQL statement for retrieving events
            String sql = "SELECT * FROM events WHERE organizer_id = ? ORDER BY date ASC";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and create Event objects
            while (resultSet.next()) {
                int eventId = resultSet.getInt("id");
                String eventName = resultSet.getString("name");
                String organizer = resultSet.getString("organizer");
                Timestamp timestamp = resultSet.getTimestamp("date"); // Use Timestamp instead of Date
                String phone_number = resultSet.getString("organizer_number");
                int organizer_id = resultSet.getInt("organizer_id");
                String joinCode = resultSet.getString("join_code");

                LocalDateTime date = timestamp.toLocalDateTime(); // Convert Timestamp to LocalDateTime
                // Create an Event object and add it to the list
                Event event = new Event(eventId, eventName, organizer, date, phone_number, organizer_id, joinCode);
                eventsList.add(event);
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eventsList;
    }

    private Boolean checkUsernameValidity(String user) {

        if (user.length() > 15 || user.length() < 5) {
            JOptionPane.showMessageDialog(profilePanel, "Username must have between 5 and 15 characters!", "Username error", JOptionPane.WARNING_MESSAGE);
            updateUsernameField.requestFocusInWindow();
            return false;
        }
        if (user.charAt(0) == Character.toLowerCase(user.charAt(0))) {
            JOptionPane.showMessageDialog(profilePanel, "The first character of the username must be a CAPITAL letter!", "Username error", JOptionPane.WARNING_MESSAGE);
            updateUsernameField.requestFocusInWindow();
            return false;
        }
        List<Character> specialCharacters = new ArrayList<>(List.of('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '[', ']', '{', '}', '<', '>', '?', '"', '\'', '\\', '|', ':', ';', '.', ',', '/', '~', '`', '=', '+'));
        for (Character chr : specialCharacters) {
            if (user.contains(String.valueOf(chr))) {
                JOptionPane.showMessageDialog(profilePanel, "Username cannot contain special characters, except for: ' - ' , ' _ ' and the space character!", "Username error", JOptionPane.WARNING_MESSAGE);
                updateUsernameField.requestFocusInWindow();
                return false;
            }
        }
        return true;
    }

    private Boolean checkUsernameDuplicity(String user) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            Statement stmt = conn.createStatement();
            String sql = "SELECT username from users WHERE username = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user);

            ResultSet rs = preparedStatement.executeQuery();


            if (rs.next()) {
                JOptionPane.showMessageDialog(profilePanel, "Username already taken!", "Username error", JOptionPane.INFORMATION_MESSAGE);
                updateUsernameField.setText("");
                updateUsernameField.requestFocus();
                return false;
            }

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private Boolean checkEmailValidity(String email) {

        if (email.contains(" ")) {
            JOptionPane.showMessageDialog(profilePanel, "Please introduce a valid email address", "Email error", JOptionPane.WARNING_MESSAGE);
            updateEmailField.requestFocusInWindow();
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);

        if (email == null) {
            JOptionPane.showMessageDialog(profilePanel, "Please introduce a valid email address", "Email error", JOptionPane.WARNING_MESSAGE);
            updateEmailField.requestFocusInWindow();
            return false;
        }

        if (pat.matcher(email).matches()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(profilePanel, "Please introduce a valid email address", "Email error", JOptionPane.WARNING_MESSAGE);
            updateEmailField.requestFocusInWindow();
            return false;
        }

    }

    private Boolean checkEmailDuplicity(String email) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer", "root", "");

            Statement stmt = conn.createStatement();
            String sql = "SELECT email from users " +
                    "WHERE email = '" + email + "'";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();


            if (rs.next()) {
                JOptionPane.showMessageDialog(profilePanel, "Email is already in use!", "Email error", JOptionPane.INFORMATION_MESSAGE);
                updateEmailField.requestFocusInWindow();
                updateEmailField.setText("");
                updateEmailField.requestFocus();
                return false;
            }

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}


