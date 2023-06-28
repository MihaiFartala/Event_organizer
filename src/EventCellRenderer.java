import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
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
        Border matteBorder = new MatteBorder(0, 0, 1, 0, Color.BLACK);
        Border emptyBorder = new EmptyBorder(17, 10, 0, 0);
        Border compoundBorder = BorderFactory.createCompoundBorder(matteBorder, emptyBorder);
        textPanel.setBorder(compoundBorder);
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
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime eventTime = value.getDate();
        Duration duration = Duration.between(currentTime, eventTime);

        if (duration.isNegative() || duration.isZero()) {
            // Event has already passed
            timeLeftLabel.setText("<html><font color='red'>Event has passed</font></html>");
        } else {
            long days = duration.toDays();
            long hours = duration.toHoursPart();
            long minutes = duration.toMinutesPart();

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
