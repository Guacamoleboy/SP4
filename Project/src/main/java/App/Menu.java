// Packages
package App;

// Imports
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Menu extends Pane {

    // Attributes
    private int sceneWidth;
    private int sceneHeight;
    private Button cancelButton;
    private String titleText = "Welcome";
    private Label header;

    // ____________________________________________________

    public Menu(int sceneWidth, int sceneHeight) {

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        // Display setup
        this.setPrefWidth(sceneWidth);
        this.setPrefHeight(sceneHeight);

        this.getChildren().add(displayHeader());
    } // Constructor end

    // ____________________________________________________

    public VBox displayMyBookings() {

        VBox menu = new VBox(15); // Padding / Margin
        menu.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        menu.getStyleClass().add("booking-vbox");

        menu.setAlignment(Pos.CENTER);
        menu.setPrefWidth(200); // Card Width
        menu.setLayoutX(20);
        menu.setLayoutY(100);

        VBox topMenu = new VBox(15);
        topMenu.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        topMenu.getStyleClass().add("topMenu-vbox");

        //DETTE SKAL LAVES!
        //if (BOOKINGS != null) {
            //String stars = convertToStars(getRating());//getRating();
            Label dateLabel = new Label(getDate());
            Label timeLabel = new Label(getTime());
            Label placeLabel = new Label(getAdress());
            Label studentLabel = new Label(getStudentName());
            Label ratingLabel = new Label(convertToStars(getRating()));
            dateLabel.getStyleClass().add("card-text-header");
            timeLabel.getStyleClass().add("card-text");
            placeLabel.getStyleClass().add("card-text");
            studentLabel.getStyleClass().add("card-text");
            ratingLabel.getStyleClass().add("star");
            dateLabel.setWrapText(true);
            timeLabel.setWrapText(true);
            placeLabel.setWrapText(true);
            studentLabel.setWrapText(true);
            ratingLabel.setWrapText(true);
        //} else {
        // Label titleLabel = new Label("NO BOOKINGS!");
        //}

        // Button to Cancel Booking
        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("button-card");
        cancelButton.setPrefHeight(30);
        cancelButton.setPrefWidth(200/2); // Half the card width

        // Hover
        Animation.addHoverScaleEffect(cancelButton);

        cancelButton.setOnAction(e -> removeBooking());

        // Add our Display Card
        topMenu.getChildren().addAll(dateLabel);
        menu.getChildren().addAll(topMenu, timeLabel, placeLabel, studentLabel, ratingLabel, cancelButton);

        return menu;

    }

    // ____________________________________________________

    public VBox displayHeader(){

        VBox title = new VBox(15);
        title.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        header = new Label(titleText);
        header.setAlignment(Pos.CENTER);
        header.getStyleClass().add("header");

        title.setAlignment(Pos.CENTER);
        title.setPrefWidth(200); // Card Width
        title.setLayoutX(20);
        title.setLayoutY(19); // Minus border

        title.getChildren().addAll(header);

        return title;

    }

    // ____________________________________________________

    public String getDate() {
        return "25.07.2025";
    }

    // ____________________________________________________

    public String getTime() {
        return "15:30";
    }

    // ____________________________________________________

    public String getStudentName() {
        return "Jonas Munkedahl";
    }

    // ____________________________________________________

    public String getAdress() {
        return "Bytoften 21, 2650 Hvidovre";
    }

    // ____________________________________________________

    public double getRating() {
        return 4.5;
    }

    // ____________________________________________________

    public void removeBooking() {
        System.out.println("THIS IS TO REMOVE A BOOKING!");
    }

    // ____________________________________________________

    public void setHeaderTitle(String text) {

        this.titleText = text;

        if (header != null) {
            header.setText(text);
        }

    }

    // ____________________________________________________

    // MAKE WITH POLYGONS
    // Color.GOLD
    // Polygon.setFill

    public String convertToStars(double rating) {

        String message = "";
        int fullStars = (int) rating;
        boolean halfStar = (rating - fullStars) >= 0.5;
        int emptyStars = 6 - fullStars - (halfStar ? 1: 0);
        for (int i = 0; i < fullStars; i++) {
            message += "★";
        }

        for (int i = 0; i < emptyStars; i++) {
            message += "☆";
        }

        return message;

    }

} // Menu end