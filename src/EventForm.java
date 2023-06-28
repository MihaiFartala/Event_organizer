import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private JButton inviteButton;
    private JLabel eventNameLabel;
    private JLabel eventOrganiserLabel;
    private JLabel eventDateLabel;
    private JLabel phoneLabel;
    private JList<EventMember> memberList;
    private JLabel codeLabel;
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
        ButtonsStyle.applyButtonStyles(inviteButton);
        ButtonsStyle.applyButtonStyles(memberListButton);
        ButtonsStyle.applyButtonStyles(closeEventButton);

        adminEditButton.setBackground(new Color(220,180,20));

        selectedEvent = currentEvent;

        adminEditButton.setVisible(loggedUser.getId() == currentEvent.getOrganizer_id());

        EventForm.loggedUser = loggedUser;
        codeLabel.setText(selectedEvent.getJoin_code());

        adminEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                memberListPanel.setVisible(false);
                chatPanel.setVisible(false);
                invitePanel.setVisible(false);
                adminEditPanel.setVisible(true);
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

        inviteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                memberListPanel.setVisible(false);
                chatPanel.setVisible(false);
                invitePanel.setVisible(true);
                adminEditPanel.setVisible(false);
            }
        });

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
            String sql = "SELECT user_id, relation FROM event_members WHERE event_id = ?";
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
