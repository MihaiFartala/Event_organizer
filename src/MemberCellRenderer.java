import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class MemberCellRenderer extends JPanel implements ListCellRenderer<EventMember> {
    private final JLabel nameLabel;
    private final JLabel emailLabel;
    private final JLabel phoneNumberLabel;
    private final JLabel relationLabel;

    public MemberCellRenderer() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel textPanel = new JPanel(new GridLayout(4, 1));
        Border matteBorder = new MatteBorder(0, 0, 1, 0, Color.BLACK);
        Border emptyBorder = new EmptyBorder(5, 10, 5, 10);
        Border compoundBorder = BorderFactory.createCompoundBorder(matteBorder, emptyBorder);
        textPanel.setBorder(compoundBorder);
        textPanel.setOpaque(false);

        nameLabel = new JLabel();
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));
        textPanel.add(nameLabel);

        emailLabel = new JLabel();
        emailLabel.setFont(emailLabel.getFont().deriveFont(Font.PLAIN, 12f));
        textPanel.add(emailLabel);

        phoneNumberLabel = new JLabel();
        phoneNumberLabel.setFont(phoneNumberLabel.getFont().deriveFont(Font.PLAIN, 12f));
        textPanel.add(phoneNumberLabel);

        relationLabel = new JLabel();
        relationLabel.setFont(relationLabel.getFont().deriveFont(Font.PLAIN, 12f));
        textPanel.add(relationLabel);

        add(textPanel, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends EventMember> list, EventMember value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        nameLabel.setText("<html><b>Name:</b> " + value.getName() + "</html>");
        emailLabel.setText("<html><b>Email:</b> " + value.getEmail() + "</html>");

        if (value.getPhoneNumber() != null) {
            phoneNumberLabel.setText("<html><b>Phone:</b> " + value.getPhoneNumber() + "</html>");
        } else {
            phoneNumberLabel.setText("<html><b>Phone: -</b> ");
        }

        if (value.getRelation() == null || value.getRelation().isEmpty()) {
            relationLabel.setText("<html><b>Role: -</b> ");
        } else {
            relationLabel.setText("<html><b>Role:</b> " + value.getRelation() + "</html>");
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
