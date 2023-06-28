import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

// Create a custom ScrollBarUI
public class ScrollBarStyle extends BasicScrollBarUI {

    // Override the paintThumb method to customize the appearance of the thumb
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Customize the thumb color and shape
        g2.setPaint(new Color(192, 192, 192));
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width - 1, thumbBounds.height, 5, 5);

        g2.dispose();
    }

    // Override the paintTrack method to customize the appearance of the track
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Customize the track color and shape
        g2.setPaint(new Color(240, 240, 240));
        g2.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width - 1, trackBounds.height, 5, 5);

        g2.dispose();
    }
}
