import java.util.Date;

public class Event {
    private final int id;
    private final String name;
    private final String organizer;
    private final Date date;
    private final String phone_number;
    private final int organizer_id;


    public int getId() {
        return id;
    }

    public  String getName() {
        return name;
    }

    public String getOrganizer() {
        return organizer;
    }

    public Date getDate() {
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


    public Event(int id, String name, String organizer, Date date, String phone_number, int organizer_id) {
        this.id = id;
        this.name = name;
        this.organizer = organizer;
        this.date = date;
        this.phone_number = phone_number;
        this.organizer_id = organizer_id;
    }
}
