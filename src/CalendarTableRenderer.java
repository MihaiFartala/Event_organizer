import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

class CalendarTableRenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        if (column == 0 || column == 6) {
            label.setForeground(Color.RED);
        } else {
            label.setForeground(Color.BLACK);
        }
        if (value != null && value != "") {
            int day = Integer.parseInt(value.toString());
            java.time.LocalDate today = java.time.LocalDate.now();
            if (day == today.getDayOfMonth() && today.getMonthValue() == startingPageForm.month && today.getYear() == startingPageForm.year) {
                label.setBackground(Color.YELLOW);
            } else {
                label.setBackground(Color.WHITE);
            }
        } else {
            label.setBackground(Color.WHITE);
        }
        return label;
    }
}