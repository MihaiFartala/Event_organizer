import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class startingPageForm {
    private JButton logoutButton;
    private JPanel buttonsPanel;
    private JPanel mainPanel;
    private JButton joinButton;
    private JButton calendarButton;
    private JButton eventsButton;
    private JButton profileButton;
    private JPanel profielPanel;


    public static void formShow(){
        JFrame frame = new JFrame("Organizer");
        frame.setContentPane( new startingPageForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1200,800);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }


}


