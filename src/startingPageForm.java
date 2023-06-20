import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class startingPageForm {
    private JButton logoutButton;
    private JPanel buttonsPanel;
    private JPanel spMainPanel;
    private JButton joinButton;
    private JButton calendarButton;
    private JButton eventsButton;
    private JButton profileButton;
    private JPanel profilePanel;
    private JPanel calendarPanel;
    private JButton profileButton1;
    private JButton joinButton1;
    private JPanel joinPanel;
    private JPanel eventsPanel;
    private JButton eventsButton1;
    private JLabel monthLabel;
    private JButton prevButton, nextButton;
    private JTable calendarTable;
    private JScrollPane calendarScroll;
    public static int month;
    public static int year;

    public startingPageForm(loggedUser loggedUser) {

        JFrame frame = new JFrame("Organizer");
        frame.setContentPane(spMainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1200,800);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        calendarButton.setFocusable(false);
        joinButton.setFocusable(false);
        eventsButton.setFocusable(false);
        profileButton.setFocusable(false);
        logoutButton.setFocusable(false);
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinPanel.setVisible(true);
                calendarPanel.setVisible(false);
                eventsPanel.setVisible(false);
                profilePanel.setVisible(false);
            }
        });
        calendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                month = java.time.MonthDay.now().getMonthValue(); // Set the initial month to the current month
                year = java.time.LocalDate.now().getYear(); // Set the initial year to the current year

                monthLabel.setText(java.time.Month.of(month).toString() + " " + year);

                CalendarTableModel tableModel = new CalendarTableModel(month, year);
//
//
                // Set the calendar table's properties
                calendarTable.setModel(tableModel);
                calendarTable.setRowHeight(48);
                calendarTable.setShowGrid(true);
                calendarTable.setGridColor(Color.BLACK);
                calendarTable.setDefaultRenderer(Object.class, new CalendarTableRenderer());


                joinPanel.setVisible(false);
                calendarPanel.setVisible(true);
                eventsPanel.setVisible(false);
                profilePanel.setVisible(false);

                // Add the listeners for the buttons
                prevButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        month--;
                        if (month == 0) {
                            month = 12;
                            year--;
                        }
                        monthLabel.setText(java.time.Month.of(month).toString() + " " + year);
                        calendarTable.setModel(new CalendarTableModel(month, year));
                    }
                });
                nextButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        month++;
                        if (month == 13) {
                            month = 1;
                            year++;
                        }
                        monthLabel.setText(java.time.Month.of(month).toString() + " " + year);
                        calendarTable.setModel(new CalendarTableModel(month, year));
                    }
                });

            }
        });
        eventsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinPanel.setVisible(false);
                calendarPanel.setVisible(false);
                eventsPanel.setVisible(true);
                profilePanel.setVisible(false);
            }
        });
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinPanel.setVisible(false);
                calendarPanel.setVisible(false);
                eventsPanel.setVisible(false);
                profilePanel.setVisible(true);
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                signUpForm form = new signUpForm();
            }
        });
    }
}


