public class loggedUser {

    private final String username;
    private final String email;
    private final int id;
    private final int phone_number;

    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public loggedUser(String user, String email, int id, int phone_number) {
        this.username = user;
        this.email = email;
        this.id = id;
        this.phone_number = phone_number;
    }
}
