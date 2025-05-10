// Packages
package App;

// Imports
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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

    public VBox displayMyMessages() {

        // VBox
        VBox messageVBox = new VBox();
        messageVBox.setLayoutX(20);
        messageVBox.setLayoutY(100);
        messageVBox.setPrefWidth(760);
        messageVBox.setPrefHeight(480);
        messageVBox.setStyle("-fx-border-color: #464646; -fx-border-width: 2px; -fx-border-radius: 20px; -fx-background-radius: 20px;");

        // HBox
        HBox messageHBox = new HBox();
        messageHBox.getStyleClass().add("message-vbox");
        messageHBox.setPrefWidth(760);
        messageHBox.setPrefHeight(480);
        messageHBox.setAlignment(Pos.TOP_LEFT);
        messageHBox.setSpacing(0);

        // Sidebar
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(760 * 0.26);
        sidebar.setAlignment(Pos.TOP_LEFT);
        sidebar.setPadding(Insets.EMPTY);
        sidebar.setStyle("-fx-background-color: #696969; -fx-border-radius: 20 0 0 20; -fx-background-radius: 20 0 0 20;"); // Transparent to blend

        // Buttons
        Button user1 = new Button("Jonas");
        Button user2 = new Button("Andreas");
        Button user3 = new Button("Ebou");
        Button user4 = new Button("Carl-Emil");

        sidebar.getChildren().addAll(user1, user2, user3, user4);
        user1.getStyleClass().addAll("user-button", "user-button1");
        user2.getStyleClass().add("user-button");
        user3.getStyleClass().add("user-button");
        user4.getStyleClass().add("user-button");

        // Message area (4/5)
        VBox messageArea = new VBox(15);
        messageArea.setPrefWidth(760 * 0.74);
        messageArea.setPadding(new Insets(20));
        messageArea.setAlignment(Pos.TOP_LEFT);
        messageArea.setStyle("-fx-background-color: transparent;");

        // Add to HBox (Left -> Right)
        messageHBox.getChildren().addAll(sidebar, messageArea);
        messageVBox.getChildren().add(messageHBox);

        // Actions
        user1.setOnAction(e -> {
            messageArea.getChildren().clear();
        });
        user2.setOnAction(e -> {
            messageArea.getChildren().clear();
        });
        user3.setOnAction(e -> {
            messageArea.getChildren().clear();
        });
        user4.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        return messageVBox;
    }

    // ____________________________________________________

    public VBox displaySettings() {

        // VBox
        VBox settingsVBox = new VBox();
        settingsVBox.setLayoutX(20);
        settingsVBox.setLayoutY(100);
        settingsVBox.setPrefWidth(760);
        settingsVBox.setPrefHeight(480);
        settingsVBox.setStyle("-fx-border-color: #464646; -fx-border-width: 2px; -fx-border-radius: 20px; -fx-background-radius: 20px;");

        // HBox
        HBox messageHBox = new HBox();
        messageHBox.getStyleClass().add("message-vbox");
        messageHBox.setPrefWidth(760);
        messageHBox.setPrefHeight(480);
        messageHBox.setAlignment(Pos.TOP_LEFT);
        messageHBox.setSpacing(0);

        // Sidebar
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(760 * 0.26);
        sidebar.setAlignment(Pos.TOP_LEFT);
        sidebar.setPadding(Insets.EMPTY);
        sidebar.setStyle("-fx-background-color: #696969; -fx-border-radius: 20 0 0 20; -fx-background-radius: 20 0 0 20;"); // Transparent to blend

        // Buttons
        Button setting1 = new Button("Darkmode");
        Button setting2 = new Button("Sensitivity");
        Button setting3 = new Button("Nigga");
        Button setting4 = new Button("Delete Account");

        sidebar.getChildren().addAll(setting1, setting2, setting3, setting4);
        setting1.getStyleClass().addAll("user-button", "user-button1");
        setting2.getStyleClass().add("user-button");
        setting3.getStyleClass().add("user-button");
        setting4.getStyleClass().add("user-button");

        // Message area (4/5)
        VBox messageArea = new VBox(15);
        messageArea.setPrefWidth(760 * 0.74);
        messageArea.setPadding(new Insets(20));
        messageArea.setAlignment(Pos.TOP_LEFT);
        messageArea.setStyle("-fx-background-color: transparent;");

        // Add to HBox (Left -> Right)
        messageHBox.getChildren().addAll(sidebar, messageArea);
        settingsVBox.getChildren().add(messageHBox);

        // Actions
        setting1.setOnAction(e -> {
            messageArea.getChildren().clear();
        });
        setting2.setOnAction(e -> {
            messageArea.getChildren().clear();
        });
        setting3.setOnAction(e -> {
            messageArea.getChildren().clear();
        });
        setting4.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        return settingsVBox;
    }


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
        title.setPrefWidth(200);
        title.setLayoutX(20);
        title.setLayoutY(20);

        title.getChildren().addAll(header);

        return title;

    }

    // ____________________________________________________

    public VBox displayComboBoxCity() {

        VBox comboBoxVBox = new VBox(15);
        comboBoxVBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Hellerup", "Lyngby", "Gentofte", "Hillerød", "Nigga");
        comboBox.setValue("Choose City");

        comboBox.getStyleClass().add("combo-box-book");

        comboBoxVBox.setAlignment(Pos.CENTER);
        comboBoxVBox.setPrefWidth(200);
        comboBoxVBox.setLayoutX(240);
        comboBoxVBox.setLayoutY(20);

        comboBoxVBox.getChildren().addAll(comboBox);

        return comboBoxVBox;
    }

    // ____________________________________________________

    public VBox displayComboBoxDate() {

        VBox comboBoxVBox = new VBox(15);
        comboBoxVBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("05/05", "06/05", "07/05", "08/05", "09/05");
        comboBox.setValue("Choose date");

        comboBox.getStyleClass().add("combo-box-book");

        comboBoxVBox.setAlignment(Pos.CENTER);
        comboBoxVBox.setPrefWidth(200);
        comboBoxVBox.setLayoutX(460);
        comboBoxVBox.setLayoutY(20);

        comboBoxVBox.getChildren().addAll(comboBox);

        return comboBoxVBox;
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