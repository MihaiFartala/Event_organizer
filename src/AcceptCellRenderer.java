import javax.swing.*;
import java.awt.*;

public class AcceptCellRenderer extends JLabel implements ListCellRenderer<String> {
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    private static final Color SELECTED_BACKGROUND_COLOR = new Color(195, 195, 195);

    public AcceptCellRenderer() {
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        setFont(getFont().deriveFont(Font.PLAIN, 12f));
        setHorizontalAlignment(SwingConstants.CENTER); // Align the label text in the center
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        setText("Request to join from: " + value);

        // Set the background and foreground color based on selection
        if (isSelected) {
            setBackground(SELECTED_BACKGROUND_COLOR);
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(BACKGROUND_COLOR);
            setForeground(list.getForeground());
        }

        return this;
    }
}
