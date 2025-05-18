// Package
package App;

// Import
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    private DatePicker datePicker;
    private TextField addressField;
    private ComboBox<String> timePicker;
    private ComboBox<String> hairTypeDropdown;
    private ComboBox<String> genderDropdown;
    private ComboBox<String> lengthDropdown;
    private ComboBox<String> hairColorDropdown;
    private ComboBox<Integer> reviewPicker;
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

        // instantiate
        setting = new Setting(this.username);

        // Create
        this.getChildren().add(displayWelcome());

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

        VBox messageVBox = new VBox();
        messageVBox.setLayoutX(20);
        messageVBox.setLayoutY(100);
        messageVBox.setPrefWidth(760);
        messageVBox.setPrefHeight(480);
        messageVBox.setStyle("-fx-border-color: #464646; -fx-border-width: 2px; -fx-border-radius: 20px; -fx-background-radius: 20px;");

        HBox messageHBox = new HBox();
        messageHBox.getStyleClass().add("message-vbox");
        messageHBox.setPrefWidth(760);
        messageHBox.setPrefHeight(480);
        messageHBox.setAlignment(Pos.TOP_LEFT);
        messageHBox.setSpacing(0);

        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(760 * 0.26);
        sidebar.setAlignment(Pos.TOP_LEFT);
        sidebar.setPadding(Insets.EMPTY);
        sidebar.setStyle("-fx-background-color: #696969; -fx-border-radius: 20 0 0 20; -fx-background-radius: 20 0 0 20;");

        Button user1 = new Button("Jonas");
        Button user2 = new Button("Andreas");
        Button user3 = new Button("Ebou");
        Button user4 = new Button("Carl-Emil");

        sidebar.getChildren().addAll(user1, user2, user3, user4);
        user1.getStyleClass().addAll("user-button", "user-button1");
        user2.getStyleClass().add("user-button");
        user3.getStyleClass().add("user-button");
        user4.getStyleClass().add("user-button");

        VBox rightContent = new VBox(10);
        rightContent.setPrefWidth(760 * 0.74);
        rightContent.setAlignment(Pos.TOP_LEFT);
        rightContent.setPadding(new Insets(0));

        VBox messageArea = new VBox(15);
        messageArea.setPadding(new Insets(20));
        messageArea.setAlignment(Pos.TOP_LEFT);
        messageArea.setStyle("-fx-background-color: transparent;");
        messageArea.setPrefHeight(420);

        HBox inputArea = new HBox(10);
        inputArea.setPadding(new Insets(10));
        inputArea.setAlignment(Pos.CENTER_LEFT);
        inputArea.setPrefHeight(40);

        TextField messageInput = new TextField();
        messageInput.setPromptText("Type your message...");
        messageInput.setPrefWidth(480);

        Image sendIcon = new Image(getClass().getResource("/assets/icons/icon17.png").toExternalForm());
        ImageView sendIconView = new ImageView(sendIcon);
        sendIconView.setFitWidth(30);
        sendIconView.setFitHeight(30);
        sendIconView.setPreserveRatio(true);

        Button sendButton = new Button();
        sendButton.setGraphic(sendIconView);
        sendButton.setStyle("-fx-background-color: orange; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-background-insets: 0; -fx-border-insets: 0; -fx-border-width: 1.5px; -fx-border-color: rgba(0,0,0,0.5); -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0.3, 0, 2);");

        sendButton.setOnAction(e -> {
            String text = messageInput.getText().trim();
            if (!text.isEmpty()) {
                messageArea.getChildren().add(
                        createMessageBubble(text, true, this.username, "Now")
                );
                messageInput.clear();
            }
        });

        inputArea.getChildren().addAll(messageInput, sendButton);
        rightContent.getChildren().addAll(messageArea, inputArea);

        messageHBox.getChildren().addAll(sidebar, rightContent);
        messageVBox.getChildren().add(messageHBox);

        user1.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().addAll(
            createMessageBubble("Shit du lugtede forleden bro.. Det helt galt. Kom aldrig igen. Forstår du?", false, "Ebou", "10:15"),
            createMessageBubble("My bad nigga. Jeg havde lort i numsen. Skal nok gå i bad næste gang.", true,"Jonas", "10:16"),
            createMessageBubble("Nigga what? Yous 20 and don't know how to wipe? Det low key crazy. Men fair nok. Wipe lige næste gang. Ses!", false,"Ebou", "10:17")
            );
        });
        user2.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().addAll(
            createMessageBubble("Det her er en tekst. Forstår du? Det tror jeg ikke du gør..", false, "Jonas", "10:15"),
            createMessageBubble("Det ren GG..", true,"Andreas", "10:16"),
            createMessageBubble("Nigga what?", false,"Jonas", "10:17")
            );
        });
        user3.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().addAll(
            createMessageBubble("Shit jeg er bare ikke ham jo..", false, "Jonas", "10:15"),
            createMessageBubble("Fax lil bro. Straight up fax. Ong no cap.", true,"Ebou", "10:16")
            );
        });
        user4.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().addAll(
            createMessageBubble("Hvad er dit navn", false, "Jonas", "10:15"),
            createMessageBubble("Carl Emil uden bindesteg din idiot.. Forstår du? Det var dog utroligt. Jeg er lige her jo!", true,"Carl Emil", "10:16"),
            createMessageBubble("lol. Muted.", false,"Jonas", "10:17")
            );
        });

        return messageVBox;
    }

    // ____________________________________________________

    private VBox createMessageBubble(String messageText, boolean rightAlign, String senderName, String timestamp) {

        // Sender + timestamp with a padding depending on the boolean in Parameter
        Label metaLabel = new Label(senderName + " - " + timestamp);
        metaLabel.setStyle("-fx-text-fill: #414141; -fx-font-size: 10px;");
        metaLabel.setAlignment(Pos.CENTER_LEFT);

        // Padding on either side for visuals
        if(rightAlign){
            metaLabel.setPadding(new Insets(0,10,0,0));
        } else {
            metaLabel.setPadding(new Insets(0,0,0,10));
        }

        // Msg with a width and wrap enabled
        Label messageLabel = new Label(messageText);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(300);

        // Visuals of each msg
        messageLabel.setStyle(
            "-fx-background-color: " + (rightAlign ? "#d1f0ff" : "#ffcf80") + ";" +
            "-fx-padding: 12 18 12 18;" +
            "-fx-background-radius: 18;" +
            "-fx-border-radius: 18;" +
            "-fx-border-width: 1;" +
            "-fx-border-color: rgba(0, 0, 0, 0.1);" +
            "-fx-text-fill: #505050;" +
            "-fx-font-size: 15px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0.3, 0, 2);"
        );

        // Message container
        VBox messageContainer = new VBox(3, metaLabel, messageLabel);
        messageContainer.setAlignment(rightAlign ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        // HBox
        HBox bubbleWrapper = new HBox(messageContainer);
        bubbleWrapper.setAlignment(rightAlign ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        return new VBox(bubbleWrapper);
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
        Button setting1 = new Button("Log Out");
        Button setting2 = new Button("Language");
        Button setting3 = new Button("Darkmode");
        Button setting4 = new Button("Change Profile Colors");
        Button setting5 = new Button("Delete Account");

        sidebar.getChildren().addAll(setting1, setting2, setting3, setting4, setting5);
        setting1.getStyleClass().addAll("user-button", "user-button1");
        setting2.getStyleClass().add("user-button");
        setting3.getStyleClass().add("user-button");
        setting4.getStyleClass().add("user-button");
        setting5.getStyleClass().add("user-button-delete"); // RED

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
            messageArea.getChildren().clear();
            messageArea.getChildren().add(setting.displayLogOutJonas());
        });

        setting2.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(setting.displayLanguage(Main.lang));
        });

        setting3.setOnAction(e -> {

            messageArea.getChildren().clear();

            String currentMode = Main.db.getDarkmode(username);
            String newMode = null;

            switch (currentMode){
                case "Yes":
                    Main.db.changeDarkmode(username, "No");
                    newMode = "No";
                    break;
                case "No":
                    Main.db.changeDarkmode(username, "Yes");
                    newMode = "Yes";
                    break;
                default:
                    System.out.println("Contact dev lol");
            }

            messageArea.getChildren().clear();

            if (newMode.equalsIgnoreCase("Yes")) {
                Main.db.changeDarkmode(username,newMode);
                messageArea.getChildren().add(setting.displayDarkmodeJonas());
            } else if (newMode.equalsIgnoreCase("No")) {
                Main.db.changeDarkmode(username,newMode);
                messageArea.getChildren().add(setting.displayLightmodeJonas());
            }

        });

        setting4.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        setting5.setOnAction(e -> {
            messageArea.getChildren().clear(); // DUMME IDIOT JONAS FUCK JEG ER DUM JO
            messageArea.getChildren().add(setting.displayDeleteJonas());
        });

        return settingsVBox;
    }

    // ____________________________________________________

    public VBox displaySchoolAdmin() {

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
        Button setting1 = new Button("Accept Student");
        Button setting2 = new Button("Remove Student");
        Button setting3 = new Button("Remove Teacher");
        Button setting4 = new Button("Add Teacher");

        sidebar.getChildren().addAll(setting1, setting2, setting3, setting4);
        setting1.getStyleClass().addAll("user-button", "user-button1");
        setting2.getStyleClass().add("user-button-delete");
        setting3.getStyleClass().add("user-button-delete");
        setting4.getStyleClass().add("user-button");

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

    public VBox displayTeacherAdmin() {

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
        Button setting1 = new Button("Accept Student");
        Button setting2 = new Button("Remove Student");
        Button setting3 = new Button("Add Homework");
        Button setting4 = new Button("Add Booking");
        Button setting5 = new Button("Add Exam");

        sidebar.getChildren().addAll(setting1, setting2, setting3, setting4, setting5);
        setting1.getStyleClass().addAll("user-button", "user-button1");
        setting2.getStyleClass().add("user-button-delete");
        setting3.getStyleClass().add("user-button");
        setting4.getStyleClass().add("user-button");
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

    public VBox displayStudentBookings() {

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
        Button setting1 = new Button("Booking Requests");
        Button setting2 = new Button("Cancel Booking");
        Button setting3 = new Button("Tips");

        sidebar.getChildren().addAll(setting1, setting2, setting3);
        setting1.getStyleClass().addAll("user-button", "user-button1");
        setting2.getStyleClass().add("user-button-delete");
        setting3.getStyleClass().add("user-button");

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
            messageArea.getChildren().clear();
        });

        setting2.setOnAction(e -> {
            messageArea.getChildren().clear();
        });

        setting3.setOnAction(e -> {
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
        String date = getDate();
        String time = getTime();

        Label dateLabel = new Label(date);
        Label timeLabel = new Label(time);
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

        cancelButton.setOnAction(e -> {
            System.out.println("Work1");
            System.out.println("Work2");
        });

        // Hover
        Animation.addHoverScaleEffect(cancelButton);

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

    public HBox displayMyBookingMenu() {

        // FRAME MOD HØJRE
        HBox mainContainer = new HBox(0);
        mainContainer.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        mainContainer.setPrefWidth(800);
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.setLayoutY(0);

        HBox topMenuHBox = new HBox(10);
        topMenuHBox.setPadding(new Insets(10, 10, 10, 10));
        topMenuHBox.setPrefWidth(800);
        topMenuHBox.setPrefHeight(50);
        topMenuHBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: rgb(0, 0, 0); -fx-background-color: #575757");

        // Label
        Label availableLabel = new Label("Active Bookings");
        availableLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px");

        // VBox for title
        VBox availableSection = new VBox();
        availableSection.setPrefWidth(200);
        availableSection.setPadding(new Insets(0,0,0,0));
        availableSection.setAlignment(Pos.TOP_LEFT);
        availableSection.getChildren().add(availableLabel);

        topMenuHBox.getChildren().add(availableSection);

        mainContainer.getChildren().add(topMenuHBox);

        return mainContainer;
    }

    // ____________________________________________________

    public HBox displayMyLastBookingMenu() {

        // FRAME MOD HØJRE
        HBox mainContainer = new HBox(0);
        mainContainer.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        mainContainer.setPrefWidth(800);
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.setLayoutY(300);

        // TOP MENU MOD HØJRE MED EN FIXED HØJDE // HOLDER ALLE ELEMENTER VENSTRE -------------> HØJRE
        HBox topMenuHBox = new HBox(0);
        topMenuHBox.setPadding(new Insets(10, 10, 10, 10));
        topMenuHBox.setPrefWidth(800);
        topMenuHBox.setPrefHeight(50);
        topMenuHBox.setStyle("-fx-border-width: 2px 0 2px 0; -fx-border-color: rgb(0, 0, 0); -fx-background-color: #575757");

        // Label
        Label availableLabel = new Label("Recent Bookings");
        availableLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px");

        // VBox for title
        VBox availableSection = new VBox();
        availableSection.setPrefWidth(200);
        availableSection.setPadding(new Insets(0,0,0,0));
        availableSection.setAlignment(Pos.TOP_LEFT);
        availableSection.getChildren().add(availableLabel);

        topMenuHBox.getChildren().add(availableSection);

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
        HBox topMenuHBox = new HBox(0);
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
        availableSection.setAlignment(Pos.TOP_LEFT);
        availableSection.getChildren().add(availableLabel);

        topMenuHBox.getChildren().add(availableSection);

        mainContainer.getChildren().add(topMenuHBox);

        return mainContainer;
    }

    // ____________________________________________________

    private Rectangle createOverlay(double width, double height) {
        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.rgb(0, 0, 0, 0.5));
        overlay.setVisible(false);  // initially hidden
        return overlay;
    }

    // ____________________________________________________

    private VBox createPopupBox() {

        VBox popup = new VBox(20);
        popup.setPrefSize(500, 500);
        popup.setAlignment(Pos.CENTER);
        popup.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px; -fx-padding: 20;");
        popup.setVisible(false);

        Label info = new Label("Booking details here...");
        Button closeBtn = new Button("Go Back");

        popup.getChildren().addAll(info, closeBtn);

        return popup;
    }

    // ____________________________________________________

    public HBox displayBookingCard() {
        HBox cardContainer = new HBox(0);
        cardContainer.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        cardContainer.setPrefWidth(800);
        cardContainer.setAlignment(Pos.TOP_LEFT);
        cardContainer.setLayoutY(57);

        HBox cardBox = new HBox();
        cardBox.setPrefWidth(800);
        cardBox.setPrefHeight(250);

        List<BookingCard> bookings = Main.db.getBookings(false);

        for (BookingCard booking : bookings) {
            String dateStr = booking.getDate();
            String timeStr = booking.getTime();
            String addressStr = booking.getAddress();
            int ratingInt = 3; //booking.getReview(); // TODO: not set up
            String hairTypeStr = Main.db.getHairTypeSummary(booking.getHairtypeId()); // e.g., "Curly, Brown, Medium, Female"

            Label date = new Label(dateStr);
            Text place = new Text(addressStr + "\n" + hairTypeStr);
            place.setTextAlignment(TextAlignment.CENTER);
            place.setWrappingWidth(140);
            place.setFill(Color.WHITE);

            Label time = new Label(timeStr);
            Label rating = new Label(convertToStars(ratingInt));

            time.setPadding(new Insets(30, 0, 0, 0));
            rating.setPadding(new Insets(0, 0, 30, 0));

            StackPane headerWithWave = new StackPane();
            headerWithWave.setPrefHeight(50);
            headerWithWave.setPrefWidth(160);
            headerWithWave.getStyleClass().add("card-header-visuals");

            SVGPath wave = new SVGPath();
            wave.setContent("M0 30 C26 10, 26 50, 52 30 C78 10, 78 50, 104 30 C130 10, 130 50, 158 30 V50 H0 Z");
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
            dropShadow.setRadius(5);
            dropShadow.setOffsetX(0);
            dropShadow.setOffsetY(-2);
            wave.setEffect(dropShadow);
            wave.setFill(Color.web("#ffa200"));
            wave.setScaleY(-1);
            wave.setTranslateY(30);

            date.getStyleClass().add("card-visuals-header");
            StackPane.setAlignment(date, Pos.TOP_CENTER);
            date.setPadding(new Insets(15, 0, 0, 0));
            headerWithWave.getChildren().addAll(wave, date);
            wave.toBack();

            // Apply CSS
            place.getStyleClass().add("card-visuals-lol");
            time.getStyleClass().add("card-visuals-bold");
            rating.getStyleClass().add("card-visuals-rating");

            VBox card = new VBox(10);
            card.setPrefWidth(160);
            card.setMinWidth(160);
            card.setMaxWidth(160);
            card.setPrefHeight(248);
            card.setAlignment(Pos.TOP_CENTER);
            card.getStyleClass().add("card-background-visuals");

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            String p = "500kr";

            card.setOnMouseClicked(event -> {
                DialogBox.displayBook(dateStr,p, timeStr, convertToStars(4));
            });

            Animation.addHoverScaleEffectVBox(card);

            card.getChildren().addAll(headerWithWave, time, place, spacer, rating);
            cardBox.getChildren().add(card);
        }

        cardContainer.getChildren().add(cardBox);
        return cardContainer;
    }

    // ______________________________________________________

    public HBox displayBookingCardStudent(int student_id) {
        HBox cardContainer = new HBox(0);
        cardContainer.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        cardContainer.setPrefWidth(800);
        cardContainer.setAlignment(Pos.TOP_LEFT);
        cardContainer.setLayoutY(57);

        HBox cardBox = new HBox();
        cardBox.setPrefWidth(800);
        cardBox.setPrefHeight(250);

        List<BookingCard> bookings = Main.db.getBookings(Main.db.getUserID(username), false);

        for (BookingCard booking : bookings) {
            String dateStr = booking.getDate();
            String timeStr = booking.getTime();
            String addressStr = booking.getAddress();
            int ratingInt = booking.getReview(); // 0 if you haven't set up yet
            String hairTypeStr = Main.db.getHairTypeSummary(booking.getHairtypeId()); // e.g., "Curly, Brown, Medium, Female"

            Label date = new Label(dateStr);
            Text place = new Text(addressStr + "\n" + hairTypeStr);
            place.setTextAlignment(TextAlignment.CENTER);
            place.setWrappingWidth(140);
            place.setFill(Color.WHITE);

            Label time = new Label(timeStr);
            Label rating = new Label(convertToStars(ratingInt)); // Optional

            time.setPadding(new Insets(30, 0, 0, 0));
            rating.setPadding(new Insets(0, 0, 30, 0));

            StackPane headerWithWave = new StackPane();
            headerWithWave.setPrefHeight(50);
            headerWithWave.setPrefWidth(160);
            headerWithWave.getStyleClass().add("card-header-visuals");

            SVGPath wave = new SVGPath();
            wave.setContent("M0 30 C26 10, 26 50, 52 30 C78 10, 78 50, 104 30 C130 10, 130 50, 158 30 V50 H0 Z");
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
            dropShadow.setRadius(5);
            dropShadow.setOffsetX(0);
            dropShadow.setOffsetY(-2);
            wave.setEffect(dropShadow);
            wave.setFill(Color.web("#ffa200"));
            wave.setScaleY(-1);
            wave.setTranslateY(30);

            date.getStyleClass().add("card-visuals-header");
            StackPane.setAlignment(date, Pos.TOP_CENTER);
            date.setPadding(new Insets(15, 0, 0, 0));
            headerWithWave.getChildren().addAll(wave, date);
            wave.toBack();

            // Apply CSS
            place.getStyleClass().add("card-visuals-lol");
            time.getStyleClass().add("card-visuals-bold");
            rating.getStyleClass().add("card-visuals-rating");

            VBox card = new VBox(10);
            card.setPrefWidth(160);
            card.setMinWidth(160);
            card.setMaxWidth(160);
            card.setPrefHeight(248);
            card.setAlignment(Pos.TOP_CENTER);
            card.getStyleClass().add("card-background-visuals");

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            Animation.addHoverScaleEffectVBox(card);

            card.getChildren().addAll(headerWithWave, time, place, spacer, rating);
            cardBox.getChildren().add(card);
        }

        cardContainer.getChildren().add(cardBox);
        return cardContainer;
    }

    // ____________________________________________________

    //TEMP, for the hardcoded version!!!!
    private String[] randomBooking() {
        Random random = new Random();
        LocalDate randomDate = LocalDate.now().plusDays(random.nextInt(30));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM");
        String date = randomDate.format(dateFormatter);

        int hour = 6 + random.nextInt(11);
        int minute = random.nextBoolean() ? 0 : 30;
        String time = String.format("%02d:%02d", hour, minute);

        String[] addresses = {
                "Hillerød 3400\nNarkovej 69",
                "Adresse 2\nNarkovej 420",
                "Adresse 3\nNarkovej 5",
                "Adresse 4\nNarkovej 3",
                "Adresse 1\nNarkovej 2",
        };
        String address = addresses[random.nextInt(addresses.length)];

        return new String[] { date, address, time };
    }

    // ____________________________________________________

    public HBox displayMyBookingCard() {

        // Frame towards right
        HBox cardContainer = new HBox(0);
        cardContainer.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        cardContainer.setPrefWidth(800);
        cardContainer.setAlignment(Pos.TOP_LEFT);
        cardContainer.setLayoutY(52); // height + border (2px)

        // cardBoxMenu (under menu)
        HBox cardBox = new HBox();
        cardBox.setPrefWidth(800);
        cardBox.setPrefHeight(250); // 50 + 50 = 500 / 2

        for (int i = 0; i < 1; i++) {

            // Labels
            String d = "26.03";
            String t = "15:30";
            Label date = new Label(d);
            Text place = new Text("Hillerød 3400\nNarkovej 69");
            place.setTextAlignment(TextAlignment.CENTER);
            place.setWrappingWidth(140);
            place.setFill(Color.WHITE);
            Label time = new Label(t);

            time.setPadding(new Insets(30, 0, 0, 0));

            StackPane headerWithWave = new StackPane();
            headerWithWave.setPrefHeight(50);
            headerWithWave.setPrefWidth(160);
            headerWithWave.getStyleClass().add("card-header-visuals");

            SVGPath wave = new SVGPath();
            wave.setContent(
                    "M0 30 " +
                    "C26 10, 26 50, 52 30 " +
                    "C78 10, 78 50, 104 30 " +
                    "C130 10, 130 50, 158 30 " +
                    "V50 H0 Z"
            );

            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.rgb(0, 0, 0, 0.3));
            dropShadow.setRadius(5);
            dropShadow.setOffsetX(0);
            dropShadow.setOffsetY(-2);

            wave.setEffect(dropShadow);
            wave.setFill(Color.web("#ffa200"));
            wave.setScaleY(-1);
            wave.setTranslateY(30);

            date.getStyleClass().add("card-visuals-header");
            StackPane.setAlignment(date, Pos.TOP_CENTER);
            date.setPadding(new Insets(15, 0, 0, 0));

            headerWithWave.getChildren().addAll(wave, date);
            wave.toBack();

            // CSS
            date.getStyleClass().add("card-visuals-header");
            place.getStyleClass().add("card-visuals-lol");
            time.getStyleClass().add("card-visuals-bold");

            // Buttons container
            HBox buttonsBox = new HBox(10);
            buttonsBox.setAlignment(Pos.CENTER);
            buttonsBox.setPadding(new Insets(0, 0, 20, 0));

            Button cancelButton = new Button("Cancel");
            cancelButton.getStyleClass().add("cancel-button");
            cancelButton.setPrefWidth(90);
            cancelButton.setPrefHeight(20);

            cancelButton.setOnAction(e -> {
                DialogBox.displayAlert(d, d);
            });

            Button payButton = new Button("Pay");
            payButton.getStyleClass().add("pay-button");
            payButton.setPrefWidth(50);
            payButton.setPrefHeight(28);

            String p = "500dkk";

            payButton.setOnAction(e -> {
                DialogBox.displayPay(d, t, p);
            });

            // Margin
            HBox.setMargin(payButton, new Insets(0, 10, 0, 0));
            HBox.setMargin(cancelButton, new Insets(0, 0, 0, 10));

            buttonsBox.getChildren().addAll(cancelButton, payButton);

            // Card display
            VBox card = new VBox(10);
            card.setPrefWidth(160);
            card.setPrefHeight(248);
            card.setAlignment(Pos.TOP_CENTER);
            card.getStyleClass().add("card-background-visuals");

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            // Hover effect on card :hover
            Animation.addHoverScaleEffectMore(cancelButton);
            Animation.addHoverScaleEffectMore(payButton);

            // Add labels and buttons to card
            card.getChildren().addAll(headerWithWave, time, place, spacer, buttonsBox);

            // Add card to CardBox
            cardBox.getChildren().add(card);
        }

        // Add cardContainer to final HBox
        cardContainer.getChildren().add(cardBox);

        return cardContainer;
    }

    // ____________________________________________________

    public HBox displayExamCard() {

        HBox cardContainer = new HBox(0);
        cardContainer.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        cardContainer.setPrefWidth(800);
        cardContainer.setAlignment(Pos.TOP_LEFT);
        cardContainer.setLayoutY(354);

        HBox cardBox = new HBox();
        cardBox.setPrefWidth(800);
        cardBox.setPrefHeight(250);

        List<BookingCard> bookings = Main.db.getBookings(true);

        for (BookingCard booking : bookings) {

            String dateStr = booking.getDate();
            String timeStr = booking.getTime();
            String addressStr = booking.getAddress();
            int ratingInt = 3; //booking.getReview(); // TODO: not set up
            String hairTypeStr = Main.db.getHairTypeSummary(booking.getHairtypeId()); // e.g., "Curly, Brown, Medium, Female"

            Label date = new Label(dateStr);
            Text place = new Text(addressStr + "\n" + hairTypeStr);
            place.setTextAlignment(TextAlignment.CENTER);
            place.setWrappingWidth(140);
            place.setFill(Color.WHITE);

            Label time = new Label(timeStr);
            Label rating = new Label(convertToStars(ratingInt));

            time.setPadding(new Insets(30, 0, 0, 0));
            rating.setPadding(new Insets(0, 0, 30, 0));

            StackPane headerWithWave = new StackPane();
            headerWithWave.setPrefHeight(50);
            headerWithWave.setPrefWidth(160);
            headerWithWave.getStyleClass().add("card-header-visuals");

            SVGPath wave = new SVGPath();
            wave.setContent("M0 30 C26 10, 26 50, 52 30 C78 10, 78 50, 104 30 C130 10, 130 50, 158 30 V50 H0 Z");
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
            dropShadow.setRadius(5);
            dropShadow.setOffsetX(0);
            dropShadow.setOffsetY(-2);
            wave.setEffect(dropShadow);
            wave.setFill(Color.web("#ffa200"));
            wave.setScaleY(-1);
            wave.setTranslateY(30);

            date.getStyleClass().add("card-visuals-header");
            StackPane.setAlignment(date, Pos.TOP_CENTER);
            date.setPadding(new Insets(15, 0, 0, 0));
            headerWithWave.getChildren().addAll(wave, date);
            wave.toBack();

            // Apply CSS
            place.getStyleClass().add("card-visuals-lol");
            time.getStyleClass().add("card-visuals-bold");
            rating.getStyleClass().add("card-visuals-rating");

            VBox card = new VBox(10);
            card.setPrefWidth(160);
            card.setMinWidth(160);
            card.setMaxWidth(160);
            card.setPrefHeight(248);
            card.setAlignment(Pos.TOP_CENTER);
            card.getStyleClass().add("card-background-visuals");

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            String p = "500kr";

            card.setOnMouseClicked(event -> {
                DialogBox.displayBook(dateStr,p, timeStr, convertToStars(4));
            });

            Animation.addHoverScaleEffectVBox(card);

            card.getChildren().addAll(headerWithWave, time, place, spacer, rating);
            cardBox.getChildren().add(card);
        }

        cardContainer.getChildren().add(cardBox);
        return cardContainer;
    }

    // ____________________________________________________

    public HBox displayExamCardStudent() {

        HBox cardContainer = new HBox(0);
        cardContainer.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        cardContainer.setPrefWidth(800);
        cardContainer.setAlignment(Pos.TOP_LEFT);
        cardContainer.setLayoutY(354);

        HBox cardBox = new HBox();
        cardBox.setPrefWidth(800);
        cardBox.setPrefHeight(250);

        List<BookingCard> bookings = Main.db.getBookings(true);

        for (BookingCard booking : bookings) {
            String dateStr = booking.getDate();
            String timeStr = booking.getTime();
            String addressStr = booking.getAddress();
            int ratingInt = 3; //booking.getReview(); // TODO: not set up
            String hairTypeStr = Main.db.getHairTypeSummary(booking.getHairtypeId()); // e.g., "Curly, Brown, Medium, Female"

            Label date = new Label(dateStr);
            Text place = new Text(addressStr + "\n" + hairTypeStr);
            place.setTextAlignment(TextAlignment.CENTER);
            place.setWrappingWidth(140);
            place.setFill(Color.WHITE);

            Label time = new Label(timeStr);
            Label rating = new Label(convertToStars(ratingInt));

            time.setPadding(new Insets(30, 0, 0, 0));
            rating.setPadding(new Insets(0, 0, 30, 0));

            StackPane headerWithWave = new StackPane();
            headerWithWave.setPrefHeight(50);
            headerWithWave.setPrefWidth(160);
            headerWithWave.getStyleClass().add("card-header-visuals");

            SVGPath wave = new SVGPath();
            wave.setContent("M0 30 C26 10, 26 50, 52 30 C78 10, 78 50, 104 30 C130 10, 130 50, 158 30 V50 H0 Z");
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
            dropShadow.setRadius(5);
            dropShadow.setOffsetX(0);
            dropShadow.setOffsetY(-2);
            wave.setEffect(dropShadow);
            wave.setFill(Color.web("#ffa200"));
            wave.setScaleY(-1);
            wave.setTranslateY(30);

            date.getStyleClass().add("card-visuals-header");
            StackPane.setAlignment(date, Pos.TOP_CENTER);
            date.setPadding(new Insets(15, 0, 0, 0));
            headerWithWave.getChildren().addAll(wave, date);
            wave.toBack();

            // Apply CSS
            place.getStyleClass().add("card-visuals-lol");
            time.getStyleClass().add("card-visuals-bold");
            rating.getStyleClass().add("card-visuals-rating");

            VBox card = new VBox(10);
            card.setPrefWidth(160);
            card.setMinWidth(160);
            card.setMaxWidth(160);
            card.setPrefHeight(248);
            card.setAlignment(Pos.TOP_CENTER);
            card.getStyleClass().add("card-background-visuals");

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            Animation.addHoverScaleEffectVBox(card);

            card.getChildren().addAll(headerWithWave, time, place, spacer, rating);
            cardBox.getChildren().add(card);
        }

        cardContainer.getChildren().add(cardBox);
        return cardContainer;
    }

    // ____________________________________________________

    public HBox displayMyLastBookingCard() {

        // HBox
        HBox cardContainer = new HBox(0);
        cardContainer.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        cardContainer.setPrefWidth(800);
        cardContainer.setAlignment(Pos.TOP_LEFT);
        cardContainer.setLayoutY(354);

        // cardBoxMenu
        HBox cardBox = new HBox();
        cardBox.setPrefWidth(800);
        cardBox.setPrefHeight(250); // 50 + 50 = 500 / 2

        for (int i = 0; i < 5; i++) {

            String d = "26.03";
            String t = "15:00";

            // Labels
            Label date = new Label(d);
            Label brand = new Label("Frisør Narko");

            brand.setPadding(new Insets(60, 0, 0, 0));

            StackPane headerWithWave = new StackPane();
            headerWithWave.setPrefHeight(50);
            headerWithWave.setPrefWidth(160);
            headerWithWave.setStyle("-fx-background-color: grey;");

            SVGPath wave = new SVGPath();
            wave.setContent(
                    "M0 30 " +
                    "C26 10, 26 50, 52 30 " +
                    "C78 10, 78 50, 104 30 " +
                    "C130 10, 130 50, 158 30 " +
                    "V50 H0 Z"
            );

            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
            dropShadow.setRadius(5);
            dropShadow.setOffsetX(0);
            dropShadow.setOffsetY(-2);

            wave.setEffect(dropShadow);
            wave.setFill(Color.web("#808080FF"));
            wave.setScaleY(-1);
            wave.setTranslateY(30);

            date.getStyleClass().add("card-visuals-header");
            StackPane.setAlignment(date, Pos.TOP_CENTER);
            date.setPadding(new Insets(15, 0, 0, 0));

            headerWithWave.getChildren().addAll(wave, date);
            wave.toBack();

            // CSS
            date.getStyleClass().add("card-visuals-header");
            brand.getStyleClass().add("card-visuals-bold");

            // "Tip" button
            Button tipButton = new Button("Tip");
            tipButton.setPrefWidth(80);
            tipButton.getStyleClass().add("tip-button");
            tipButton.setPadding(new Insets(5, 10, 5, 10));
            VBox.setMargin(tipButton, new Insets(0, 0, 20, 0));

            tipButton.setOnAction(e -> DialogBox.displayTip(d, t));

            // TipButton :hover
            Animation.addHoverScaleEffect(tipButton);

            // Card display
            VBox card = new VBox(10);
            card.setPrefWidth(160);
            card.setPrefHeight(248);
            card.setAlignment(Pos.TOP_CENTER);
            card.setStyle("-fx-background-color: #afafaf; -fx-border-color: rgba(0,0,0,0.7);");
            card.getStyleClass().add("card-disabled");

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            // Add
            card.getChildren().addAll(headerWithWave, brand, spacer, tipButton);

            // Add
            cardBox.getChildren().add(card);
        }

        // Add
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

    public VBox displayWelcome(){
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));

        Label header = new Label("Welcome, " + this.username + "!");
        header.setStyle(
        "-fx-background-color: #4a4a4a;" +
        "-fx-text-fill: white;" +
        "-fx-font-size: 18px;" +
        "-fx-font-weight: bold;" +
        "-fx-padding: 10 20 10 20;" +
        "-fx-background-radius: 15;"
        );
        header.setAlignment(Pos.CENTER_LEFT);
        header.setMaxWidth(Region.USE_COMPUTED_SIZE);

        HBox contentRow = new HBox(20);

        VBox textBox = new VBox();
        textBox.setPrefSize(500, 450);
        textBox.setStyle("-fx-background-color: rgb(80,80,80); -fx-padding: 20; -fx-background-radius: 25px; -fx-border-radius: 25px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 2, 0.5, 1, 1);");

        Label headerLabel = new Label(getMessage("src/main/java/constants/welcomeHeader.txt"));
        headerLabel.setWrapText(true);
        headerLabel.setMaxWidth(460);
        headerLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: orange; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 2, 0.5, 1, 1);");
        headerLabel.setPadding(new Insets(0,0,15,0));

        Label endContent = new Label(getMessage("src/main/java/constants/welcomeEnd.txt"));
        endContent.setWrapText(true);
        endContent.setMaxWidth(460);
        endContent.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        endContent.setPadding(new Insets(140,0,0,0));

        Label content = new Label(getMessage("src/main/java/constants/welcomeMessage.txt"));
        content.setWrapText(true);
        content.setMaxWidth(460);
        content.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        textBox.getChildren().addAll(headerLabel, content, endContent);

        VBox imageBoxes = new VBox(20);
        imageBoxes.setPrefWidth(200);

        for (int i = 0; i < 3; i++) {
            String path = "/assets/school/" + (i + 1) + ".png";
            Image image = new Image(Objects.requireNonNull(getClass().getResource(path), "Billedet findes ikke: " + path).toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);

            // Hover
            Animation.addHoverScaleEffectImage(imageView);

            // Bypass for rounded corners on Image
            Rectangle clip = new Rectangle(200, 150);
            clip.setArcWidth(25);
            clip.setArcHeight(25);

            // nigga

            imageView.setClip(clip);

            imageBoxes.getChildren().add(imageView);
        }

        contentRow.getChildren().addAll(textBox, imageBoxes);
        container.getChildren().addAll(header, contentRow);

        return container;
    }

    // ____________________________________________________

    public String getMessage(String path) {
        String message = "";
        File file = new File(path);
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                message += scan.nextLine() + "\n";}
        }
        catch (Exception e) {
            System.out.println("File not found?!?!");
        }

        return message;
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
        leftPane.setPadding(new Insets(0,0,0,0));

        Image profileImage = new Image(getClass().getResource("/assets/profile/person1.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(150);
        profileImageView.setFitHeight(130);
        profileImageView.setPreserveRatio(true);

        Label roleLabel = new Label("Customer");
        roleLabel.setPadding(new Insets(5,0,5,0));

        VBox leftPaneInfo = new VBox();
        leftPaneInfo.setAlignment(Pos.CENTER);
        leftPaneInfo.getChildren().add(roleLabel);
        leftPaneInfo.setPrefWidth(190);

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
        HBox contactBox = createNavBox("Contact");
        HBox reviewsBox = createNavBox("Cut Reviews");
        HBox galleryBox = createNavBox("Service Reviews");

        navRow.getChildren().addAll(aboutMeBox, reviewsBox, contactBox, galleryBox);

        VBox rightPaneContent = new VBox(10);
        rightPaneContent.setPrefWidth(550);
        rightPaneContent.setAlignment(Pos.TOP_CENTER);

        // Actions
        contactBox.setOnMouseClicked(e -> {
            contactBox.getStyleClass().add("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayContactMe());
        });

        aboutMeBox.setOnMouseClicked(e -> {
            contactBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            aboutMeBox.getStyleClass().add("nav-box-selected-first");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayAboutMe());
        });

        reviewsBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            contactBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayReview());
        });

        galleryBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            contactBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayPictures());
        });

        // CSS
        leftPane.getStyleClass().add("left-pane");
        leftPaneInfo.getStyleClass().add("left-pane-info");
        contactBox.getStyleClass().add("about-me-text-area");
        rightPane.getStyleClass().add("right-pane");

        // Default Selected Box
        aboutMeBox.getStyleClass().add("nav-box-selected-first");

        // Add
        rightPane.getChildren().addAll(rightPaneBanner, navRow, rightPaneContent);
        profilePictureHBox.getChildren().addAll(leftPane, rightPane);
        profileVBox.getChildren().addAll(profilePictureHBox, bottomContentBox);

        // Load about me by default
        bottomContentBox.getChildren().add(displayAboutMe());

        return profileVBox;
    }

    // ____________________________________________________

    public VBox displayProfileStudent() {

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
            getChildren().clear();
            getChildren().add(displayAvailableBookings());
            getChildren().add(displayBookingCard());
            getChildren().add(displayExamMenu());
            getChildren().add(displayExamCard());
        });

        aboutMeBox.setOnMouseClicked(e -> {
            availableBookingsBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            aboutMeBox.getStyleClass().add("nav-box-selected-first");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayAboutMe());
        });

        reviewsBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            availableBookingsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayReview());
        });

        galleryBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            availableBookingsBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayPictures());
        });

        // CSS
        leftPane.getStyleClass().add("left-pane");
        leftPaneInfo.getStyleClass().add("left-pane-info");
        rightPane.getStyleClass().add("right-pane");

        // Default Selected Box
        aboutMeBox.getStyleClass().add("nav-box-selected-first");

        // Add
        rightPane.getChildren().addAll(rightPaneBanner, navRow, rightPaneContent);
        profilePictureHBox.getChildren().addAll(leftPane, rightPane);
        profileVBox.getChildren().addAll(profilePictureHBox, bottomContentBox);

        // Load about me by default
        bottomContentBox.getChildren().add(displayAboutMe());

        return profileVBox;
    }

    // ____________________________________________________

    public VBox displayProfileSchool() {

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

        Label roleLabel = new Label("School");
        roleLabel.setStyle("-fx-padding: 5;");

        VBox leftPaneInfo = new VBox();
        leftPaneInfo.setAlignment(Pos.CENTER);
        leftPaneInfo.getChildren().add(roleLabel);

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

        HBox aboutMeBox = createNavBox("F&Q");
        HBox contactBox = createNavBox("Contact");
        HBox reviewsBox = createNavBox("Students");
        HBox galleryBox = createNavBox("Reviews of us");

        navRow.getChildren().addAll(aboutMeBox, reviewsBox, contactBox, galleryBox);

        VBox rightPaneContent = new VBox(10);
        rightPaneContent.setPrefWidth(550);
        rightPaneContent.setAlignment(Pos.TOP_CENTER);

        // Actions
        contactBox.setOnMouseClicked(e -> {
            getChildren().clear();
            getChildren().add(displayAvailableBookings());
            getChildren().add(displayBookingCard());
            getChildren().add(displayExamMenu());
            getChildren().add(displayExamCard());
        });

        aboutMeBox.setOnMouseClicked(e -> {
            contactBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            aboutMeBox.getStyleClass().add("nav-box-selected-first");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayAboutMe());
        });

        reviewsBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            contactBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayReview());
        });

        galleryBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            contactBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayPictures());
        });

        // CSS
        leftPane.getStyleClass().add("left-pane");
        leftPaneInfo.getStyleClass().add("left-pane-info");
        contactBox.getStyleClass().add("about-me-text-area");
        rightPane.getStyleClass().add("right-pane");

        // Default Selected Box
        aboutMeBox.getStyleClass().add("nav-box-selected-first");

        // Add
        rightPane.getChildren().addAll(rightPaneBanner, navRow, rightPaneContent);
        profilePictureHBox.getChildren().addAll(leftPane, rightPane);
        profileVBox.getChildren().addAll(profilePictureHBox, bottomContentBox);

        // Load about me by default
        bottomContentBox.getChildren().add(displayAboutMe());

        return profileVBox;
    }

    // ____________________________________________________

    public VBox displayProfileTeacher() {

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

        Label roleLabel = new Label("Teacher");
        roleLabel.setStyle("-fx-padding: 5;");

        VBox leftPaneInfo = new VBox();
        leftPaneInfo.setAlignment(Pos.CENTER);
        leftPaneInfo.getChildren().add(roleLabel);

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

        HBox aboutMeBox = createNavBox("F&Q");
        HBox contactBox = createNavBox("Contact");
        HBox reviewsBox = createNavBox("Students");
        HBox galleryBox = createNavBox("Reviews of us");

        navRow.getChildren().addAll(aboutMeBox, reviewsBox, contactBox, galleryBox);

        VBox rightPaneContent = new VBox(10);
        rightPaneContent.setPrefWidth(550);
        rightPaneContent.setAlignment(Pos.TOP_CENTER);

        // Actions
        contactBox.setOnMouseClicked(e -> {
            getChildren().clear();
            getChildren().add(displayAvailableBookings());
            getChildren().add(displayBookingCard());
            getChildren().add(displayExamMenu());
            getChildren().add(displayExamCard());
        });

        aboutMeBox.setOnMouseClicked(e -> {
            contactBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            aboutMeBox.getStyleClass().add("nav-box-selected-first");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayAboutMe());
        });

        reviewsBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            contactBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayReview());
        });

        galleryBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            contactBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayPictures());
        });

        // CSS
        leftPane.getStyleClass().add("left-pane");
        leftPaneInfo.getStyleClass().add("left-pane-info");
        contactBox.getStyleClass().add("about-me-text-area");
        rightPane.getStyleClass().add("right-pane");

        // Default Selected Box
        aboutMeBox.getStyleClass().add("nav-box-selected-first");

        // Add
        rightPane.getChildren().addAll(rightPaneBanner, navRow, rightPaneContent);
        profilePictureHBox.getChildren().addAll(leftPane, rightPane);
        profileVBox.getChildren().addAll(profilePictureHBox, bottomContentBox);

        // Load about me by default
        bottomContentBox.getChildren().add(displayAboutMe());

        return profileVBox;
    }

    // ____________________________________________________

    public VBox displayProfileSupport() {

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

        Label roleLabel = new Label("Support");
        roleLabel.setStyle("-fx-padding: 5;");

        VBox leftPaneInfo = new VBox();
        leftPaneInfo.setAlignment(Pos.CENTER);
        leftPaneInfo.getChildren().add(roleLabel);

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
        HBox contactBox = createNavBox("Stats");
        HBox reviewsBox = createNavBox("F&Q");
        HBox galleryBox = createNavBox("Message");

        navRow.getChildren().addAll(aboutMeBox, reviewsBox, contactBox, galleryBox);

        VBox rightPaneContent = new VBox(10);
        rightPaneContent.setPrefWidth(550);
        rightPaneContent.setAlignment(Pos.TOP_CENTER);

        // Actions
        contactBox.setOnMouseClicked(e -> {
            getChildren().clear();
            getChildren().add(displayAvailableBookings());
            getChildren().add(displayBookingCard());
            getChildren().add(displayExamMenu());
            getChildren().add(displayExamCard());
        });

        aboutMeBox.setOnMouseClicked(e -> {
            contactBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            aboutMeBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayAboutMe());
        });

        reviewsBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected");
            contactBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayReview());
        });

        galleryBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected");
            contactBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
            bottomContentBox.getChildren().add(displayPictures());
        });

        // CSS
        leftPane.getStyleClass().add("left-pane");
        leftPaneInfo.getStyleClass().add("left-pane-info");
        contactBox.getStyleClass().add("about-me-text-area");
        rightPane.getStyleClass().add("right-pane");

        // Default Selected Box
        aboutMeBox.getStyleClass().add("nav-box-selected");

        // Add
        rightPane.getChildren().addAll(rightPaneBanner, navRow, rightPaneContent);
        profilePictureHBox.getChildren().addAll(leftPane, rightPane);
        profileVBox.getChildren().addAll(profilePictureHBox, bottomContentBox);

        // Load about me by default
        bottomContentBox.getChildren().add(displayAboutMe());

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
        leftBox.setStyle("-fx-background-color: #575757; -fx-border-radius: 0 0 0 20px; -fx-background-radius: 0 0 0 20px; -fx-border-width: 0 2px 0 0; -fx-border-color: rgb(0,0,0); -fx-padding: 20px 0 0 0");
        leftBox.setPrefWidth(190);
        leftBox.setAlignment(Pos.TOP_CENTER);

        // VBox inside HBox (leftpane)
        VBox infoSections = new VBox(10);
        infoSections.setAlignment(Pos.TOP_CENTER);

        Label starsLabel = new Label(convertToStars(4));
        starsLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: orange;");

        Label lastOnlineLabel = new Label("Last Online:");
        Label lastOnlineValue = new Label("2 hours ago");

        // Person info
        Label nameLabel = new Label(this.username);
        nameLabel.setStyle("-fx-text-fill: orange; -fx-font-size: 18px;");

        // Socials
        Label instagramLabel = new Label("Instagram");
        Label facebookLabel = new Label("Facebook");
        Label githubLabel = new Label("Github");

        List<VBox> socialButtons = new ArrayList<>();

        VBox instagramButton = createSelectableSocialButton(instagramLabel, socialButtons);
        VBox facebookButton = createSelectableSocialButton(facebookLabel, socialButtons);
        VBox githubButton = createSelectableSocialButton(githubLabel, socialButtons);

        socialButtons.addAll(List.of(instagramButton, facebookButton, githubButton));

        VBox socialsBox = new VBox(0,nameLabel, instagramButton, facebookButton, githubButton);
        socialsBox.setAlignment(Pos.CENTER);

        Label cityLabel = new Label("Hillerød");
        cityLabel.setPadding(new Insets(15, 0, 0, 0));
        cityLabel.setStyle("-fx-text-fill: #FFF; -fx-font-size: 16px; " +
        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 2, 0.5, 1, 1);");

        VBox locationBox = new VBox(10, cityLabel);
        locationBox.setAlignment(Pos.CENTER);

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
        infoSections.getChildren().addAll(ratingBox, lastOnlineBox, socialsBox, locationBox);
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
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPadding(Insets.EMPTY);
        scrollPane.setStyle("-fx-background-color: transparent;");

        Label aboutHeader = new Label("Welcome stranger...");
        aboutHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: orange;");

        Label bioText = new Label("Hi, I'm " + username + ". I love cutting hair and making people happy. Would you like to get farted on? I bet you do you sicko. Book me today or regret it forever!!");
        bioText.setWrapText(true);
        bioText.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Label interestsHeader = new Label("Interests");
        interestsHeader.setStyle("-fx-font-size: 16px; -fx-text-fill: #ccc; -fx-padding: 10 0 5 0;");

        HBox tagsBox = new HBox(5);
        tagsBox.setPadding(new Insets(5, 0, 0, 0));
        tagsBox.setAlignment(Pos.CENTER_LEFT);

        String[] tags = {"Gym", "Creativity", "Gaming", "Hiking", "Farting"};

        for (String tag : tags) {
            Label tagLabel = new Label("#" + tag);
            tagLabel.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 4 8 4 8; -fx-background-radius: 10; -fx-font-size: 14px");
            tagsBox.getChildren().add(tagLabel);
        }

        Label funFactHeader = new Label("Fun Fact");
        funFactHeader.setStyle("-fx-font-size: 16px; -fx-text-fill: #ccc; -fx-padding: 10 0 5 0;");

        Label funFactText = new Label("A random fun fact that isnt fun at all..");
        funFactText.setWrapText(true);
        funFactText.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 13px;");

        VBox rightContent = new VBox(10, aboutHeader, bioText, interestsHeader, tagsBox, funFactHeader, funFactText);
        rightContent.setPadding(new Insets(20));
        rightContent.setStyle("-fx-background-color: #575757; -fx-background-radius: 0 0 20px 0; -fx-border-radius: 0 0 20px 0;");
        rightContent.setPrefWidth(560);
        rightContent.setPrefHeight(600);

        scrollPane.setContent(rightContent);
        rightBox.getChildren().add(scrollPane);

        splitBox.getChildren().addAll(leftBox, rightBox);
        mainContainer.getChildren().add(splitBox);

        return mainContainer;
    }

    // ____________________________________________________

    private VBox createSelectableSocialButton(Label label, List<VBox> allButtons) {
        VBox container = new VBox(label);
        container.getStyleClass().add("socials-button");
        container.setAlignment(Pos.CENTER);
        container.setCursor(Cursor.HAND);
        container.setPadding(new Insets(8, 16, 8, 16));
        container.setSpacing(0);

        container.setOnMouseClicked(e -> {
            for (VBox btn : allButtons) {
                btn.getStyleClass().remove("selected");
            }
            container.getStyleClass().add("selected");
        });

        return container;
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

    protected void darkmodeToggle(String darkmode) {

        if (darkmode.equalsIgnoreCase("Yes")) {
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

    // ____________________________________________________

    public VBox displayContactMe() {
        VBox mainContainer = new VBox();
        mainContainer.setPadding(Insets.EMPTY);
        mainContainer.setSpacing(0);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setStyle("-fx-background-color: #2d2d2d; -fx-background-radius: 0 0 20px 20px; -fx-border-radius: 0 0 20px 20px;");

        Label header = new Label("Contact Me");
        header.setStyle("-fx-font-size: 26px; -fx-text-fill: orange; -fx-font-weight: bold;");

        Label subHeader = new Label("I'm glad you're here.");
        subHeader.setStyle("-fx-font-size: 16px; -fx-text-fill: #cccccc;");

        Label bioText = new Label("Hi, I'm " + username + ". I love cutting hair and making people happy.\nNeed a fresh cut or just wanna talk? Reach out!");
        bioText.setWrapText(true);
        bioText.setTextAlignment(TextAlignment.CENTER);
        bioText.setAlignment(Pos.CENTER);
        bioText.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Label emailLabel = new Label("Email: fuckdig@fedme.dk");
        emailLabel.setStyle("-fx-text-fill: #fff; -fx-font-size: 14px;");
        emailLabel.setTextAlignment(TextAlignment.CENTER);

        Label phoneLabel = new Label("Phone: 12345678");
        phoneLabel.setStyle("-fx-text-fill: #fff; -fx-font-size: 14px;");
        phoneLabel.setTextAlignment(TextAlignment.CENTER);

        HBox socialLinks = new HBox(10);
        socialLinks.setAlignment(Pos.CENTER);

        String[] socials = {"Instagram", "Facebook", "GitHub"};
        for (String social : socials) {
            Label link = new Label(social);
            link.setStyle("-fx-background-color: #595959; -fx-text-fill: white; -fx-padding: 6 12 6 12; -fx-background-radius: 8; -fx-font-size: 13px; -fx-cursor: hand;");
            socialLinks.getChildren().add(link);
        }

        Button contactButton = new Button("Send a Message");
        contactButton.setStyle("-fx-background-color: orange;-fx-background-insets: 0; -fx-border-insets: 0; -fx-cursor: hand; -fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-border-radius: 15px;");
        contactButton.setPrefWidth(200);

        // Hover
        Animation.addHoverScaleEffectMore(contactButton);

        VBox contentBox = new VBox(15, header, subHeader, bioText, emailLabel, phoneLabel, socialLinks, contactButton);
        contentBox.setPadding(new Insets(30));
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPrefHeight(600);
        contentBox.setPrefWidth(Double.MAX_VALUE);
        contentBox.setStyle("-fx-background-color: #3b3b3b; -fx-background-radius: 0 0 20px 20px; -fx-border-radius: 0 0 20px 20px;");

        mainContainer.getChildren().add(contentBox);
        VBox.setVgrow(contentBox, Priority.ALWAYS);

        return mainContainer;
    }

    public HBox displayBookingsStudent() {
        //billede til knap
        Image plusIcon = new Image(getClass().getResource("/assets/icons/icon20.png").toExternalForm());


        ImageView plusView = new ImageView(plusIcon);
        plusView.setFitWidth(20);
        plusView.setFitHeight(20);

        // FRAME MOD HØJRE
        HBox mainContainer = new HBox(0);
        mainContainer.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        mainContainer.setPrefWidth(800);
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.setLayoutY(0);

        HBox topMenuHBox = new HBox(10);
        topMenuHBox.setPadding(new Insets(10, 10, 10, 10));
        topMenuHBox.setPrefWidth(800);
        topMenuHBox.setPrefHeight(50);
        topMenuHBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: rgb(0, 0, 0); -fx-background-color: #575757");

        // Label
        Label bookingLabel = new Label("Create new booking");
        bookingLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px");

        // VBox for title
        VBox availableSection = new VBox();
        availableSection.setPrefWidth(200);
        availableSection.setPadding(new Insets(0, 0, 0, 0));
        availableSection.setAlignment(Pos.TOP_LEFT);
        availableSection.getChildren().add(bookingLabel);

        Button addBooking = new Button();
        addBooking.setGraphic(plusView);

        // 50px Top Menu Header
        topMenuHBox.getChildren().addAll(availableSection, addBooking);

        addBooking.setOnAction(e -> {
            bookingPopup();
        });


        // First load
        // Check om personen har aktive bookings. Hvis ja så skal de jo også loades ved load af menuen.
        //Og tilføj nye bookings
        mainContainer.getChildren().addAll(topMenuHBox); // VBox

        return mainContainer;
    }

    // ____________________________________________________

    private void bookingPopup() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create new booking");
        dialog.setHeaderText("Select new booking information");

        ButtonType saveButtonType = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20, 20, 10, 20));

        datePicker = new DatePicker();
        datePicker.setPrefHeight(30);

        addressField = new TextField();
        addressField.setPrefHeight(30);
        addressField.getStyleClass().add("text-field");

        timePicker = createTimePicker();
        timePicker.setPrefHeight(30);

        hairTypeDropdown = new ComboBox<>();
        hairTypeDropdown.getItems().addAll("Straight", "Wavy", "Curly", "Coily");
        hairTypeDropdown.setPromptText("Choose Hairtype");
        hairTypeDropdown.getStyleClass().add("combo-box");

        hairColorDropdown = new ComboBox<>();
        hairColorDropdown.getItems().addAll("Blonde", "Black", "Brown", "Red", "Grey");
        hairColorDropdown.setPromptText("Hair Color");
        hairColorDropdown.getStyleClass().add("combo-box");

        lengthDropdown = new ComboBox<>();
        lengthDropdown.getItems().addAll("Bald", "Buzz", "Short", "Medium", "Long", "Very Long", "Tied");
        lengthDropdown.setPromptText("Hair Length");
        lengthDropdown.getStyleClass().add("combo-box");

        genderDropdown = new ComboBox<>();
        genderDropdown.getItems().addAll("Male", "Female");
        genderDropdown.setPromptText("Gender");
        genderDropdown.getStyleClass().add("combo-box");

        CheckBox examCheckBox = new CheckBox("Mark if this is for exam");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 0, 20, 0));

        // Date picker
        Label dateLabel = new Label("Date:");
        grid.add(dateLabel, 0, 0);
        grid.add(datePicker, 1, 0);

        // Address writer/setter
        Label addressLabel = new Label("Full address:");
        grid.add(addressLabel, 0, 1);
        grid.add(addressField, 1, 1);

        // Time picker
        Label timeLabel = new Label("Time:");
        grid.add(timeLabel, 0, 2);
        grid.add(timePicker, 1, 2);

        //Hairtype selecter
        Label hairtypeLabel = new Label("Preferred hair type:");
        grid.add(hairtypeLabel, 0, 3);
        grid.add(hairTypeDropdown, 1, 3);

        //Haircolor selecter
        Label hairColorLabel = new Label("Preferred hair color:");
        grid.add(hairColorLabel, 0, 4);
        grid.add(hairColorDropdown, 1, 4);

        //Hairlength selecter
        Label lengthLabel = new Label("Preferred hair Length:");
        grid.add(lengthLabel, 0, 5);
        grid.add(lengthDropdown, 1, 5);

        //Gender select
        Label genderLabel = new Label("Preferred gender:");
        grid.add(genderLabel, 0, 6);
        grid.add(genderDropdown, 1, 6);

        //Exam or nah?
        Label examLabel = new Label("Is it an exam?");
        grid.add(examLabel, 0, 7);
        grid.add(examCheckBox, 1, 7);

        // Adding grid content vbox
        content.getChildren().add(grid);

        // adding content to dialogpane
        dialog.getDialogPane().setContent(content);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {

            LocalDate selectedDate = datePicker.getValue();
            String selectedTime = timePicker.getValue();
            String selectedAddress = addressField.getText();

            String hairType = hairTypeDropdown.getValue();
            String hairColor = hairColorDropdown.getValue();
            String hairLength = lengthDropdown.getValue();
            String gender = genderDropdown.getValue();
            boolean isExam = examCheckBox.isSelected();

            // Check for missing required fields
            if (selectedDate == null || selectedTime == null || selectedAddress == null || selectedAddress.isBlank()
                    || hairType == null || hairColor == null || hairLength == null || gender == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Missing Information");
                alert.setHeaderText("Please fill in all fields before applying.");
                alert.setContentText("Make sure to select date, time, address, hair type, color, length, and gender.");
                alert.showAndWait();
                return; // Exit early, do not save
            }

            int currentStudent_id = Main.db.getUserID(username);
            int hairtypeId = Main.db.getOrCreateHairType(hairType, hairColor, hairLength, gender);

            BookingCard card = new BookingCard(selectedDate, selectedTime, selectedAddress, hairtypeId, isExam, currentStudent_id);
            card.createBooking();

        }
    }
    // ____________________________________________________

    private ComboBox<String> createTimePicker () {

        ComboBox<String> timePicker = new ComboBox<>();
        timePicker.setPromptText("Select Time");

        // Populate times: 00:00 to 23:30
        for (int hour = 0; hour < 24; hour++) {
            String h = String.format("%02d", hour);
            timePicker.getItems().add(h + ":00");
            timePicker.getItems().add(h + ":30");
        }

        // Optional styling
        timePicker.setPrefWidth(120);
        timePicker.setStyle("-fx-font-size: 14px;");

        return timePicker;
    }

    // ____________________________________________________

    public void alertForgot(String msg){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error!");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
