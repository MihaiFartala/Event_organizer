import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonsStyle {

    static void applyButtonStyles(JButton button) {
        button.setFocusable(false);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(146,26,44)); // Set your desired button color here

        // Create a rounded border
        int borderRadius = 10;
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(button.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, borderRadius, borderRadius);
                g2.dispose();
                super.paint(g, c);
            }
        });

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBackground().darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(146,26,44)); // Restore the original button color
            }
        });
    }
}
