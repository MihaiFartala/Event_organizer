public class loggedUser {

    private final String username;
    private final String password;
    private final String email;
    private final int id;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public loggedUser(String user, String pass, String email, int id) {
        this.username = user;
        this.password = pass;
        this.email = email;
        this.id = id;
    }
}
