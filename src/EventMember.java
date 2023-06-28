public class EventMember {
    private String name;
    private String email;
    private String phoneNumber;
    private String relation;
    private int id;

    public EventMember(int id, String name, String email, String phoneNumber, String relation) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.relation = relation;
    }

    // Getters and setters for the member fields

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getId() {
        return id;
    }
}
