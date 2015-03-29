import java.util.Date;

public class Message {

    private String messageText;
    private String author;
    private String date;
    private boolean deleted = false;
    private double id;

    public Message(String messageText, String author) {
        this.messageText = messageText;
        this.author = author;
        Date currentDate = new Date();
        date = currentDate.toString();
        id = Math.random();
    }

    public Message(String messageText, String author, String date, double ID) {
        this.messageText = messageText;
        this.author = author;
        this.date = date;
        this.id = ID;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public boolean delete(){
        try {
            this.deleted = true;
            this.messageText = "Message was deleted by " + this.author + ".";
            this.author = "System";
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean edit(String newMessage){
        try{
            this.messageText = newMessage + " (Edited at " + new Date().toString() + ")";
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public String toString() {
        return "\"id\"" + ':' + '"' + id + '"'
                + ", " + "\"user\"" + ':' + '"' + author + '"'
                + ", " + "\"message\"" + ':' + '"' + messageText + '"'
                + ", " + "\"date\"" + ':' + '"' + date + '"';
    }

    public static Message fromString(String str){
        double tmpId = 0;
        String tmpDate = "";
        String tmpUser = "";
        String tmpMessage = "";
        int counter = 0;
        int firstPos = 0;
        int secondPos;
        boolean flag = false;
        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == '"'){
                counter++;
                if ((counter == 3 || counter == 7 || counter == 11 || counter == 15) && flag == false){
                    firstPos = i;
                    flag = true;
                }
            }
            if (counter == 4 && flag == true){
                secondPos = i;
                String tmp = str.substring(firstPos + 1, secondPos);
                tmpId = Double.parseDouble(str.substring(firstPos + 1, secondPos));
                flag = false;
            }
            if (counter == 8 && flag == true){
                secondPos = i;
                tmpUser = str.substring(firstPos + 1, secondPos);
                flag = false;
            }
            if (counter == 12 && flag == true){
                secondPos = i;
                tmpMessage = str.substring(firstPos + 1, secondPos);
                flag = false;
            }
            if (counter == 16 && flag == true){
                secondPos = i;
                tmpDate = str.substring(firstPos + 1, secondPos);
                flag = false;
            }
        }
        return new Message(tmpMessage, tmpUser, tmpDate, tmpId);
    }
}
