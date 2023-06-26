import java.util.Date;

public class Event {
    private final int id;
    private final String name;
    private final String organizer;
    private final Date date;

    public Event(int id, String name, String organizer, Date date) {
        this.id = id;
        this.name = name;
        this.organizer = organizer;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOrganizer() {
        return organizer;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return name + " - " + organizer + " - " + date;
    }
}
