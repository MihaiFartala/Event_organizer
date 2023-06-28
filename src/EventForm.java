import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import static java.sql.Types.NULL;
import static javax.swing.SwingConstants.BOTTOM;

public class EventForm extends JFrame {
    private JPanel buttonsPanel;
    private JPanel spMainPanel;
    private JPanel invitePanel;
    private JPanel adminEditPanel;
    private JPanel memberListPanel;
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

        //adminEditButton.setVisible(new Color());

        selectedEvent = currentEvent;

        adminEditButton.setVisible(loggedUser.getId() == currentEvent.getOrganizer_id());

        EventForm.loggedUser = loggedUser;

        adminEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminEditPanel.setVisible(true);
                chatPanel.setVisible(false);
                invitePanel.setVisible(false);
                memberListPanel.setVisible(false);
            }
        });

        chatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminEditPanel.setVisible(false);
                chatPanel.setVisible(true);
                invitePanel.setVisible(false);
                memberListPanel.setVisible(false);
            }
        });

        inviteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminEditPanel.setVisible(false);
                chatPanel.setVisible(false);
                invitePanel.setVisible(true);
                memberListPanel.setVisible(false);
            }
        });

        memberListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminEditPanel.setVisible(false);
                chatPanel.setVisible(false);
                invitePanel.setVisible(false);
                memberListPanel.setVisible(true);
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
        eventDateLabel.setText("Date: " + event.getDate());
        if(event.getPhone_number() == null){
            phoneLabel.setVisible(false);
        }
        else {
            phoneLabel.setText(event.getPhone_number());
            eventOrganiserLabel.setVerticalAlignment(BOTTOM);
        }



    }

    private void closeEventForm() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(spMainPanel);
        frame.dispose();
    }

}
