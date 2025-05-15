// Package
package App;

// Import
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.layout.Priority;

public class Menu extends Pane {

    // Attributes

    // DATATYPE //
    private String username;
    private String password;
    private String titleText = "Welcome";
    private int sceneWidth;
    private int sceneHeight;
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";
    private boolean isDarkMode = true;

    // OBJECT //
    private TextArea chatArea;
    private TextField messageField;
    private String currentChatPartner;
    private VBox userListBox;
    private Button cancelButton;
    private Label header;
    private ComboBox<String> box1;
    private ComboBox<String> box2;
    private ComboBox<String> box3;
    private ComboBox<String> box4;
    private Setting setting;
    private VBox messageArea;

    // ____________________________________________________

    public Menu(String username, String password, int sceneWidth, int sceneHeight){

        // User data
        this.username = username;
        this.password = password;

        // Scene Setup
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.setPrefHeight(sceneHeight);
        this.setPrefWidth(sceneWidth);

        // instantiate (forstår du?)
        setting = new Setting(this.username);

        // Create
        this.getChildren().add(displayHeader());

    }

    // ____________________________________________________

    private void loadOnlineUsers() {

        if (userListBox.getChildren().size() > 1) {
            userListBox.getChildren().remove(1, userListBox.getChildren().size());
        }

        if (Main.db.isConnected()) {
            String query = "SELECT username FROM users WHERE status = 'Online' AND username != '" + username + "'";
            ResultSet rs = Main.db.executeQuery(query);

            try {
                boolean hasUsers = false;
                while (rs != null && rs.next()) {
                    hasUsers = true;
                    String onlineUser = rs.getString("username");

                    Button userButton = new Button(onlineUser);
                    userButton.getStyleClass().add("user-button");
                    userButton.setPrefWidth(220);
                    Animation.addHoverScaleEffect(userButton);

                    userButton.setOnAction(e -> {
                        currentChatPartner = onlineUser;
                        loadChatHistory(onlineUser);
                    });

                    userListBox.getChildren().add(userButton);
                }

                if (!hasUsers) {
                    Label noUsersLabel = new Label("No users online");
                    noUsersLabel.getStyleClass().add("label");
                    userListBox.getChildren().add(noUsersLabel);
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving online users: " + e.getMessage());
            }
        }

    }

    // ____________________________________________________

    private void loadChatHistory(String partner) {
        chatArea.clear();
        if (Main.db.isConnected()) {
            ResultSet rs = Main.db.getMessages(username, partner);
            try {
                while (rs != null && rs.next()) {
                    String sender = rs.getString("sender");
                    String content = rs.getString("content");
                    String timestamp = rs.getString("timestamp");

                    String messagePrefix = sender.equals(username) ? "You: " : sender + ": ";
                    chatArea.appendText(messagePrefix + content + " (" + timestamp + ")\n");
                }
            } catch (SQLException e) {
                System.out.println("Error loading chat history: " + e.getMessage());
            }
        }

    }

    // ____________________________________________________

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (message.isEmpty() || currentChatPartner == null) {
            return;
        }

        if (Main.db.isConnected()) {
            if (Main.db.saveMessage(username, currentChatPartner, message)) {
                messageField.clear();
                loadChatHistory(currentChatPartner);
            }
        }

    }

    // ____________________________________________________

    public void refreshUserList() {
        loadOnlineUsers();
    }

    // ____________________________________________________

    public void cleanup() {
        /*if (Main.db != null) {
            Main.db.closeConnection();
        }*/
    }

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

        // Sidebar add
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
        sidebar.setStyle("-fx-background-color: #696969; -fx-border-radius: 20 0 0 20; -fx-background-radius: 20 0 0 20; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646");

        // Buttons
        Button setting1 = new Button("Darkmode");
        Button setting2 = new Button("Language");
        Button setting3 = new Button("Log Out");
        Button setting4 = new Button("Delete Account");
        Button setting5 = new Button("Change Profile Colors");

        sidebar.getChildren().addAll(setting1, setting2, setting3, setting4, setting5);
        setting1.getStyleClass().addAll("user-button", "user-button1");
        setting2.getStyleClass().add("user-button");
        setting3.getStyleClass().add("user-button");
        setting4.getStyleClass().add("user-button-delete"); // RED
        setting5.getStyleClass().add("user-button");

        // Message area (4/5)
        messageArea = new VBox(15);
        messageArea.setPrefWidth(760 * 0.74);
        messageArea.setPadding(new Insets(0));
        messageArea.setAlignment(Pos.TOP_LEFT);
        messageArea.setStyle("-fx-background-color: transparent;");

        // Add to HBox (Left -> Right)
        messageHBox.getChildren().addAll(sidebar, messageArea);
        settingsVBox.getChildren().add(messageHBox);

        // Actions
        setting1.setOnAction(e -> {
            darkmodeToggle(isDarkMode);
        });

        setting2.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        setting3.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(setting.displayLogOutJonas());
        });

        setting4.setOnAction(e -> {
            messageArea.getChildren().clear(); // DUMME IDIOT JONAS FUCK JEG ER DUM JO
            messageArea.getChildren().add(setting.displayDeleteJonas());
        });

        setting5.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        return settingsVBox;
    }

    // ____________________________________________________

    public VBox displaySupportSettings() {

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
        Button setting2 = new Button("Language");
        Button setting3 = new Button("Log out");
        Button setting4 = new Button("Delete Account");

        sidebar.getChildren().addAll(setting1, setting2, setting3, setting4);
        setting1.getStyleClass().addAll("user-button", "user-button1");
        setting2.getStyleClass().add("user-button");
        setting3.getStyleClass().add("user-button-delete");
        setting4.getStyleClass().add("user-button-delete"); // RED

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
            //displayDelete
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

    public HBox displayAvailableBookings() {

        // FRAME MOD HØJRE
        HBox mainContainer = new HBox(0);
        mainContainer.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        mainContainer.setPrefWidth(800);
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.setLayoutY(0);

        // TOP MENU MOD HØJRE MED EN FIXED HØJDE // HOLDER ALLE ELEMENTER VENSTRE -------------> HØJRE
        HBox topMenuHBox = new HBox(10);
        topMenuHBox.setPadding(new Insets(10, 10, 10, 10));
        topMenuHBox.setPrefWidth(800);
        topMenuHBox.setPrefHeight(50);
        topMenuHBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: rgb(0, 0, 0); -fx-background-color: #575757");

        // Label
        Label availableLabel = new Label("Available bookings");
        availableLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px");

        // VBox for title
        VBox availableSection = new VBox();
        availableSection.setPrefWidth(200);
        availableSection.setPadding(new Insets(0,0,0,0));
        availableSection.setAlignment(Pos.CENTER);
        availableSection.getChildren().add(availableLabel);

        ComboBox<String> cityDropDown = new ComboBox<>();
        cityDropDown.getItems().addAll("Hillerød", "Gentofte", "Hellerup", "Lyngby", "KBH", "Charlottenlund");//TODO: Smid metode på for at hente fra db
        cityDropDown.setPromptText("City");
        cityDropDown.setPrefWidth(600/4);
        cityDropDown.getStyleClass().add("combo-box-availablebooking");

        ComboBox<String> dateDropDown = new ComboBox<>();
        dateDropDown.getItems().addAll("16.05.2025", "17.05.2025", "18.05.2025", "19.05.2025", "20.05.2025", "21.05.2025", "22.05.2025");
        dateDropDown.setPromptText("Date");
        dateDropDown.setPrefWidth(600/4);
        dateDropDown.getStyleClass().add("combo-box-availablebooking");

        ComboBox<String> ratingDropDown = new ComboBox<>();
        ratingDropDown.getItems().addAll("1", "2", "3", "4", "5");
        ratingDropDown.setPromptText("Rating");
        ratingDropDown.setPrefWidth(600/4);
        ratingDropDown.getStyleClass().add("combo-box-availablebooking");

        ComboBox<String> studentDropDown = new ComboBox<>();
        studentDropDown.getItems().addAll("Jens", "Line", "Lone", "Lotto", "Mohammed", "Charlotte");
        studentDropDown.setPromptText("Student");
        studentDropDown.setPrefWidth(600/4);
        studentDropDown.getStyleClass().add("combo-box-availablebooking");

        topMenuHBox.getChildren().addAll(availableSection, cityDropDown, dateDropDown, ratingDropDown, studentDropDown);

        mainContainer.getChildren().add(topMenuHBox);

        return mainContainer;
    }

    // ____________________________________________________

    public HBox displayExamMenu() {

        // FRAME MOD HØJRE
        HBox mainContainer = new HBox(0);
        mainContainer.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        mainContainer.setPrefWidth(800);
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.setLayoutY(300);

        // TOP MENU MOD HØJRE MED EN FIXED HØJDE // HOLDER ALLE ELEMENTER VENSTRE -------------> HØJRE
        HBox topMenuHBox = new HBox(10);
        topMenuHBox.setPadding(new Insets(10, 10, 10, 10));
        topMenuHBox.setPrefWidth(800);
        topMenuHBox.setPrefHeight(50);
        topMenuHBox.setStyle("-fx-border-width: 2px 0 2px 0; -fx-border-color: rgb(0, 0, 0); -fx-background-color: #575757");

        // Label
        Label availableLabel = new Label("Exam Bookings");
        availableLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px");

        // VBox for title
        VBox availableSection = new VBox();
        availableSection.setPrefWidth(200);
        availableSection.setPadding(new Insets(0,0,0,0));
        availableSection.setAlignment(Pos.CENTER);
        availableSection.getChildren().add(availableLabel);

        topMenuHBox.getChildren().add(availableSection);

        mainContainer.getChildren().add(topMenuHBox);

        return mainContainer;
    }

    // ____________________________________________________

    public HBox displayBookingCard(){

        // Frame towards right
        HBox cardContainer = new HBox(0);
        cardContainer.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        cardContainer.setPrefWidth(800);
        cardContainer.setAlignment(Pos.TOP_LEFT);
        cardContainer.setLayoutY(57); // height + border (2px)

        // cardBoxMenu (under menu)
        HBox cardBox = new HBox();
        cardBox.setPrefWidth(800);
        cardBox.setPrefHeight(250); // 50 + 50 = 500 / 2

        for(int i = 0; i < 5; i++){

            // Labels
            Label date = new Label("26.03.2025");
            Label place = new Label("Hillerød 3400" + " " + "Narkovej 69");
            Label time = new Label("15:30");
            Label rating = new Label(convertToStars(4));

            place.setWrapText(true);
            place.setPadding(new Insets(0, 10, 0, 10));
            rating.setPadding(new Insets(0, 0, 20, 0));

            // CSS
            date.getStyleClass().add("card-visuals-header");
            place.getStyleClass().add("card-visuals-lol");
            time.getStyleClass().add("card-visuals-bold");
            rating.getStyleClass().add("card-visuals-rating");

            VBox cardHeader = new VBox();
            cardHeader.setPrefHeight(70);
            cardHeader.setAlignment(Pos.CENTER);
            cardHeader.getStyleClass().add("card-header-visuals");
            cardHeader.getChildren().add(date);

            // Card display
            VBox card = new VBox(10);
            card.setPrefWidth(160);
            card.setPrefHeight(248);
            card.setAlignment(Pos.TOP_CENTER);
            card.getStyleClass().add("card-background-visuals");

            // SKYD MIG

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            // Hover effect on card :hover
            Animation.addHoverScaleEffectVBox(card);

            // Add labels to card
            card.getChildren().addAll(cardHeader, time, place, spacer, rating);

            // Add card to CardBox
            cardBox.getChildren().add(card);

        }

        // Add cardContainer to final HBox
        cardContainer.getChildren().add(cardBox);

        return cardContainer;
    }

    // ____________________________________________________

    public HBox displayExamCard(){

        // Frame towards right
        HBox cardContainer = new HBox(0);
        cardContainer.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        cardContainer.setPrefWidth(800);
        cardContainer.setAlignment(Pos.TOP_LEFT);
        cardContainer.setLayoutY(354);

        // cardBoxMenu (under menu)
        HBox cardBox = new HBox();
        cardBox.setPrefWidth(800);
        cardBox.setPrefHeight(250); // 50 + 50 = 500 / 2

        for(int i = 0; i < 5; i++){

            // Labels
            Label date = new Label("26.03.2025");
            Label place = new Label("Hillerød 3400" + " " + "Narkovej 69");
            Label time = new Label("15:30");
            Label rating = new Label(convertToStars(4));

            place.setWrapText(true);
            place.setPadding(new Insets(0, 10, 0, 10));
            rating.setPadding(new Insets(0, 0, 20, 0));

            // CSS
            date.getStyleClass().add("card-visuals-header");
            place.getStyleClass().add("card-visuals-lol");
            time.getStyleClass().add("card-visuals-bold");
            rating.getStyleClass().add("card-visuals-rating");

            VBox cardHeader = new VBox();
            cardHeader.setPrefHeight(70);
            cardHeader.setAlignment(Pos.CENTER);
            cardHeader.setStyle("-fx-background-color: orange");
            cardHeader.getChildren().add(date);

            // Card display
            VBox card = new VBox(10);
            card.setPrefWidth(160);
            card.setPrefHeight(248);
            card.setAlignment(Pos.TOP_CENTER);
            card.getStyleClass().add("card-background-visuals");

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            // Hover effect on card :hover
            Animation.addHoverScaleEffectVBox(card);

            // Add labels to card
            card.getChildren().addAll(cardHeader, time, place, spacer, rating);

            // Add card to CardBox
            cardBox.getChildren().add(card);

        }

        // Add cardContainer to final HBox
        cardContainer.getChildren().add(cardBox);

        return cardContainer;
    }

    // ____________________________________________________

    public HBox displayExamBookings() {
        HBox examBooking = new HBox(20);
        examBooking.setAlignment(Pos.CENTER);
        examBooking.setPadding(new Insets(20));
        examBooking.setLayoutY(320);

        for (int i = 0; i < 3; i++) {
            VBox menu = new VBox(15);
            menu.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            menu.getStyleClass().add("booking-vbox");

            menu.setAlignment(Pos.CENTER);
            menu.setPrefWidth(200); // Card Width

            VBox topMenu = new VBox(15);
            topMenu.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            topMenu.getStyleClass().add("topMenu-vbox");

            // Labels
            Label dateLabel = new Label(getDate());
            Label placeLabel = new Label(getAdress());
            Label ratingLabel = new Label(convertToStars(getRating()));

            dateLabel.getStyleClass().add("card-text-header");
            placeLabel.getStyleClass().add("card-text");
            ratingLabel.getStyleClass().add("star");

            dateLabel.setWrapText(true);
            placeLabel.setWrapText(true);
            ratingLabel.setWrapText(true);

            topMenu.getChildren().addAll(dateLabel);
            menu.getChildren().addAll(topMenu, placeLabel, ratingLabel);

            menu.setOnMouseClicked(event -> {
                System.out.println("DEBUG // WORKS");
            });

            // VBox animation
            Animation.addHoverScaleEffectVBox(menu);

            examBooking.getChildren().add(menu);
        }

        return examBooking;
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

    public VBox displayExamHeader(String exam){

        VBox title = new VBox(15);
        title.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        header = new Label(exam);
        header.setAlignment(Pos.CENTER);
        header.getStyleClass().add("header");

        title.setAlignment(Pos.CENTER);
        title.setPrefWidth(200);
        title.setLayoutX(20);
        title.setLayoutY(260);

        title.getChildren().addAll(header);

        return title;
    }

    // ____________________________________________________

    public VBox displayProfile() {

        VBox profileVBox = new VBox(0);
        profileVBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        profileVBox.setAlignment(Pos.TOP_CENTER);
        profileVBox.setLayoutX(20);
        profileVBox.setLayoutY(20);
        profileVBox.setPrefWidth(760);
        profileVBox.setPrefHeight(560);
        profileVBox.getStyleClass().add("profile-vbox");

        HBox profilePictureHBox = new HBox();
        profilePictureHBox.setPrefWidth(760);
        profilePictureHBox.setPrefHeight(150);
        profilePictureHBox.setSpacing(0);
        profilePictureHBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        profilePictureHBox.getStyleClass().add("profilePictureHBox-visuals");

        VBox leftPane = new VBox();
        leftPane.setPrefWidth(190);
        leftPane.setPrefHeight(560);
        leftPane.setAlignment(Pos.TOP_CENTER);

        Image profileImage = new Image(getClass().getResource("/assets/profile/person1.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(150);
        profileImageView.setFitHeight(130);
        profileImageView.setPreserveRatio(true);

        Label roleLabel = new Label("Student");
        roleLabel.setStyle("-fx-padding: 5;");

        VBox leftPaneInfo = new VBox();
        leftPaneInfo.setAlignment(Pos.CENTER);
        leftPaneInfo.getChildren().add(roleLabel);

        TextArea aboutMeTextArea = new TextArea();
        aboutMeTextArea.setPrefRowCount(4);
        aboutMeTextArea.setPrefWidth(180);
        aboutMeTextArea.setPrefHeight(240);
        aboutMeTextArea.setWrapText(true);
        aboutMeTextArea.setPromptText("Dette er hardcoded info om mig. Hej på dig");
        boolean isOwner = false; // SKAL LAVES TIL EN METODE!!!
        aboutMeTextArea.setDisable(!isOwner);
        aboutMeTextArea.setEditable(isOwner);
        aboutMeTextArea.setFocusTraversable(false);

        // Rating Box
        HBox ratingBox = new HBox(10);
        ratingBox.setAlignment(Pos.CENTER);
        Label star = new Label(convertToStars(getRating()));
        star.getStyleClass().add("star");
        ratingBox.getChildren().add(star);
        leftPane.getChildren().addAll(profileImageView, leftPaneInfo);

        VBox rightPaneBanner = new VBox();
        rightPaneBanner.setPrefHeight(132);
        rightPaneBanner.setPrefWidth(570);
        rightPaneBanner.getStyleClass().add("right-pane-banner");
        rightPaneBanner.setStyle("-fx-border-color: rgb(0, 0, 0); -fx-border-width: 0 0 2px 2px; -fx-padding: 2px 0 0 0;");

        VBox bottomContentBox = new VBox();
        bottomContentBox.setPrefWidth(760);
        bottomContentBox.setAlignment(Pos.TOP_LEFT);
        bottomContentBox.getStyleClass().add("bottom-content-box");

        Region spacer = new Region();
        spacer.setPrefHeight(2);  // 2px of space

        VBox rightPane = new VBox();
        rightPane.setPrefWidth(570);
        rightPane.setPrefHeight(560);
        rightPane.setAlignment(Pos.TOP_CENTER);

        HBox navRow = new HBox();
        navRow.setPrefHeight(57);
        navRow.setAlignment(Pos.CENTER);
        navRow.setSpacing(0);

        HBox aboutMeBox = createNavBox("About me");
        HBox availableBookingsBox = createNavBox("Available Bookings");
        HBox reviewsBox = createNavBox("Reviews");
        HBox galleryBox = createNavBox("Gallery");

        navRow.getChildren().addAll(aboutMeBox, availableBookingsBox, reviewsBox, galleryBox);

        VBox rightPaneContent = new VBox(10);
        rightPaneContent.setPrefWidth(550);
        rightPaneContent.setAlignment(Pos.TOP_CENTER);

        // Actions
        availableBookingsBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            availableBookingsBox.getStyleClass().add("nav-box-selected");
            setHeaderTitle("Book");
            getChildren().clear();
            getChildren().add(displayHeader());
            getChildren().add(displayExamHeader("Exam"));
            getChildren().add(displayComboBox());
            getChildren().add(displayAvailableBookings());
            getChildren().add(displayExamBookings());
        });

        aboutMeBox.setOnMouseClicked(e -> {
            availableBookingsBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            aboutMeBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayAboutMe());
        });

        reviewsBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected");
            availableBookingsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayReview());
        });

        galleryBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected");
            availableBookingsBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayPictures());
        });

        // CSS
        leftPane.getStyleClass().add("left-pane");
        leftPaneInfo.getStyleClass().add("left-pane-info");
        aboutMeTextArea.getStyleClass().add("about-me-text-area");
        rightPane.getStyleClass().add("right-pane");

        // Default Selected Box
        aboutMeBox.getStyleClass().add("nav-box-selected");

        // Add
        rightPane.getChildren().addAll(rightPaneBanner, navRow, rightPaneContent);
        profilePictureHBox.getChildren().addAll(leftPane, rightPane);
        profileVBox.getChildren().addAll(profilePictureHBox, bottomContentBox);

        return profileVBox;
    }

    // ____________________________________________________

    private HBox createNavBox(String title) {

        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(570/4);
        box.setPrefHeight(40);
        box.setSpacing(0);
        box.setPadding(Insets.EMPTY);
        box.getStyleClass().add("nav-box");

        Label label = new Label(title);
        label.getStyleClass().add("nav-label");

        HBox.setHgrow(label, Priority.ALWAYS);

        box.getChildren().add(label);

        return box;

    }

    // ____________________________________________________

    public VBox displayReview() {

        VBox reviewContainer = new VBox(10);
        reviewContainer.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("My Recent Reviews");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #FFF; -fx-border-width: 0 0 2px 0; -fx-border-color: orange");
        VBox.setMargin(title, new Insets(20, 0, 10, 0));

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(30);
        flowPane.setVgap(20);
        flowPane.setAlignment(Pos.CENTER);

        for (int i = 0; i < 9; i++) {
            VBox singleReview = new VBox(5);
            singleReview.setPadding(new Insets(30));
            singleReview.setStyle("-fx-background-color: white; -fx-background-radius: 10px; "
            + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

            Label header = new Label("Rigtig god klip");
            header.setStyle("-fx-text-alignment: CENTER !important; -fx-font-weight: bold; -fx-font-size: 14px;");

            Label body = new Label("Det var en rigtig god klipning, og kan varmt anbefales!");
            body.setStyle("-fx-font-size: 10px;");
            body.setWrapText(true);
            body.setMaxWidth(760/2);

            Label stars = new Label(convertToStars(getRating()));
            stars.getStyleClass().add("star");

            singleReview.getChildren().addAll(header, body, stars);
            flowPane.getChildren().add(singleReview);
        }

        ScrollPane scrollPane = new ScrollPane(flowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(600);

        scrollPane.setStyle("-fx-padding: 10px 0 20px 0; -fx-background: transparent; -fx-background-color: transparent;");
        flowPane.setStyle("-fx-padding: 10px 0 20px 0; -fx-background-color: transparent;");

        // Remove scroll visuals
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        reviewContainer.getChildren().addAll(title, scrollPane);
        return reviewContainer;
    }

    // ____________________________________________________

    public VBox displayPictures() {

        VBox pictureContainer = new VBox(10);
        pictureContainer.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("My Recent Cuts");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #FFF; -fx-border-width: 0 0 2px 0; -fx-border-color: orange");
        VBox.setMargin(title, new Insets(20, 0, 10, 0));

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(30);
        flowPane.setVgap(20);
        flowPane.setAlignment(Pos.TOP_CENTER);
        flowPane.setStyle("-fx-background-color: transparent;");

        for (int i = 1; i <= 10; i++) {
            VBox pictureBox = new VBox(5);
            pictureBox.setAlignment(Pos.TOP_CENTER);
            pictureBox.setPadding(new Insets(20));
            pictureBox.setStyle("-fx-background-color: white; -fx-background-radius: 10px; "
            + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
            pictureBox.setMaxWidth(760/2);

            Label header = new Label("Fresh fade");
            header.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-alignment: center");

            // Fix visuals a bit more at some point
            try {
                String path = "/assets/slideshow/" + i + ".png";
                Image image = new Image(getClass().getResource(path).toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(180);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);

                pictureBox.getChildren().addAll(header, imageView);
                flowPane.getChildren().add(pictureBox);
            } catch (Exception e) {
                System.out.println("Failed to load picture: " + i + ".png");
            }
        }

        ScrollPane scrollPane = new ScrollPane(flowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(600);
        scrollPane.setStyle("-fx-padding: 10px 0 20px 0; -fx-background: transparent; -fx-background-color: transparent;");
        flowPane.setStyle("-fx-padding: 10px 0 20px 0; -fx-background-color: transparent;");

        // Remove scroll visuals
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        pictureContainer.getChildren().addAll(title, scrollPane);
        return pictureContainer;
    }

    // ____________________________________________________

    public VBox displayAboutMe() {
        VBox mainContainer = new VBox();
        mainContainer.setPadding(Insets.EMPTY);
        mainContainer.setSpacing(0);
        mainContainer.setAlignment(Pos.TOP_LEFT);

        HBox splitBox = new HBox();
        splitBox.setSpacing(0);
        splitBox.setPadding(Insets.EMPTY);
        splitBox.setAlignment(Pos.TOP_LEFT);
        splitBox.setPrefHeight(560);
        splitBox.setPrefWidth(760);

        VBox leftBox = new VBox();
        leftBox.setPadding(Insets.EMPTY);
        leftBox.setSpacing(0);
        leftBox.setStyle("-fx-background-color: transparent; -fx-border-radius: 0 0 0 20px; -fx-background-radius: 0 0 0 20px; -fx-border-width: 0 2px 0 0; -fx-border-color: rgb(0,0,0); -fx-padding: 20px 0 0 0");
        leftBox.setPrefWidth(190);
        leftBox.setAlignment(Pos.TOP_CENTER);

        // VBox inside HBox (leftpane)
        VBox infoSections = new VBox(10);
        infoSections.setAlignment(Pos.TOP_CENTER);

        Label starsLabel = new Label(convertToStars(4));
        starsLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: orange;");

        Label lastOnlineLabel = new Label("Last Online:");
        Label lastOnlineValue = new Label("2 hours ago");

        // Socials
        Label instagramLabel = new Label("Instagram");
        Label facebookLabel = new Label("Facebook");
        Label githubLabel = new Label("Github");

        // Socials VBox
        VBox socialsBox = new VBox(10, instagramLabel, facebookLabel, githubLabel);
        socialsBox.setAlignment(Pos.CENTER);

        VBox ratingBox = new VBox(5, starsLabel);
        ratingBox.setAlignment(Pos.CENTER);
        VBox lastOnlineBox = new VBox(5, lastOnlineLabel, lastOnlineValue);
        lastOnlineBox.setAlignment(Pos.CENTER);

        // Visuals
        socialsBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: rgb(0,0,0); -fx-padding: 0 0 10px 0");
        lastOnlineBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: rgb(0,0,0); -fx-padding: 0 0 20px 0");
        lastOnlineLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        instagramLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #fff");
        facebookLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #fff");
        githubLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #fff");
        lastOnlineValue.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #fff");

        // VBox add
        infoSections.getChildren().addAll(ratingBox, lastOnlineBox, socialsBox);
        infoSections.setAlignment(Pos.CENTER);

        VBox wrapper = new VBox();
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.getChildren().add(infoSections);

        leftBox.getChildren().add(infoSections);
        leftBox.setAlignment(Pos.TOP_CENTER);

        VBox rightBox = new VBox();
        rightBox.setPrefWidth(560);
        rightBox.setPadding(Insets.EMPTY);
        rightBox.setSpacing(0);
        rightBox.setAlignment(Pos.TOP_LEFT);

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(0);
        flowPane.setVgap(0);
        flowPane.setPadding(Insets.EMPTY);
        flowPane.setAlignment(Pos.TOP_LEFT);
        flowPane.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(flowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(560);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPadding(Insets.EMPTY);

        rightBox.getChildren().add(scrollPane);

        splitBox.getChildren().addAll(leftBox, rightBox);
        mainContainer.getChildren().add(splitBox);

        return mainContainer;
    }

    // ____________________________________________________

    public VBox displayComboBox() {

        VBox outerBox = new VBox(0);
        outerBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        outerBox.getStyleClass().add("combo-box-book");
        outerBox.setAlignment(Pos.CENTER);
        outerBox.setPrefWidth(220);
        outerBox.setLayoutX(240);
        outerBox.setLayoutY(20);

        // First Row (Center align ComboBoxes)
        HBox row1 = new HBox(10);
        row1.setAlignment(Pos.CENTER);  // Center the ComboBoxes in this row

        ComboBox<String> box1 = new ComboBox<>();
        box1.getItems().addAll("Hillerød", "Gentofte", "Hellerup", "Lyngby", "KBH", "Charlottenlund");
        box1.setPromptText("City");
        box1.getStyleClass().add("combo-box-booking-fix");
        box1.setPrefWidth(100);
        box1.setStyle("-fx-alignment: center;");

        ComboBox<String> box2 = new ComboBox<>();
        box2.getItems().addAll("05/05", "06/05", "07/05", "08/05", "09/05", "10/05", "11/05");
        box2.setPromptText("Date");
        box2.getStyleClass().add("combo-box-booking-fix");
        box2.setPrefWidth(100);
        box2.setStyle("-fx-alignment: center;");

        // Second Row (Center align ComboBoxes)
        HBox row2 = new HBox(10);
        row2.setAlignment(Pos.CENTER);  // Center the ComboBoxes in this row

        ComboBox<String> box3 = new ComboBox<>();
        box3.getItems().addAll("1", "2", "3", "4", "5");
        box3.setPromptText("Rating");
        box3.getStyleClass().add("combo-box-booking-fix");
        box3.setPrefWidth(100);
        box3.setStyle("-fx-alignment: center;");

        ComboBox<String> box4 = new ComboBox<>();
        box4.getItems().addAll("New", "1", "2", "3", "4");
        box4.setPromptText("Year");
        box4.getStyleClass().add("combo-box-booking-fix");
        box4.setPrefWidth(100);
        box4.setStyle("-fx-alignment: center;");

        // Add the ComboBoxes to their respective rows
        row1.getChildren().addAll(box1, box2);
        row2.getChildren().addAll(box3, box4);

        // Add the rows to the outer VBox
        outerBox.getChildren().addAll(row1, row2);

        return outerBox;
    }

    // ____________________________________________________

    public String getDate() {

        int day = (int) (Math.random() * 28) + 1; // Fuck the rest of the days. Hardcoded anyways.
        int month = (int) (Math.random() * 12) + 1;
        String year = "2025";

        return String.valueOf(day) + "."  + String.valueOf(month) + "." + year;

    }

    // ____________________________________________________

    public String getTime() {

        int hour = (int) (Math.random() * 24) + 1;
        int minutes = (int) (Math.random() * 60) + 1;

        return String.valueOf(hour) + ":" + String.valueOf(minutes);

    }

    // ____________________________________________________

    public String getStudentName() {
        String[] names =
                {
                        "Jonas MunkeDahl",
                        "Andreas Lortelort",
                        "Ebou Gedemunk",
                        "Carl-Emil Gok",
                        "Tess Something",
                        "Tine Dahl",
                };

        int randomName = (int) (Math.random() * names.length);

        return names[randomName];
    }

    // ____________________________________________________

    public String getAdress() {
        String[] adress =
                {
                        "Bytoften 21, 2650 Hvidovre",
                        "fawfawf 11, 3650 Narko",
                        "Place 44, 5550 Ged",
                        "Lort 4, 6650 Gok",
                        "Tessfad 66, 6666 Hillerød",
                        "Wtfffff 55, 5100 Yessir",
                };

        int randomAdress = (int) (Math.random() * adress.length);

        return adress[randomAdress];
    }

    // ____________________________________________________

    public double getRating() {
        return 1 + (Math.random() * 4);
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

        rating = Math.max(0, Math.min(5, rating));

        int fullStars = (int) rating;
        boolean halfStar = (rating - fullStars) >= 0.5;
        int emptyStars = 5 - fullStars - (halfStar ? 1 : 0);

        // Add full stars
        for (int i = 0; i < fullStars; i++) {
            message += "★";
        }

        // Must be here else it fucks up and only shows 4 stars
        if (halfStar) {
            message += "☆";
        }

        for (int i = 0; i < emptyStars; i++) {
            message += "☆";
        }

        return message;
    }

    // ____________________________________________________

    protected void darkmodeToggle(boolean darkmode) {

        if (darkmode) {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(setting.displayDarkmodeJonas());
        } else {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(setting.displayLightmodeJonas());
        }
    }

    // ____________________________________________________

    public VBox displayAdminMenu() {

        // VBox
        VBox adminMenuVBox = new VBox();
        adminMenuVBox.setLayoutX(20);
        adminMenuVBox.setLayoutY(100);
        adminMenuVBox.setPrefWidth(760);
        adminMenuVBox.setPrefHeight(480);
        adminMenuVBox.setStyle("-fx-border-color: #464646; -fx-border-width: 2px; -fx-border-radius: 20px; -fx-background-radius: 20px;");

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
        sidebar.setStyle("-fx-background-color: #696969; -fx-border-radius: 20 0 0 20; -fx-background-radius: 20 0 0 20; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646");

        // Buttons
        Button admin1 = new Button("Ban user");
        Button admin2 = new Button("Unban user");
        Button admin3 = new Button("Add school");
        Button admin4 = new Button("Change role");
        Button admin5 = new Button("Change profile picture"); // hvis nogen har hitler som profil billede
        Button admin6 = new Button("Change colors");
        Button admin7 = new Button("Change username");
        Button admin8 = new Button("Remove school");
        Button admin9 = new Button("Mute user");
        Button admin10 = new Button("Change number");
        Button admin11 = new Button("Change mail");

        sidebar.getChildren().addAll(admin1, admin2, admin3, admin4, admin5, admin6, admin7, admin8, admin9, admin10, admin11);
        admin1.getStyleClass().addAll("user-button", "user-button1");
        admin2.getStyleClass().add("user-button");
        admin3.getStyleClass().add("user-button");
        admin4.getStyleClass().add("user-button");
        admin5.getStyleClass().add("user-button");
        admin6.getStyleClass().add("user-button");
        admin7.getStyleClass().add("user-button");
        admin8.getStyleClass().add("user-button");
        admin9.getStyleClass().add("user-button");
        admin10.getStyleClass().add("user-button");
        admin11.getStyleClass().addAll("user-button", "user-button-last");

        // Message area (4/5)
        messageArea = new VBox(15);
        messageArea.setPrefWidth(760 * 0.74);
        messageArea.setPadding(new Insets(0));
        messageArea.setAlignment(Pos.TOP_LEFT);
        messageArea.setStyle("-fx-background-color: transparent;");

        // Add to HBox (Left -> Right)
        messageHBox.getChildren().addAll(sidebar, messageArea);
        adminMenuVBox.getChildren().add(messageHBox);

        // Actions
        admin1.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(Setting.displayBanUser());
        });

        admin2.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        admin3.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        admin4.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        admin5.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        admin6.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        admin7.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        admin8.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        admin9.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        admin10.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        admin11.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        return adminMenuVBox;
    }

}
