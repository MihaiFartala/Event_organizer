import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class signUpForm {
    private JButton signUpButton;
    public JPanel mainPanel;
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

    loggedUser loggedUser;

    public signUpForm() {

        JFrame frame = new JFrame("Event organizer");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(430,400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        loginUsername.requestFocusInWindow();
        frame.setVisible(true);

        ButtonsStyle.applyButtonStyles(signUpButton);
        ButtonsStyle.applyButtonStyles(goToSignUpButton);
        ButtonsStyle.applyButtonStyles(goToLoginButton);
        ButtonsStyle.applyButtonStyles(LoginButton);

        FieldsStyle.applyStyle(loginUsername);
        FieldsStyle.applyStyle(loginPassword);
        FieldsStyle.applyStyle(usernameField);
        FieldsStyle.applyStyle(emailField);
        FieldsStyle.applyStyle(passwordField);
        FieldsStyle.applyStyle(confirmPasswordField);


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
                loginUsername.setText("");
                loginPassword.setText("");
                loginUsername.requestFocusInWindow();
            }
        });
        goToSignUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginPanel.setVisible(false);
                SignUpPanel.setVisible(true);
                usernameField.setText("");
                emailField.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");
                usernameField.requestFocusInWindow();
            }
        });
        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(login()){
                    frame.dispose();
                    startingPageForm startingPage = new startingPageForm(loggedUser); //loggedUser
                }
            }
        });


        // Add ActionListener to input fields for handling Enter key press for signup form
        KeyListener enterKeyListenerSignUp = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    signUpButton.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };

        usernameField.addKeyListener(enterKeyListenerSignUp);
        emailField.addKeyListener(enterKeyListenerSignUp);
        passwordField.addKeyListener(enterKeyListenerSignUp);
        confirmPasswordField.addKeyListener(enterKeyListenerSignUp);


        KeyListener enterKeyListenerLogin = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    LoginButton.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };

        loginUsername.addKeyListener(enterKeyListenerLogin);
        loginPassword.addKeyListener(enterKeyListenerLogin);

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
                preparedStatement.setString(3,PasswordHash.hashPassword(password));

                preparedStatement.execute();

                stmt.close();

            }catch(Exception e){
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(this.mainPanel, "New account successfully created!","Success!", JOptionPane.INFORMATION_MESSAGE);
            usernameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            usernameField.requestFocus();

            goToLoginButton.doClick();
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private Boolean checkData(String user, String email, String pass, String cPass){

        if(user.isEmpty() || email.isEmpty() || pass.isEmpty() || cPass.isEmpty()){
            JOptionPane.showMessageDialog(this.mainPanel, "Please complete all fields!","Empty fields!", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if(checkUsernameValidity(user))
            if(checkUsernameDuplicity(user))
                if(checkPasswordValidity(pass)){
                    if(checkConfirmedPassword(pass,cPass))
                        if(checkEmailValidity(email)){
                            return checkEmailDuplicity(email);
                        }
                }
        return false;
    }

    private Boolean checkUsernameValidity(String user){

//        if(user.contains(" "))
//        {
//            JOptionPane.showMessageDialog(this.mainPanel, "The username can not contain spaces","Username error", JOptionPane.WARNING_MESSAGE);
//            usernameField.requestFocusInWindow();
//            return false;
//        }

        if(user.length() > 15 || user.length() < 5)
        {
            JOptionPane.showMessageDialog(this.mainPanel, "Username must have between 5 and 15 characters!","Username error", JOptionPane.WARNING_MESSAGE);
            usernameField.requestFocusInWindow();
            return false;
        }
        if(user.charAt(0) == Character.toLowerCase(user.charAt(0))){
            JOptionPane.showMessageDialog(this.mainPanel, "The first character of the username must be a CAPITAL letter!","Username error", JOptionPane.WARNING_MESSAGE);
            usernameField.requestFocusInWindow();
            return false;
        }
        List<Character> specialCharacters = new ArrayList<>(List.of('!','@','#','$','%','^','&','*','(',')','[',']','{','}','<','>','?','"','\'','\\','|',':',';','.',',','/','~','`','=','+'));
        for(Character chr : specialCharacters){
            if(user.contains(String.valueOf(chr))){
                JOptionPane.showMessageDialog(this.mainPanel, "Username cannot contain special characters, except for: ' - ' , ' _ ' and the space character!","Username error", JOptionPane.WARNING_MESSAGE);
                usernameField.requestFocusInWindow();
                return false;
            }
        }
        return true;
    }

    private Boolean checkUsernameDuplicity(String user){

        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventorganizer","root","");

            Statement stmt = conn.createStatement();
            String sql = "SELECT username from users WHERE username = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,user);

            ResultSet rs = preparedStatement.executeQuery();


            if(rs.next()){
                JOptionPane.showMessageDialog(this.mainPanel, "Username already taken!","Username error", JOptionPane.INFORMATION_MESSAGE);
                usernameField.setText("");
                usernameField.requestFocus();
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
            passwordField.requestFocusInWindow();
            return false;
        }
        if(password.length() < 8 || password.length() > 25){
            JOptionPane.showMessageDialog(this.mainPanel, "The password must have between 8 and 25 characters","Password error", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocusInWindow();
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
                passwordField.requestFocusInWindow();
                return false;
            }
        }
        return true;
    }

    private Boolean checkConfirmedPassword(String password, String confirm){
        if(!password.equals(confirm)){
            JOptionPane.showMessageDialog(this.mainPanel, "The confirmation password is not equal with the password","Confirmation password error", JOptionPane.WARNING_MESSAGE);
            confirmPasswordField.requestFocusInWindow();
            return false;
        }
        return true;
    }

    private Boolean checkEmailValidity(String email){

        if(email.contains(" "))
        {
            JOptionPane.showMessageDialog(this.mainPanel, "Please introduce a valid email address","Email error", JOptionPane.WARNING_MESSAGE);
            emailField.requestFocusInWindow();
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
            emailField.requestFocusInWindow();
            return false;
        }

        if(pat.matcher(email).matches())
        {
            return true;
        }
        else
        {
            JOptionPane.showMessageDialog(this.mainPanel, "Please introduce a valid email address","Email error", JOptionPane.WARNING_MESSAGE);
            emailField.requestFocusInWindow();
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


            if(rs.next()){
                JOptionPane.showMessageDialog(this.mainPanel, "Email is already in use!","Email error", JOptionPane.INFORMATION_MESSAGE);
                emailField.requestFocusInWindow();
                emailField.setText("");
                emailField.requestFocus();
                return false;
            }

            stmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return true;
    }

    private boolean login(){
        String username = loginUsername.getText();
        String password = String.valueOf(loginPassword.getPassword());
        return checkUserAndPassword(username, password);
    }

    private void close(){
        mainPanel.setVisible(false);
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
            String sql = "SELECT * from users " +
                    "WHERE username = '" + username + "'";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();


            if(rs.next()){
                String user = rs.getString("username");
                String pass = rs.getString("password");

                if(user.equals(username) && pass.equals(PasswordHash.hashPassword(password))){
                    String email = rs.getString("email");
                    int id  = rs.getInt("id");
                    loggedUser = new loggedUser(user, pass, email, id);
                    return true;
                }
                else {
                    JOptionPane.showMessageDialog(this.mainPanel, "Incorrect username / password","Error!", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }


            stmt.close();

            JOptionPane.showMessageDialog(this.mainPanel, "Incorrect username / password","Error!", JOptionPane.WARNING_MESSAGE);
            return false;
        }catch(Exception e){
            e.printStackTrace();

        }

        return false;
    }
}
