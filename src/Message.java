import java.time.LocalDate;
import java.time.LocalDateTime;

public class Message {
    private String message;
    private String sender;
    private LocalDateTime date;



    public  String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }


    public void setDate(LocalDateTime date){
        this.date = date;
    }
    @Override
    public String toString() {
        return sender + " - " + message + " - " + date;
    }


    public Message(String message, String sender, LocalDateTime date) {
        this.message = message;
        this.sender = sender;
        this.date = date;

    }
}
