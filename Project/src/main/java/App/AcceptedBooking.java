package App;

public class AcceptedBooking {

    // Attributes
    private int acceptedBookingId;
    private int bookingId;
    private String comments;
    private String customer;
    private int student;
    private String planned;
    private String date;
    private String time;

    // _________________________________

    public AcceptedBooking(int acceptedBookingId, int bookingId, String comments, String customer, int student, String planned, String date, String time) {
        this.acceptedBookingId = acceptedBookingId;
        this.bookingId = bookingId;
        this.comments = comments;
        this.customer = customer;
        this.student = student;
        this.planned = planned;
        this.date = date;
        this.time = time;
    }

    // _________________________________

    public int getAcceptedBookingId() {
        return acceptedBookingId;
    }

    // _________________________________

    public int getBookingId() {
        return bookingId;
    }

    // _________________________________

    public String getComments() {
        return comments;
    }

    // _________________________________

    public String getCustomer() {
        return customer;
    }

    // _________________________________

    public int getStudent() {
        return student;
    }

    // _________________________________

    public String getPlanned() {
        return planned;
    }

    // _________________________________

    public String getDate() {
        return date;
    }

    // _________________________________

    public String getTime() {
        return time;
    }
}