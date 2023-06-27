import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class FieldsStyle {

    public static void applyStyle(JTextField textField) {
        textField.setBorder(createRoundedBorder());
        textField.setBackground(Color.WHITE);
        textField.setFont(textField.getFont().deriveFont(14f));
        textField.setForeground(Color.BLACK);
    }

    public static void applyStyle(JPasswordField passwordField) {
        passwordField.setBorder(createRoundedBorder());
        passwordField.setBackground(Color.WHITE);
        passwordField.setFont(passwordField.getFont().deriveFont(14f));
        passwordField.setForeground(Color.BLACK);
    }


    public static void applyStyle(JSpinner spinner) {
        spinner.setBorder(createRoundedBorder());
        spinner.setBackground(Color.WHITE);
        spinner.setFont(spinner.getFont().deriveFont(14f));
        spinner.setForeground(Color.BLACK);
    }

    private static Border createRoundedBorder() {
        int borderRadius = 10;
        Color lineColor = getLineColor();
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(lineColor),
                BorderFactory.createEmptyBorder(5, borderRadius, 5, borderRadius)
        );
    }




    private static Color getLineColor() {
        return Color.GRAY;
    }
}
