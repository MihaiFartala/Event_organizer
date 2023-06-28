import java.time.LocalDateTime;

public class Event {
    private final int id;
    private String name;
    private final String organizer;
    private LocalDateTime date;
    private String phone_number;
    private final int organizer_id;
    private final String join_code;


    public int getId() {
        return id;
    }

    public  String getName() {
        return name;
    }

    public String getOrganizer() {
        return organizer;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public int getOrganizer_id(){
        return organizer_id;
    }

    @Override
    public String toString() {
        return name + " - " + organizer + " - " + date;
    }

    public String getJoin_code(){
        return join_code;
    }

    public Event(int id, String name, String organizer, LocalDateTime date, String phone_number, int organizer_id, String joinCode) {
        this.id = id;
        this.name = name;
        this.organizer = organizer;
        this.date = date;
        this.phone_number = phone_number;
        this.organizer_id = organizer_id;
        this.join_code = joinCode;
    }
}
