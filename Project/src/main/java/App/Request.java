package App;

public class Request {

    // Attributes
    private static int id;
    private String sender;
    private int receiver;
    private String comment;

    // __________________________________________________

    public static int getId() {
        return id;
    }

    // __________________________________________________

    public void setId(int id) {
        this.id = id;
    }

    // __________________________________________________

    public String getSender() {
        return this.sender;
    }

    // __________________________________________________

    public int getReceiver() {
        return this.receiver;
    }

    // __________________________________________________

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    // __________________________________________________

    public void setSender(String sender) {
        this.sender = sender;
    }

    // __________________________________________________

    public String getSenderName() {
        return this.sender;
    }

    // __________________________________________________

    public void setSenderName(String senderName) {
        this.sender = senderName;
    }

    // __________________________________________________

    public String getComment() {
        return comment;
    }

    // __________________________________________________

    public void setComment(String comment) {
        this.comment = comment;
    }

} // Class end