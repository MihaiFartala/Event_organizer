public class loggedUser {

    private String username;
    private String email;
    private final int id;
    private String phone_number;

    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public String getPhoneNumber(){
        return phone_number;
    }

    public void setPhoneNumber(String phone){
        this.phone_number = phone;
    }

    public void setUsername(String Username){
        this.username = Username;
    }

    public void setEmail(String Email){
        this.email = Email;
    }

    public loggedUser(String user, String email, int id, String phone_number) {
        this.username = user;
        this.email = email;
        this.id = id;
        this.phone_number = phone_number;
    }
}
