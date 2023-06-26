import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;

public class EventCellRenderer extends JPanel implements ListCellRenderer<Event> {
    private final JLabel eventLabel;
    private final JLabel organizerLabel;

    public EventCellRenderer() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 60)); // Adjust the preferred size as needed

        setBackground(new Color(227, 226, 219));

        eventLabel = new JLabel();
        eventLabel.setFont(eventLabel.getFont().deriveFont(Font.BOLD));
        add(eventLabel, BorderLayout.NORTH);

        // Create and configure the organizer label
        organizerLabel = new JLabel();
        organizerLabel.setFont(organizerLabel.getFont().deriveFont(Font.ITALIC));
        add(organizerLabel, BorderLayout.CENTER);

        // Create and configure the "Go To" button
        JButton goToButton = new JButton("Go To");
        goToButton.setFocusable(false);
        goToButton.setPreferredSize(new Dimension(70, 25));
        goToButton.setBackground(new Color(146, 26, 44)); // Set the background color
        goToButton.setForeground(new Color(248, 248, 248)); // Set the foreground color
        add(goToButton, BorderLayout.EAST);

        // Add vertical spacing between rows
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

//        // Add ActionListener to the "Go To" button
//        goToButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JList<Event> eventList = findEventList(EventCellRenderer.this);
//                Event selectedEvent = eventList.getSelectedValue();
//                if (selectedEvent != null) {
//                    // Open a new form specific to the selected event
//                    EventDetailsForm eventDetailsForm = new EventDetailsForm(selectedEvent);
//                    eventDetailsForm.setVisible(true);
//                }
//            }
//        });
    }

    private JList<Event> findEventList(Container container) {
        if (container instanceof JList) {
            return (JList<Event>) container;
        } else if (container.getParent() != null) {
            return findEventList(container.getParent());
        } else {
            throw new IllegalStateException("EventCellRenderer is not contained within a JList");
        }
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Event> list, Event value, int index, boolean isSelected, boolean cellHasFocus) {
        eventLabel.setText("Event: " + value.getName());
        organizerLabel.setText("Created by: " + value.getOrganizer());

        // Set the background and foreground color based on selection
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }
}
