package App;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookingCard  {

    // Attributes
    private LocalDate date;
    private String time;
    private String address;
    private int review;
    private int hairtype_id;
    private boolean exam;
    private int student_id;
    private String paid;
    private String accepted;

    // ____________________________________________________

    public BookingCard(LocalDate date, String time, String address, int hairtype_id, boolean exam, int student_id) {
        this.student_id = student_id;
        this.date = date;
        this.time = time;
        this.address = address;
        this.review = review;
        this.hairtype_id = hairtype_id;
        this.exam = exam;

    }

    // ____________________________________________________

    public void createBooking() {

        // Default for now
        String paid = "No";
        String accepted = "No";

        Main.db.createBooking(date, time, address, hairtype_id, exam, student_id, paid, accepted);

    }

    // ____________________________________________________

    //getters
    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
        return date.format(formatter);
    }

    // ____________________________________________________

    public String getTime() { return time; }

    // ____________________________________________________

    public String getAddress() { return address; }

    // ____________________________________________________

    public int getHairtypeId() { return hairtype_id; }

    // ____________________________________________________

    public int getReview() { return review; }

} // Class end