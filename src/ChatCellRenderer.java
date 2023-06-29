import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ChatCellRenderer extends JPanel implements ListCellRenderer<Message> {
    private final JLabel senderLabel;
    private final JLabel messageLabel;
    private final JLabel dateLabel;

    public ChatCellRenderer(String user) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        messageLabel = new JLabel();
        messageLabel.setFont(messageLabel.getFont().deriveFont(Font.BOLD, 14f));

        senderLabel = new JLabel();
        senderLabel.setFont(senderLabel.getFont().deriveFont(Font.PLAIN, 10f));


        dateLabel = new JLabel();
        dateLabel.setFont(dateLabel.getFont().deriveFont(Font.PLAIN, 12f));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(senderLabel, BorderLayout.SOUTH);
        textPanel.add(messageLabel, BorderLayout.CENTER);
        textPanel.add(dateLabel, BorderLayout.EAST);

        add(textPanel, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Message> list, Message value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        senderLabel.setText("Sent by: " + value.getSender());
        messageLabel.setText(value.getMessage());

        LocalDateTime dateTime = value.getDate();
        LocalDateTime currentDateTime = LocalDateTime.now();

        String formattedDate;

        if (isSameDay(dateTime, currentDateTime)) {
            formattedDate = "Today " + dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (isSameDay(dateTime.plusDays(1), currentDateTime)) {
            formattedDate = "Yesterday " + dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            Duration duration = Duration.between(dateTime, currentDateTime);
            long days = duration.toDays();

            if (days >= 2 && days <= 6) {
                formattedDate = days + " days ago";
            } else {
                formattedDate = formatDate(dateTime);
            }
        }

        dateLabel.setText(formattedDate);

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

    private boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.toLocalDate().isEqual(dateTime2.toLocalDate());
    }

    private String formatDate(LocalDateTime dateTime) {
        long years = dateTime.until(LocalDateTime.now(), ChronoUnit.YEARS);
        if (years > 0) {
            return years + " years ago";
        }

        long months = dateTime.until(LocalDateTime.now(), ChronoUnit.MONTHS);
        if (months > 0) {
            return months + " months ago";
        }

        long weeks = dateTime.until(LocalDateTime.now(), ChronoUnit.WEEKS);
        if (weeks > 0) {
            return weeks + " weeks ago";
        }

        long days = dateTime.until(LocalDateTime.now(), ChronoUnit.DAYS);
        if (days > 0) {
            return days + " days ago";
        }

        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
