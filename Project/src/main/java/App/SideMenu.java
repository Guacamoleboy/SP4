// Packages
package App;

// Imports
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;

public class SideMenu extends Pane {

    // Attributes
    private int sceneWidth;
    private int sceneHeight;
    private Menu menu;
    private String role;

    // ____________________________________________________

    public SideMenu(int sceneWidth, int sceneHeight, Menu menu, String role) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.menu = menu;
        this.role = role;

        // Display setup
        this.setPrefWidth(sceneWidth);
        this.setPrefHeight(sceneHeight);

        // Create
        openMenuType(this.role);

    }

    // ____________________________________________________

    public BorderPane displayCustomer() {

        BorderPane layout = new BorderPane();
        layout.setPrefWidth(sceneWidth);
        layout.setPrefHeight(sceneHeight);

        VBox topBox = new VBox();
        topBox.setSpacing(20);
        // Change to CSS at some point
        topBox.setStyle("-fx-alignment: top-center;");
        topBox.setPadding(new javafx.geometry.Insets(15, 0, 0, 0)); // top, right, bottom, left

        // Top Buttons Creating
        Button profileBtn = imageButton("/assets/profile/person1.png", 50, 50, 60);
        Button btn1 = imageButton("/assets/icons/icon1.png", 30, 30, 50);
        Button btn2 = imageButton("/assets/icons/icon2.png", 30, 30, 50);
        Button btn3 = imageButton("/assets/icons/icon3.png", 30, 30, 50);
        Button btn4 = imageButton("/assets/icons/icon5.png", 30, 30, 50);

        // Top VBox addALl
        topBox.getChildren().addAll(profileBtn, btn1, btn2, btn3, btn4);

        // New VBox for Settings to be at the bottom
        VBox bottomBox = new VBox();
        bottomBox.setStyle("-fx-alignment: bottom-center;");
        bottomBox.setPadding(new javafx.geometry.Insets(0, 0, 20, 0)); // top, right, bottom, left

        // The setting button
        Button settingsBtn = imageButton("/assets/icons/icon4.png", 30, 30, 50);
        bottomBox.getChildren().add(settingsBtn);

        // Actions
        btn1.setOnAction(e -> {
            menu.setHeaderTitle("Book");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
            menu.getChildren().add(menu.displayExamHeader("Exam"));
            menu.getChildren().add(menu.displayComboBox());
            menu.getChildren().add(menu.displayAvailableBookings());
            menu.getChildren().add(menu.displayExamBookings());
        });

        btn2.setOnAction(e -> {
            menu.setHeaderTitle("Messages");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
            menu.getChildren().add(menu.displayMyMessages());
        });

        btn3.setOnAction(e -> {
            menu.setHeaderTitle("My Bookings");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
            menu.getChildren().add(menu.displayMyBookings());
        });

        btn4.setOnAction(e -> {
            menu.setHeaderTitle("Favorites");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        settingsBtn.setOnAction(e -> {
            menu.setHeaderTitle("Settings");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
            menu.getChildren().add(menu.displaySettings());
        });

        profileBtn.setOnAction(e -> {
            menu.setHeaderTitle("Profile");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        // Hover
        Animation.addHoverScaleEffectMore(btn1);
        Animation.addHoverScaleEffectMore(btn2);
        Animation.addHoverScaleEffectMore(btn3);
        Animation.addHoverScaleEffectMore(btn4);
        Animation.addHoverScaleEffectMore(profileBtn);
        Animation.addHoverScaleEffectMore(settingsBtn);

        // Add VBox
        layout.setTop(topBox);
        layout.setBottom(bottomBox);

        return layout;

    }

    // ____________________________________________________

    public BorderPane displayStudent() {

        BorderPane layout = new BorderPane();
        layout.setPrefWidth(sceneWidth);
        layout.setPrefHeight(sceneHeight);

        VBox topBox = new VBox();
        topBox.setSpacing(20);
        // Change to CSS at some point
        topBox.setStyle("-fx-alignment: top-center;");
        topBox.setPadding(new javafx.geometry.Insets(15, 0, 0, 0)); // top, right, bottom, left

        // Top Buttons Creating
        Button profileBtn = imageButton("/assets/profile/person1.png", 50, 50, 60);
        Button btn1 = imageButton("/assets/icons/icon8.png", 30, 30, 50); // Current Bookings
        Button btn2 = imageButton("/assets/icons/icon1.png", 30, 30, 50); // Available Bookings
        Button btn3 = imageButton("/assets/icons/icon2.png", 30, 30, 50); // Messages
        Button btn4 = imageButton("/assets/icons/icon9.png", 30, 30, 50); // Exam

        // Top VBox addALl
        topBox.getChildren().addAll(profileBtn, btn1, btn2, btn3, btn4);

        // New VBox for Settings to be at the bottom
        VBox bottomBox = new VBox();
        bottomBox.setStyle("-fx-alignment: bottom-center;");
        bottomBox.setPadding(new javafx.geometry.Insets(0, 0, 20, 0)); // top, right, bottom, left

        // The setting button
        Button settingsBtn = imageButton("/assets/icons/icon4.png", 30, 30, 50);
        bottomBox.getChildren().add(settingsBtn);

        // Actions
        btn1.setOnAction(e -> {
            menu.setHeaderTitle("Book");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
            menu.getChildren().add(menu.displayExamHeader("Exam"));
            menu.getChildren().add(menu.displayComboBox());
            menu.getChildren().add(menu.displayAvailableBookings());
            menu.getChildren().add(menu.displayExamBookings());
        });

        settingsBtn.setOnAction(e -> {
            menu.setHeaderTitle("Settings");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
            menu.getChildren().add(menu.displaySettings());
        });

        profileBtn.setOnAction(e -> {
            menu.setHeaderTitle("Profile");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        // Hover
        Animation.addHoverScaleEffectMore(btn1);
        Animation.addHoverScaleEffectMore(profileBtn);
        Animation.addHoverScaleEffectMore(settingsBtn);

        // Add VBox
        layout.setTop(topBox);
        layout.setBottom(bottomBox);

        return layout;

    }

    // ____________________________________________________

    public BorderPane displaySupport() {

        BorderPane layout = new BorderPane();
        layout.setPrefWidth(sceneWidth);
        layout.setPrefHeight(sceneHeight);

        VBox topBox = new VBox();
        topBox.setSpacing(20);
        // Change to CSS at some point
        topBox.setStyle("-fx-alignment: top-center;");
        topBox.setPadding(new javafx.geometry.Insets(15, 0, 0, 0)); // top, right, bottom, left

        // Top Buttons Creating
        Button profileBtn = imageButton("/assets/profile/person1.png", 50, 50, 60);
        Button btn1 = imageButton("/assets/icons/icon6.png", 30, 30, 50); // SUPPORT TICKETS
        Button btn2 = imageButton("/assets/icons/icon7.png", 30, 30, 50); // ADMIN
        Button btn3 = imageButton("/assets/icons/icon2.png", 30, 30, 50); // Messages

        // Top VBox addALl
        topBox.getChildren().addAll(profileBtn, btn1, btn2, btn3);

        // New VBox for Settings to be at the bottom
        VBox bottomBox = new VBox();
        bottomBox.setStyle("-fx-alignment: bottom-center;");
        bottomBox.setPadding(new javafx.geometry.Insets(0, 0, 20, 0)); // top, right, bottom, left

        // The setting button
        Button settingsBtn = imageButton("/assets/icons/icon4.png", 30, 30, 50);
        bottomBox.getChildren().add(settingsBtn);

        // Actions
        btn1.setOnAction(e -> {
            menu.setHeaderTitle("Tickets");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        btn2.setOnAction(e -> {
            menu.setHeaderTitle("Admin");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        btn3.setOnAction(e -> {
            menu.setHeaderTitle("Messages");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        settingsBtn.setOnAction(e -> {
            menu.setHeaderTitle("Settings");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
            menu.getChildren().add(menu.displaySettings());
        });

        profileBtn.setOnAction(e -> {
            menu.setHeaderTitle("Profile");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        // Hover
        Animation.addHoverScaleEffectMore(btn1);
        Animation.addHoverScaleEffectMore(btn2);
        Animation.addHoverScaleEffectMore(btn3);
        Animation.addHoverScaleEffectMore(profileBtn);
        Animation.addHoverScaleEffectMore(settingsBtn);

        // Add VBox
        layout.setTop(topBox);
        layout.setBottom(bottomBox);

        return layout;

    }

    // ____________________________________________________

    public BorderPane displayTeacher() {

        BorderPane layout = new BorderPane();
        layout.setPrefWidth(sceneWidth);
        layout.setPrefHeight(sceneHeight);

        VBox topBox = new VBox();
        topBox.setSpacing(20);
        // Change to CSS at some point
        topBox.setStyle("-fx-alignment: top-center;");
        topBox.setPadding(new javafx.geometry.Insets(15, 0, 0, 0)); // top, right, bottom, left

        // Top Buttons Creating
        Button profileBtn = imageButton("/assets/profile/person1.png", 50, 50, 60);
        Button btn1 = imageButton("/assets/icons/icon7.png", 30, 30, 50); // ADMIN AF ELEVER
        Button btn2 = imageButton("/assets/icons/icon5.png", 30, 30, 50); // RATING AF EKSAMENS BOOKING
        Button btn3 = imageButton("/assets/icons/icon1.png", 30, 30, 50); // OPRET TIDER FOR ELEVER (?)
        Button btn4 = imageButton("/assets/icons/icon2.png", 30, 30, 50); // BESKEDER

        // Top VBox addALl
        topBox.getChildren().addAll(profileBtn, btn1, btn2, btn3, btn4);

        // New VBox for Settings to be at the bottom
        VBox bottomBox = new VBox();
        bottomBox.setStyle("-fx-alignment: bottom-center;");
        bottomBox.setPadding(new javafx.geometry.Insets(0, 0, 20, 0)); // top, right, bottom, left

        // The setting button
        Button settingsBtn = imageButton("/assets/icons/icon4.png", 30, 30, 50);
        bottomBox.getChildren().add(settingsBtn);

        // Actions
        btn1.setOnAction(e -> {
            menu.setHeaderTitle("Admin");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        btn2.setOnAction(e -> {
            menu.setHeaderTitle("Rating");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        btn3.setOnAction(e -> {
            menu.setHeaderTitle("Booking");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        btn4.setOnAction(e -> {
            menu.setHeaderTitle("Beskeder");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        settingsBtn.setOnAction(e -> {
            menu.setHeaderTitle("Settings");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
            menu.getChildren().add(menu.displaySettings());
        });

        profileBtn.setOnAction(e -> {
            menu.setHeaderTitle("Profile");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        // Hover
        Animation.addHoverScaleEffectMore(btn1);
        Animation.addHoverScaleEffectMore(btn2);
        Animation.addHoverScaleEffectMore(btn3);
        Animation.addHoverScaleEffectMore(btn4);
        Animation.addHoverScaleEffectMore(profileBtn);
        Animation.addHoverScaleEffectMore(settingsBtn);

        // Add VBox
        layout.setTop(topBox);
        layout.setBottom(bottomBox);

        return layout;

    }

    // ____________________________________________________

    public BorderPane displaySchool() {

        BorderPane layout = new BorderPane();
        layout.setPrefWidth(sceneWidth);
        layout.setPrefHeight(sceneHeight);

        VBox topBox = new VBox();
        topBox.setSpacing(20);
        // Change to CSS at some point
        topBox.setStyle("-fx-alignment: top-center;");
        topBox.setPadding(new javafx.geometry.Insets(15, 0, 0, 0)); // top, right, bottom, left

        // Top Buttons Creating
        Button profileBtn = imageButton("/assets/profile/person1.png", 50, 50, 60);
        Button btn1 = imageButton("/assets/icons/icon7.png", 30, 30, 50); // ADMIN
        Button btn2 = imageButton("/assets/icons/icon2.png", 30, 30, 50); // MESSAGE

        // Top VBox addALl
        topBox.getChildren().addAll(profileBtn, btn1, btn2);

        // New VBox for Settings to be at the bottom
        VBox bottomBox = new VBox();
        bottomBox.setStyle("-fx-alignment: bottom-center;");
        bottomBox.setPadding(new javafx.geometry.Insets(0, 0, 20, 0)); // top, right, bottom, left

        // The setting button
        Button settingsBtn = imageButton("/assets/icons/icon4.png", 30, 30, 50);
        bottomBox.getChildren().add(settingsBtn);

        // Actions
        btn1.setOnAction(e -> {
            menu.setHeaderTitle("Admin");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        btn2.setOnAction(e -> {
            menu.setHeaderTitle("Messages");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        settingsBtn.setOnAction(e -> {
            menu.setHeaderTitle("Settings");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
            menu.getChildren().add(menu.displaySettings());
        });

        profileBtn.setOnAction(e -> {
            menu.setHeaderTitle("Profile");
            menu.getChildren().clear();
            menu.getChildren().add(menu.displayHeader());
        });

        // Hover
        Animation.addHoverScaleEffectMore(btn1);
        Animation.addHoverScaleEffectMore(btn2);
        Animation.addHoverScaleEffectMore(settingsBtn);

        // Add VBox
        layout.setTop(topBox);
        layout.setBottom(bottomBox);

        return layout;

    }

    // ____________________________________________________

    private Button imageButton(String imagePath, int iconWidth, int iconHeight, int buttonSize) {

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        icon.setFitWidth(iconWidth);
        icon.setFitHeight(iconHeight);

        Button button = new Button();
        button.setGraphic(icon);

        // Change to CSS at some point
        //button.setStyle("-fx-background-color: #696969; -fx-cursor: hand; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        button.getStyleClass().add("image-button");
        button.setPrefSize(buttonSize, buttonSize);
        return button;

    }

    // ____________________________________________________

    private void openMenuType(String role) {
        switch (role) {
            case "Student":
                this.getChildren().add(displayStudent());
                break;
            case "Customer":
                this.getChildren().add(displayCustomer());
                break;
            case "Teacher":
                this.getChildren().add(displayTeacher());
                break;
            case "Support":
                this.getChildren().add(displaySupport());
                break;
            case "School":
                this.getChildren().add(displaySchool());
                break;
            case null:
                System.out.println("Role is null! Contact an admin!");
                break;
            default:
                System.out.println("A MAJOR ERROR HAS OCCURRED!");
                break;
        }
    }

} // Class end
