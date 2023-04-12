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


public class signUpForm {
    private JButton signUpButton;
    JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JPasswordField confirmPasswordField;
    private JButton goToLoginButton;
    private JPanel SignUpPanel;
    private JPanel LoginPanel;
    private JButton goToSignUpButton;
    private JButton LoginButton;
    private JTextField loginUsername;
    private JPasswordField loginPassword;

    public signUpForm() {
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        goToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignUpPanel.setVisible(false);
                LoginPanel.setVisible(true);
            }
        });
        goToSignUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginPanel.setVisible(false);
                SignUpPanel.setVisible(true);
            }
        });
        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }

    private void register()
    {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());
        String confirmPassword = String.valueOf(confirmPasswordField.getPassword());

        if(checkData(username, email, password, confirmPassword)){
            try{
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer","root","");

                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO users (id, email, username, password) " +
                        "VALUES (null, ?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1,email);
                preparedStatement.setString(2,username);
                preparedStatement.setString(3,password);

                preparedStatement.execute();

                stmt.close();

            }catch(Exception e){
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(this.mainPanel, "New account created! Go to Login Page!","Success!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void formShow(){
        JFrame frame = new JFrame("App");
        frame.setContentPane( new signUpForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(430,400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private Boolean checkData(String user, String email, String pass, String cPass){
        if(checkUsernameValidity(user))
            if(checkUsernameDuplicity(user))
                if(checkPasswordValidity(pass)){
                    if(checkConfirmedPassword(pass,cPass))
                        if(checkEmailValidity(email)){
                            if(checkEmailDuplicity(email))
                                return true;
                        }
                }
        return false;
    }

    private Boolean checkUsernameValidity(String user){
        if(user.contains(" "))
        {
            JOptionPane.showMessageDialog(this.mainPanel, "The username can not contain spaces","Username error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if(user.length() > 15 || user.length() < 5)
        {
            JOptionPane.showMessageDialog(this.mainPanel, "Username must have between 5 and 15 characters!","Username error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(user.charAt(0) == Character.toLowerCase(user.charAt(0))){
            JOptionPane.showMessageDialog(this.mainPanel, "The first character of the username must be a CAPITAL letter!","Username error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        List<Character> specialCharacters = new ArrayList<>(List.of('!','@','#','$','%','^','&','*','(',')','[',']','{','}','<','>','?','"','\'','\\','|',':',';','.',',','/','~','`','=','+'));
        for(Character chr : specialCharacters){
            if(user.contains(String.valueOf(chr))){
                JOptionPane.showMessageDialog(this.mainPanel, "Username cannot contain special characters, except for: ' - ' , ' _ ' and the space character!","Username error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private Boolean checkUsernameDuplicity(String user){

        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer","root","");

            Statement stmt = conn.createStatement();
            String sql = "SELECT username from users " +
                         "WHERE username = '" + user + "'";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();


            while(rs.next()){
                JOptionPane.showMessageDialog(this.mainPanel, "Username already taken!","Username error", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }

            stmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return true;
    }

    private Boolean checkPasswordValidity(String password){
        if(password.contains(" "))
        {
            JOptionPane.showMessageDialog(this.mainPanel, "The password can not contain spaces","Password error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(password.length() < 8 || password.length() > 25){
            JOptionPane.showMessageDialog(this.mainPanel, "The password must have between 8 and 25 characters","Password error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else {
            Pattern lowercase = Pattern.compile("[a-z]");
            Pattern uppercase = Pattern.compile("[A-Z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasLowercase = lowercase.matcher(password);
            Matcher hasUppercase = uppercase.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            if (!(hasLowercase.find() && hasDigit.find() && hasSpecial.find() && hasUppercase.find())){
                JOptionPane.showMessageDialog(this.mainPanel, "The password must be a combination of uppercase letters, lowercase letters, numbers and symbols","Password error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private Boolean checkConfirmedPassword(String password, String confirm){
        if(!password.equals(confirm)){
            JOptionPane.showMessageDialog(this.mainPanel, "The confirmation password is not equal with the password","Confirmation password error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private Boolean checkEmailValidity(String email){

        if(email.contains(" "))
        {
            JOptionPane.showMessageDialog(this.mainPanel, "Please introduce a valid email address","Email error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
        {
            JOptionPane.showMessageDialog(this.mainPanel, "Please introduce a valid email address","Email error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if(pat.matcher(email).matches())
        {
            return true;
        }
        else
        {
            JOptionPane.showMessageDialog(this.mainPanel, "Please introduce a valid email address","Email error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

    }

    private Boolean checkEmailDuplicity(String email){

        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer","root","");

            Statement stmt = conn.createStatement();
            String sql = "SELECT email from users " +
                    "WHERE email = '" + email + "'";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();


            while(rs.next()){
                JOptionPane.showMessageDialog(this.mainPanel, "Email is already in use!","Email error", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }

            stmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return true;
    }

    private void login(){
        String username = loginUsername.getText();
        String password = String.valueOf(loginPassword.getPassword());
        if(checkUserAndPassword(username, password)){
            System.out.println("LOGGED IN");

        }
    }

    private boolean checkUserAndPassword(String username, String password){

        if(username.equals("") || password.equals(""))
        {
            JOptionPane.showMessageDialog(this.mainPanel, "Please complete all fields","No data", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(username.contains(" ") || password.contains(" "))
        {
            JOptionPane.showMessageDialog(this.mainPanel, "Username or password can not contain any spaces","Incorrect data", JOptionPane.WARNING_MESSAGE);
            return false;
        }


        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer","root","");

            Statement stmt = conn.createStatement();
            String sql = "SELECT username, password from users " +
                    "WHERE username = '" + username + "'";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();


            while(rs.next()){
                String user = rs.getString("username");
                String pass = rs.getString("password");

                if(user.equals(username) && pass.equals(password)){
                    return true;
                }
                else {
                    JOptionPane.showMessageDialog(this.mainPanel, "Incorrect username / password","Error!", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }

            stmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
}
