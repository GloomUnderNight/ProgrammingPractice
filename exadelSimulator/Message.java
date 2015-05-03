import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Message {

    private String message;
    private String user;
    private String date;
    private double id;

    public Message(String message, String user) {
        this.message = message;
        this.user = user;
        date = getCurrentDate();
        id = Math.random();
    }

    public Message(String message, String user, String date, double ID) {
        this.message = message;
        this.user = user;
        this.date = date;
        this.id = ID;
    }

    public static String getCurrentDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Minsk"));
        return dateFormat.format(new Date());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public boolean delete(){
        try {
            this.message = "Message was deleted by " + this.user + " at " + getCurrentDate() + ".";
            this.user = "System";
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean edit(String newMessage){
        try{
            this.message = newMessage + " (Edited at " + getCurrentDate() + ")";
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":\"").append(id)
                .append("\", \"user\":\"").append(user)
                .append("\", \"message\":\"").append(message)
                .append("\", \"date\":\"").append(date).append("\"}");
        return sb.toString();
    }

}
