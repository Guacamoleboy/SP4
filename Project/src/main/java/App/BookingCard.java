package App;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookingCard  {

    private LocalDate date;
    private String time;
    private String address;
    private int review;
    private int hairtype_id;
    private boolean exam; //false no, true yes
    private int student_id;

    public BookingCard(LocalDate date, String time, String address, int hairtype_id, boolean exam, int student_id) {
        this.student_id = student_id;
        this.date = date;
        this.time = time;
        this.address = address;
        this.review = review;
        this.hairtype_id = hairtype_id;
        this.exam = exam;


    }

    public void createBooking() {
        Main.db.createBooking(date, time, address, hairtype_id, exam, student_id);
    }

    //getters
    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
        return date.format(formatter);
    }

    public String getTime() { return time; }
    public String getAddress() { return address; }
    public int getHairtypeId() { return hairtype_id; }
    public int getReview() { return review; }


}
