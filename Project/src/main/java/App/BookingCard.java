package App;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookingCard  {

    // Attributes
    private String date;
    private String time;
    private String address;
    private double review;
    private int hairtype_id;
    private boolean exam;
    public static int student_id;
    private String paid;
    private String accepted;
    private String customer;

    // ____________________________________________________

    public BookingCard(String date, String time, String address, int hairtype_id, boolean exam, int student_id, String paid, String accepted, String customer) {
        BookingCard.student_id = student_id;
        this.date = date;
        this.time = time;
        this.address = address;
        this.hairtype_id = hairtype_id;
        this.exam = exam;
        this.paid = paid;
        this.accepted = accepted;
        this.customer = customer;

        String username = Main.db.getUserName(student_id);

        Profile user = new Profile(username);

        this.review = user.getRating();

    }

    // ____________________________________________________

    public void createBooking() {

        // Default for now
        String paid = "No";
        String accepted = "No";

        Main.db.createBooking(date, time, address, hairtype_id, exam, student_id, paid, accepted, customer);

    }

    // ____________________________________________________

    //getters
    public String getDate() {
        return this.date;
    }

    // ____________________________________________________

    public String getTime() {
        return time;
    }

    // ____________________________________________________

    public String getAddress() {
        return address;
    }

    // ____________________________________________________

    public int getHairtypeId() {
        return hairtype_id;
    }

    // ____________________________________________________

    public double getReview() {
        return this.review;
    }

    // ____________________________________________________

    public String getPaid() {
        return this.paid;
    }

    // ____________________________________________________

    public String getAccepted() {
        return this.accepted;
    }

    // ____________________________________________________

    public static int getStudentID() {
        return student_id;
    }

    // ____________________________________________________

    public String getCustomer() {
        return this.customer;
    }

} // Class end