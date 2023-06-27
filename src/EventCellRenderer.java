import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventCellRenderer extends JPanel implements ListCellRenderer<Event> {
    private final JLabel nameLabel;
    private final JLabel organizerLabel;
    private final JLabel timeLeftLabel;

    public EventCellRenderer() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel arrowLabel = new JLabel("\u27F6"); // Bullet point Unicode character
        arrowLabel.setFont(arrowLabel.getFont().deriveFont(Font.BOLD, 16f));
        arrowLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(arrowLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(new EmptyBorder(17, 10, 0, 0)); // Add spacing between bullet and labels
        textPanel.setOpaque(false);

        nameLabel = new JLabel();
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));
        textPanel.add(nameLabel, BorderLayout.NORTH);

        organizerLabel = new JLabel();
        organizerLabel.setFont(organizerLabel.getFont().deriveFont(Font.ITALIC, 12f));
        textPanel.add(organizerLabel, BorderLayout.CENTER);

        timeLeftLabel = new JLabel();
        timeLeftLabel.setFont(timeLeftLabel.getFont().deriveFont(Font.PLAIN, 12f));
        textPanel.add(timeLeftLabel, BorderLayout.EAST);

        add(textPanel, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Event> list, Event value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        nameLabel.setText("<html><b>Event:</b> " + value.getName() + "</html>");
        organizerLabel.setText("<html><b>Organizer:</b> " + value.getOrganizer() + "</html>");

        // Calculate the time difference
        long currentTime = System.currentTimeMillis();
        long eventTime = value.getDate().getTime();
        long timeDifference = eventTime - currentTime;

        if (timeDifference <= 0) {
            // Event has already passed
            timeLeftLabel.setText("<html><font color='red'>Event has passed</font></html>");
        } else {
            // Convert the time difference to days, hours, and minutes
            long days = timeDifference / (24 * 60 * 60 * 1000);
            long hours = (timeDifference % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
            long minutes = (timeDifference % (60 * 60 * 1000)) / (60 * 1000);

            String timeLeft = "<html><b>Time Left:</b> " + days + " days, " + hours + " hours, " + minutes + " minutes</html>";
            timeLeftLabel.setText(timeLeft);
        }

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
