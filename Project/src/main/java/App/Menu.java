// Package
package App;

// Import
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
    private Profile user;
    Profile userProfile;

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
        this.user = new Profile(username);
        this.userProfile = new Profile(this.username);

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

    private void sendMessage(String message) {
        System.out.println("Sends: " + currentChatPartner);
        if (message.isEmpty() || currentChatPartner == null) {
            System.out.println("Something went wrong");
            return;
        }
        System.out.println("HELLO?");
        if (Main.db.isConnected()) {
            Main.db.saveMessage(username, currentChatPartner, message);
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

        /*
        INDLÆS BESKEDER SKAL LAVES HER
        KAN IKKE TESTE FØR JEG FÅR MIN DATABASE TIL AT VIRKE....

        ArrayList<String> persons = Main.db.loadYourMessages(username);
        for (String person : persons) {
            Button personButton = new Button(person);
            personButton.getStyleClass().addAll("user-button");
            personButton.setOnAction(e -> {
                currentChatPartner = personButton.getText();
                messageArea.getChildren().clear();
                loadChatHistory(person);
                sidebar.getChildren().add(personButton);
            });
        }*/
        System.out.println("Lav indlæsning af beskeder på linje 221-235 under Menu.java (Metode er lavet!)");


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
                sendMessage(messageInput.getText());
                messageInput.clear();
            }
        });

        inputArea.getChildren().addAll(messageInput, sendButton);
        rightContent.getChildren().addAll(messageArea, inputArea);

        messageHBox.getChildren().addAll(sidebar, rightContent);
        messageVBox.getChildren().add(messageHBox);

        user1.setOnAction(e -> {
            currentChatPartner = user1.getText();
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

    public VBox displayRating() {

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
        messageArea.setPrefHeight(480);

        HBox inputArea = new HBox(10);
        inputArea.setPadding(new Insets(10));
        inputArea.setAlignment(Pos.CENTER_LEFT);
        inputArea.setPrefHeight(40);

        rightContent.getChildren().addAll(messageArea);

        messageHBox.getChildren().addAll(sidebar, rightContent);
        messageVBox.getChildren().add(messageHBox);

        user1.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayExamReview());
        });
        user2.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayExamReview());
        });
        user3.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayExamReview());
        });
        user4.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayExamReview());
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
            messageArea.getChildren().add(setting.displayProfileColors());
        });

        setting5.setOnAction(e -> {
            messageArea.getChildren().clear();
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
        Button setting5 = new Button("Add Student");

        sidebar.getChildren().addAll(setting1, setting2, setting3, setting4, setting5);
        setting1.getStyleClass().addAll("user-button", "user-button1");
        setting2.getStyleClass().add("user-button-delete");
        setting3.getStyleClass().add("user-button-delete");
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
            messageArea.getChildren().add(displayAcceptStudentRequests());
        });

        setting2.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayRemoveStudents());
        });

        setting3.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayRemoveTeacher());
        });

        setting4.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayAddTeacher());
        });

        setting5.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayAddStudent());
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
        Button setting3 = new Button("Add Student");

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
            messageArea.getChildren().add(displayAcceptStudentRequests());
        });

        setting2.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayRemoveStudents());
        });

        setting3.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayAddStudent());
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
            messageArea.getChildren().add(createBookingRequestsContent());
        });

        setting2.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayActiveBookinsCancel());
        });

        setting3.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(createTipsContent());
        });

        return settingsVBox;
    }

    // ____________________________________________________

    private Node createTipsContent() {
        VBox tipsBox = new VBox(15);
        tipsBox.setPadding(new Insets(10, 10, 10, 10));
        tipsBox.setPrefHeight(480);
        tipsBox.setPrefWidth(Double.MAX_VALUE);
        tipsBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Recent tips");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; -fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        // Tip list container
        VBox tipList = new VBox(10);

        String[] comments = {
                "Tak for sidst!",
                "Slap af en fade bro! :fire:",
                "Du fuckede op, men respekt nok. Du får lige lidt."
        };
        String[] amounts = { "$50", "$30", "$75" };

        for (int i = 0; i < comments.length; i++) {

            VBox singleTipBox = new VBox();
            singleTipBox.setPadding(new Insets(10));
            singleTipBox.setStyle("-fx-background-color: #d0d0d0; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0.3, 0, 3); -fx-background-radius: 15; -fx-border-color: #dddddd; -fx-border-radius: 15;");

            HBox tipLine = new HBox();
            tipLine.setAlignment(Pos.CENTER_LEFT);

            Label commentLabel = new Label(comments[i]);
            commentLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #444;");

            Label amountLabel = new Label(amounts[i]);
            amountLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #41bd25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0.3, 0, 1);");

            HBox.setHgrow(commentLabel, Priority.ALWAYS);
            commentLabel.setMaxWidth(Double.MAX_VALUE);

            tipLine.getChildren().addAll(commentLabel, amountLabel);
            singleTipBox.getChildren().add(tipLine);

            tipList.getChildren().add(singleTipBox);
        }

        VBox.setVgrow(tipList, Priority.ALWAYS);

        HBox summaryBox = new HBox(10);
        summaryBox.setAlignment(Pos.CENTER);
        summaryBox.setPadding(new Insets(10, 0, 0, 0));

        VBox totalTipsBox = new VBox();
        totalTipsBox.setAlignment(Pos.CENTER);
        totalTipsBox.setPadding(new Insets(10));
        totalTipsBox.setStyle("-fx-background-color: #e0ffe0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0.3, 0, 3);");
        Label totalLabel = new Label("Total Tips");
        totalLabel.setStyle("-fx-font-weight: bold;");
        Label totalAmount = new Label("$155");
        totalTipsBox.getChildren().addAll(totalLabel, totalAmount);

        VBox monthTipsBox = new VBox();
        monthTipsBox.setAlignment(Pos.CENTER);
        monthTipsBox.setPadding(new Insets(10));
        monthTipsBox.setStyle("-fx-background-color: #e0f7ff; -fx-border-radius: 8; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0.3, 0, 3);");
        Label monthLabel = new Label("This Month");
        monthLabel.setStyle("-fx-font-weight: bold;");
        Label monthAmount = new Label("$80");
        monthTipsBox.getChildren().addAll(monthLabel, monthAmount);

        HBox.setHgrow(totalTipsBox, Priority.ALWAYS);
        HBox.setHgrow(monthTipsBox, Priority.ALWAYS);
        totalTipsBox.setMaxWidth(Double.MAX_VALUE);
        monthTipsBox.setMaxWidth(Double.MAX_VALUE);

        summaryBox.getChildren().addAll(totalTipsBox, monthTipsBox);

        // Add all to main VBox
        tipsBox.getChildren().addAll(header, tipList, summaryBox);
        return tipsBox;
    }

    // ____________________________________________________

    private Node createBookingRequestsContent() {
        VBox bookingsBox = new VBox(15);
        bookingsBox.setPadding(new Insets(10));
        bookingsBox.setPrefHeight(480);
        bookingsBox.setPrefWidth(Double.MAX_VALUE);
        bookingsBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Booking Requests");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; -fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        VBox bookingList = new VBox(10);

        String[] bookings = {
                "Haircut with Jonas - 26.03 - 14:00",
                "Fade with Andreas - 27.03 - 11:30",
                "Beard trim with Carl Emil - 28.03 - 16:00"
        };

        for (String booking : bookings) {
            VBox singleBookingBox = new VBox();
            singleBookingBox.setPadding(new Insets(10));
            singleBookingBox.setStyle("-fx-background-color: #d0d0d0; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0.3, 0, 3); -fx-background-radius: 15; -fx-border-color: #dddddd; -fx-border-radius: 15;");

            HBox bookingLine = new HBox(10);
            bookingLine.setAlignment(Pos.CENTER_LEFT);

            Label bookingLabel = new Label(booking);
            bookingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #444;");
            HBox.setHgrow(bookingLabel, Priority.ALWAYS);
            bookingLabel.setMaxWidth(Double.MAX_VALUE);

            Button acceptBtn = new Button("Accept");
            acceptBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-font-size: 12px; -fx-padding: 4 10; -fx-background-radius: 20px; -fx-border-radius: 20px;");

            Button denyBtn = new Button("Deny");
            denyBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-font-size: 12px; -fx-padding: 4 10; -fx-background-radius: 20px; -fx-border-radius: 20px;");

            // Hover
            Animation.addHoverScaleEffectMore(acceptBtn);
            Animation.addHoverScaleEffectMore(denyBtn);

            acceptBtn.setOnAction(e -> {
                String bookingInfo = bookingLabel.getText();

                // Extract booking details
                String[] parts = bookingInfo.split(" - ");
                String studentName = parts[0].split(" with ")[1];
                String date = parts[1];
                String time = parts[2];

                // Update booking status in database
                boolean success = Main.db.executeUpdate("UPDATE bookings SET accepted = 'Yes' WHERE student_id = (SELECT id FROM users WHERE username = '" +
                        studentName + "') AND date = '" + date + "' AND time = '" + time + "'");

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Booking has been accepted");
                    alert.showAndWait();

                    // Refresh the booking list
                    messageArea.getChildren().clear();
                    messageArea.getChildren().add(createBookingRequestsContent());
                } else {
                    alertForgot("Failed to accept booking");
                }
            });

            denyBtn.setOnAction(e -> {
                String bookingInfo = bookingLabel.getText();

                // Extract booking details
                String[] parts = bookingInfo.split(" - ");
                String studentName = parts[0].split(" with ")[1];
                String date = parts[1];
                String time = parts[2];

                // Update booking status in database
                boolean success = Main.db.executeUpdate("UPDATE bookings SET accepted = 'No' WHERE student_id = (SELECT id FROM users WHERE username = '" +
                        studentName + "') AND date = '" + date + "' AND time = '" + time + "'");

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Booking has been denied");
                    alert.showAndWait();

                    // Refresh the booking list
                    messageArea.getChildren().clear();
                    messageArea.getChildren().add(createBookingRequestsContent());
                } else {
                    alertForgot("Failed to deny booking");
                }
            });

            HBox requestLine = new HBox(10);
            requestLine.setAlignment(Pos.CENTER_LEFT);
            requestLine.getChildren().addAll(bookingLabel, acceptBtn, denyBtn);
            singleBookingBox.getChildren().add(requestLine);

            bookingList.getChildren().add(singleBookingBox);
        }

        VBox.setVgrow(bookingList, Priority.ALWAYS);

        bookingsBox.getChildren().addAll(header, bookingList);
        return bookingsBox;
    }

    // ____________________________________________________

    private Node displayActiveBookinsCancel() {
        VBox bookingsBox = new VBox(15);
        bookingsBox.setPadding(new Insets(10));
        bookingsBox.setPrefHeight(480);
        bookingsBox.setPrefWidth(Double.MAX_VALUE);
        bookingsBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Cancel booking");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; -fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        VBox bookingList = new VBox(10);

        String[] bookings = {
                "Haircut with Jonas - 26.03 - 14:00",
                "Fade with Andreas - 27.03 - 11:30",
                "Beard trim with Carl Emil - 28.03 - 16:00"
        };

        for (String booking : bookings) {
            VBox singleBookingBox = new VBox();
            singleBookingBox.setPadding(new Insets(10));
            singleBookingBox.setStyle("-fx-background-color: #d0d0d0; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0.3, 0, 3); -fx-background-radius: 15; -fx-border-color: #dddddd; -fx-border-radius: 15;");

            HBox bookingLine = new HBox(10);
            bookingLine.setAlignment(Pos.CENTER_LEFT);

            Label bookingLabel = new Label(booking);
            bookingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #444;");
            HBox.setHgrow(bookingLabel, Priority.ALWAYS);
            bookingLabel.setMaxWidth(Double.MAX_VALUE);

            Button denyBtn = new Button("Cancel");
            denyBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-font-size: 12px; -fx-padding: 4 10; -fx-background-radius: 20px; -fx-border-radius: 20px;");

            // Hover
            Animation.addHoverScaleEffectMore(denyBtn);

            denyBtn.setOnAction(e -> {
                String bookingInfo = bookingLabel.getText();

                // Extract booking details
                String[] parts = bookingInfo.split(" - ");
                String studentName = parts[0].split(" with ")[1];
                String date = parts[1];
                String time = parts[2];

                // Confirm cancellation
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Cancellation");
                confirm.setHeaderText(null);
                confirm.setContentText("Are you sure you want to cancel this booking?");

                Optional<ButtonType> result = confirm.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Delete booking from database
                    boolean success = Main.db.executeUpdate("DELETE FROM bookings WHERE student_id = (SELECT id FROM users WHERE username = '" +
                            studentName + "') AND date = '" + date + "' AND time = '" + time + "'");

                    if (success) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Booking has been cancelled");
                        alert.showAndWait();

                        // Refresh the booking list
                        messageArea.getChildren().clear();
                        messageArea.getChildren().add(displayActiveBookinsCancel());
                    } else {
                        alertForgot("Failed to cancel booking");
                    }
                }
            });

            bookingLine.getChildren().addAll(bookingLabel, denyBtn);
            singleBookingBox.getChildren().add(bookingLine);

            bookingList.getChildren().add(singleBookingBox);
        }

        VBox.setVgrow(bookingList, Priority.ALWAYS);

        bookingsBox.getChildren().addAll(header, bookingList);
        return bookingsBox;
    }

    // ____________________________________________________

    private Node displayRemoveStudents() {
        VBox studentsBox = new VBox(15);
        studentsBox.setPadding(new Insets(10));
        studentsBox.setPrefHeight(480);
        studentsBox.setPrefWidth(Double.MAX_VALUE);
        studentsBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Remove Student");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; -fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        VBox studentList = new VBox(10);

        String[] students = {
                "Jonas",
                "Andreas",
                "Carl Emil",
                "Ebou",
                "Momen",
                "Tess",

        };

        for (String student : students) {
            VBox studentBox = new VBox();
            studentBox.setPadding(new Insets(10));
            studentBox.setStyle("-fx-background-color: #d0d0d0; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0.3, 0, 3); -fx-background-radius: 15; -fx-border-color: #dddddd; -fx-border-radius: 15;");

            HBox studentLine = new HBox(10);
            studentLine.setAlignment(Pos.CENTER_LEFT);

            Label studentLabel = new Label(student);
            studentLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #444;");
            HBox.setHgrow(studentLabel, Priority.ALWAYS);
            studentLabel.setMaxWidth(Double.MAX_VALUE);

            Button removeBtn = new Button("Remove");
            removeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-font-size: 12px; -fx-padding: 4 10; -fx-background-radius: 20px; -fx-border-radius: 20px;");

            // Hover
            Animation.addHoverScaleEffectMore(removeBtn);

            // Action
            removeBtn.setOnAction(e -> {
                String studentToRemove = studentLabel.getText();

                // Confirm removal
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Removal");
                confirm.setHeaderText(null);
                confirm.setContentText("Are you sure you want to remove " + studentToRemove + "?");

                Optional<ButtonType> result = confirm.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    boolean success = Main.db.deleteAccount(studentToRemove);

                    if (success) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText(studentToRemove + " has been removed");
                        alert.showAndWait();

                        // Refresh the student list
                        messageArea.getChildren().clear();
                        messageArea.getChildren().add(displayRemoveStudents());
                    } else {
                        alertForgot("Failed to remove student");
                    }
                }
            });

            studentLine.getChildren().addAll(studentLabel, removeBtn);
            studentBox.getChildren().add(studentLine);

            studentList.getChildren().add(studentBox);
        }

        VBox.setVgrow(studentList, Priority.ALWAYS);

        studentsBox.getChildren().addAll(header, studentList);
        return studentsBox;
    }

    // ____________________________________________________

    private Node displayRemoveTeacher() {
        VBox studentsBox = new VBox(15);
        studentsBox.setPadding(new Insets(10));
        studentsBox.setPrefHeight(480);
        studentsBox.setPrefWidth(Double.MAX_VALUE);
        studentsBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Remove Teacher");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; -fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        VBox studentList = new VBox(10);

        String[] students = {
                "Tess",
                "Tine",
                "Idk",
                "John",
                "Ged"
        };

        for (String student : students) {
            VBox studentBox = new VBox();
            studentBox.setPadding(new Insets(10));
            studentBox.setStyle("-fx-background-color: #d0d0d0; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0.3, 0, 3); -fx-background-radius: 15; -fx-border-color: #dddddd; -fx-border-radius: 15;");

            HBox studentLine = new HBox(10);
            studentLine.setAlignment(Pos.CENTER_LEFT);

            Label studentLabel = new Label(student);
            studentLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #444;");
            HBox.setHgrow(studentLabel, Priority.ALWAYS);
            studentLabel.setMaxWidth(Double.MAX_VALUE);

            Button removeBtn = new Button("Remove");
            removeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-font-size: 12px; -fx-padding: 4 10; -fx-background-radius: 20px; -fx-border-radius: 20px;");

            // Hover
            Animation.addHoverScaleEffectMore(removeBtn);

            // Action
            removeBtn.setOnAction(e -> {
                String teacherToRemove = studentLabel.getText();

                // Confirm removal
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Removal");
                confirm.setHeaderText(null);
                confirm.setContentText("Are you sure you want to remove " + teacherToRemove + "?");

                Optional<ButtonType> result = confirm.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    boolean success = Main.db.deleteAccount(teacherToRemove);

                    if (success) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText(teacherToRemove + " has been removed");
                        alert.showAndWait();

                        // Refresh the teacher list
                        messageArea.getChildren().clear();
                        messageArea.getChildren().add(displayRemoveTeacher());
                    } else {
                        alertForgot("Failed to remove teacher");
                    }
                }
            });

            studentLine.getChildren().addAll(studentLabel, removeBtn);
            studentBox.getChildren().add(studentLine);

            studentList.getChildren().add(studentBox);
        }

        VBox.setVgrow(studentList, Priority.ALWAYS);

        studentsBox.getChildren().addAll(header, studentList);
        return studentsBox;
    }

    // ____________________________________________________

    private Node displayAddTeacher() {

        VBox addTeacherBox = new VBox(15);
        addTeacherBox.setPadding(new Insets(10));
        addTeacherBox.setPrefWidth(Double.MAX_VALUE);
        addTeacherBox.setAlignment(Pos.TOP_LEFT);
        addTeacherBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; " +
                "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Add Teacher");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; " +
                "-fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        HBox inputBox = new HBox(20);
        inputBox.setPrefWidth(400);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Enter teacher name, id or email...");
        teacherInput.setPrefWidth(300);
        teacherInput.setPrefHeight(50);
        teacherInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        addButton.setPrefHeight(50);

        // Hover
        Animation.addHoverScaleEffectMore(addButton);

        // Action
        addButton.setOnAction(e -> {
            String newTeacher = teacherInput.getText().trim();

            if (newTeacher.isEmpty()) {
                alertForgot("Please enter a teacher name");
                return;
            }

            // Check if user exists
            if (Main.db.userExists(newTeacher)) {
                // Change role to Teacher
                boolean success = Main.db.changeRole(newTeacher, "Teacher");

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText(newTeacher + " has been added as a teacher");
                    alert.showAndWait();

                    // Clear field
                    teacherInput.clear();
                } else {
                    alertForgot("Failed to add teacher");
                }
            } else {
                alertForgot("User not found");
            }
        });

        HBox.setHgrow(teacherInput, Priority.ALWAYS);
        inputBox.getChildren().addAll(teacherInput, addButton);

        addTeacherBox.getChildren().addAll(header, inputBox);
        return addTeacherBox;
    }

    // ____________________________________________________

    private Node displayAddStudent() {

        VBox addTeacherBox = new VBox(15);
        addTeacherBox.setPadding(new Insets(10));
        addTeacherBox.setPrefWidth(Double.MAX_VALUE);
        addTeacherBox.setAlignment(Pos.TOP_LEFT);
        addTeacherBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; " +
                "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Add Student");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; " +
                "-fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        HBox inputBox = new HBox(20);
        inputBox.setPrefWidth(400);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Enter student name, id or email...");
        teacherInput.setPrefWidth(300);
        teacherInput.setPrefHeight(50);
        teacherInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        addButton.setPrefHeight(50);

        // Hover
        Animation.addHoverScaleEffectMore(addButton);

        // Action
        addButton.setOnAction(e -> {
            String newStudent = teacherInput.getText().trim();

            if (newStudent.isEmpty()) {
                alertForgot("Please enter a student name");
                return;
            }

            // Check if user exists
            if (Main.db.userExists(newStudent)) {
                // Change role to Student
                boolean success = Main.db.changeRole(newStudent, "Student");

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText(newStudent + " has been added as a student");
                    alert.showAndWait();

                    // Clear field
                    teacherInput.clear();
                } else {
                    alertForgot("Failed to add student");
                }
            } else {
                alertForgot("User not found");
            }
        });

        HBox.setHgrow(teacherInput, Priority.ALWAYS);
        inputBox.getChildren().addAll(teacherInput, addButton);

        addTeacherBox.getChildren().addAll(header, inputBox);
        return addTeacherBox;
    }

    // ____________________________________________________

    private Node displayBanStudent() {

        VBox addTeacherBox = new VBox(15);
        addTeacherBox.setPadding(new Insets(10));
        addTeacherBox.setPrefWidth(Double.MAX_VALUE);
        addTeacherBox.setAlignment(Pos.TOP_LEFT);
        addTeacherBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; " +
                "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Ban Student");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; " +
                "-fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        HBox inputBox = new HBox(20);
        inputBox.setPrefWidth(400);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Enter student name, id or email...");
        teacherInput.setPrefWidth(300);
        teacherInput.setPrefHeight(50);
        teacherInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Button addButton = new Button("Ban");
        addButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        addButton.setPrefHeight(50);

        // Hover
        Animation.addHoverScaleEffectMore(addButton);

        // Action
        addButton.setOnAction(e -> {
            String userToBan = teacherInput.getText().trim();
            if (!userToBan.isEmpty()) {
                AdminMenu adminMenu = new AdminMenu(Main.db, this.username);
                adminMenu.banUser(userToBan);
            } else {
                alertForgot("Please enter a username to ban");
            }
        });

        HBox.setHgrow(teacherInput, Priority.ALWAYS);
        inputBox.getChildren().addAll(teacherInput, addButton);

        addTeacherBox.getChildren().addAll(header, inputBox);
        return addTeacherBox;
    }

    // ____________________________________________________

    private Node displayUnban() {

        VBox addTeacherBox = new VBox(15);
        addTeacherBox.setPadding(new Insets(10));
        addTeacherBox.setPrefWidth(Double.MAX_VALUE);
        addTeacherBox.setAlignment(Pos.TOP_LEFT);
        addTeacherBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; " +
                "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Unban user");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; " +
                "-fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        HBox inputBox = new HBox(20);
        inputBox.setPrefWidth(400);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Enter user name, id or email...");
        teacherInput.setPrefWidth(300);
        teacherInput.setPrefHeight(50);
        teacherInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Button addButton = new Button("Unban");
        addButton.setStyle("-fx-background-color: #00ff22; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        addButton.setPrefHeight(50);

        // Hover
        Animation.addHoverScaleEffectMore(addButton);

        // Action
        addButton.setOnAction(e -> {
            String userToUnban = teacherInput.getText().trim();
            if (!userToUnban.isEmpty()) {
                AdminMenu adminMenu = new AdminMenu(Main.db, this.username);
                adminMenu.unbanUser(userToUnban);
            } else {
                alertForgot("Please enter a username to unban");
            }
        });

        HBox.setHgrow(teacherInput, Priority.ALWAYS);
        inputBox.getChildren().addAll(teacherInput, addButton);

        addTeacherBox.getChildren().addAll(header, inputBox);
        return addTeacherBox;

    }

    // ____________________________________________________

    private Node displayAddSchool() {

        VBox addTeacherBox = new VBox(15);
        addTeacherBox.setPadding(new Insets(10));
        addTeacherBox.setPrefWidth(Double.MAX_VALUE);
        addTeacherBox.setAlignment(Pos.TOP_LEFT);
        addTeacherBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; " +
                "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Add School");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; " +
                "-fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        HBox firstRow = new HBox(20);
        firstRow.setAlignment(Pos.CENTER_LEFT);

        TextField schoolInput = new TextField();
        schoolInput.setPromptText("Enter school name");
        schoolInput.setPrefWidth(300);
        schoolInput.setPrefHeight(50);
        schoolInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        addButton.setPrefHeight(50);

        Animation.addHoverScaleEffectMore(addButton);

        HBox secondRow = new HBox(20);
        secondRow.setAlignment(Pos.CENTER_LEFT);

        TextField addressInput = new TextField();
        addressInput.setPromptText("Address");
        addressInput.setPrefWidth(300);
        addressInput.setPrefHeight(50);
        addressInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        TextField cityInput = new TextField();
        cityInput.setPromptText("City");
        cityInput.setPrefWidth(300);
        cityInput.setPrefHeight(50);
        cityInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        addButton.setOnAction(e -> {
            String schoolName = schoolInput.getText().trim();
            String address = addressInput.getText().trim();
            String city = cityInput.getText().trim();

            if (schoolName.isEmpty() || address.isEmpty() || city.isEmpty()) {
                alertForgot("Please fill in all fields");
                return;
            }

            // Create school in database
            String query = "INSERT INTO schools (name, address, city) VALUES ('" +
                    schoolName + "', '" + address + "', '" + city + "')";
            boolean success = Main.db.executeUpdate(query);

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("School added successfully");
                alert.showAndWait();

                // Clear fields
                schoolInput.clear();
                addressInput.clear();
                cityInput.clear();
            } else {
                alertForgot("Failed to add school");
            }
        });

        HBox.setHgrow(schoolInput, Priority.ALWAYS);
        firstRow.getChildren().addAll(schoolInput, addButton);
    
        HBox.setHgrow(addressInput, Priority.ALWAYS);
        HBox.setHgrow(cityInput, Priority.ALWAYS);
        secondRow.getChildren().addAll(addressInput, cityInput);
    
        addTeacherBox.getChildren().addAll(header, firstRow, secondRow);
        return addTeacherBox;
    }

    // ____________________________________________________

    private Node displayUsernameChange() {

        VBox addTeacherBox = new VBox(15);
        addTeacherBox.setPadding(new Insets(10));
        addTeacherBox.setPrefWidth(Double.MAX_VALUE);
        addTeacherBox.setAlignment(Pos.TOP_LEFT);
        addTeacherBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; " +
                "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Change username");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; " +
                "-fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        HBox inputBox = new HBox(20);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Username");
        teacherInput.setPrefHeight(50);
        teacherInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        TextField roleInput = new TextField();
        roleInput.setPromptText("New username");
        roleInput.setPrefHeight(50);
        roleInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Button addButton = new Button("Change");
        addButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        addButton.setPrefHeight(50);

        Animation.addHoverScaleEffectMore(addButton);

        addButton.setOnAction(e -> {
            String currentUsername = teacherInput.getText().trim();
            String newUsername = roleInput.getText().trim();

            if (currentUsername.isEmpty() || newUsername.isEmpty()) {
                alertForgot("Please fill in all fields");
                return;
            }

            // Check if user exists
            if (!Main.db.userExists(currentUsername)) {
                alertForgot("User not found");
                return;
            }

            // Check if new username is already taken
            if (Main.db.userExists(newUsername)) {
                alertForgot("Username already taken");
                return;
            }

            // Get user's email to update username
            String email = Main.db.getEmail(currentUsername);
            if (email == null) {
                alertForgot("Failed to retrieve user email");
                return;
            }

            boolean success = Main.db.changeUsername(email, newUsername);

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Username changed successfully");
                alert.showAndWait();

                // Clear fields
                teacherInput.clear();
                roleInput.clear();
            } else {
                alertForgot("Failed to change username");
            }
        });

        HBox.setHgrow(teacherInput, Priority.ALWAYS);
        HBox.setHgrow(roleInput, Priority.ALWAYS);

        inputBox.getChildren().addAll(teacherInput, roleInput, addButton);
        addTeacherBox.getChildren().addAll(header, inputBox);
        return addTeacherBox;
    }

    // ____________________________________________________

    private Node displayChangeNumberAdmin() {

        VBox addTeacherBox = new VBox(15);
        addTeacherBox.setPadding(new Insets(10));
        addTeacherBox.setPrefWidth(Double.MAX_VALUE);
        addTeacherBox.setAlignment(Pos.TOP_LEFT);
        addTeacherBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; " +
                "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Change number");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; " +
                "-fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        HBox inputBox = new HBox(20);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Username");
        teacherInput.setPrefHeight(50);
        teacherInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        TextField roleInput = new TextField();
        roleInput.setPromptText("New number");
        roleInput.setPrefHeight(50);
        roleInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Button addButton = new Button("Change");
        addButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        addButton.setPrefHeight(50);

        Animation.addHoverScaleEffectMore(addButton);

        addButton.setOnAction(e -> {
            String username = teacherInput.getText().trim();
            String newNumber = roleInput.getText().trim();

            if (username.isEmpty() || newNumber.isEmpty()) {
                alertForgot("Please fill in all fields");
                return;
            }

            // Check if user exists
            if (!Main.db.userExists(username)) {
                alertForgot("User not found");
                return;
            }

            // Update phone number in database
            boolean success = Main.db.executeUpdate("UPDATE users SET phone_number = '" + newNumber + "' WHERE username = '" + username + "'");

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Phone number updated successfully");
                alert.showAndWait();

                // Clear fields
                teacherInput.clear();
                roleInput.clear();
            } else {
                alertForgot("Failed to update phone number");
            }
        });

        HBox.setHgrow(teacherInput, Priority.ALWAYS);
        HBox.setHgrow(roleInput, Priority.ALWAYS);

        inputBox.getChildren().addAll(teacherInput, roleInput, addButton);
        addTeacherBox.getChildren().addAll(header, inputBox);
        return addTeacherBox;
    }

    // ____________________________________________________

    private Node displayChangeEmailAdmin() {

        VBox addTeacherBox = new VBox(15);
        addTeacherBox.setPadding(new Insets(10));
        addTeacherBox.setPrefWidth(Double.MAX_VALUE);
        addTeacherBox.setAlignment(Pos.TOP_LEFT);
        addTeacherBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; " +
                "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Change email");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; " +
                "-fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        HBox inputBox = new HBox(20);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Username");
        teacherInput.setPrefHeight(50);
        teacherInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        TextField roleInput = new TextField();
        roleInput.setPromptText("New email");
        roleInput.setPrefHeight(50);
        roleInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Button addButton = new Button("Change");
        addButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        addButton.setPrefHeight(50);

        Animation.addHoverScaleEffectMore(addButton);

        addButton.setOnAction(e -> {
            String username = teacherInput.getText().trim();
            String newEmail = roleInput.getText().trim();

            if (username.isEmpty() || newEmail.isEmpty()) {
                alertForgot("Please fill in all fields");
                return;
            }

            // Check if user exists
            if (!Main.db.userExists(username)) {
                alertForgot("User not found");
                return;
            }

            // Update email in database
            boolean success = Main.db.executeUpdate("UPDATE users SET email = '" + newEmail + "' WHERE username = '" + username + "'");

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Email updated successfully");
                alert.showAndWait();

                // Clear fields
                teacherInput.clear();
                roleInput.clear();
            } else {
                alertForgot("Failed to update email");
            }
        });

        HBox.setHgrow(teacherInput, Priority.ALWAYS);
        HBox.setHgrow(roleInput, Priority.ALWAYS);

        inputBox.getChildren().addAll(teacherInput, roleInput, addButton);
        addTeacherBox.getChildren().addAll(header, inputBox);
        return addTeacherBox;
    }

    // ____________________________________________________

    private Node displayChangeRole() {

        VBox addTeacherBox = new VBox(15);
        addTeacherBox.setPadding(new Insets(10));
        addTeacherBox.setPrefWidth(Double.MAX_VALUE);
        addTeacherBox.setAlignment(Pos.TOP_LEFT);
        addTeacherBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; " +
                "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Change role");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; " +
                "-fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        HBox inputBox = new HBox(20);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Username");
        teacherInput.setPrefHeight(50);
        teacherInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        TextField roleInput = new TextField();
        roleInput.setPromptText("Role");
        roleInput.setPrefHeight(50);
        roleInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        addButton.setPrefHeight(50);

        Animation.addHoverScaleEffectMore(addButton);

        addButton.setOnAction(e -> {
            String username = teacherInput.getText().trim();
            String role = roleInput.getText().trim();

            if (username.isEmpty() || role.isEmpty()) {
                alertForgot("Please fill in all fields");
                return;
            }

            AdminMenu adminMenu = new AdminMenu(Main.db, this.username);
            adminMenu.changeUserRole(username, role);
        });

        HBox.setHgrow(teacherInput, Priority.ALWAYS);
        HBox.setHgrow(roleInput, Priority.ALWAYS);

        inputBox.getChildren().addAll(teacherInput, roleInput, addButton);
        addTeacherBox.getChildren().addAll(header, inputBox);
        return addTeacherBox;
    }

    // ____________________________________________________

    private Node displayResetProfilePicture() {

        VBox addTeacherBox = new VBox(15);
        addTeacherBox.setPadding(new Insets(10));
        addTeacherBox.setPrefWidth(Double.MAX_VALUE);
        addTeacherBox.setAlignment(Pos.TOP_LEFT);
        addTeacherBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; " +
                "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Reset Profile Picture");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; " +
                "-fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        HBox inputBox = new HBox(20);
        inputBox.setPrefWidth(400);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Enter name, id or email");
        teacherInput.setPrefWidth(300);
        teacherInput.setPrefHeight(50);
        teacherInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Button addButton = new Button("Change");
        addButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        addButton.setPrefHeight(50);

        // Hover
        Animation.addHoverScaleEffectMore(addButton);

        // Action
        addButton.setOnAction(e -> {
            String username = teacherInput.getText().trim();

            if (username.isEmpty()) {
                alertForgot("Please enter a username");
                return;
            }

            AdminMenu adminMenu = new AdminMenu(Main.db, this.username);
            adminMenu.resetProfilePicture(username);
        });

        HBox.setHgrow(teacherInput, Priority.ALWAYS);
        inputBox.getChildren().addAll(teacherInput, addButton);

        addTeacherBox.getChildren().addAll(header, inputBox);
        return addTeacherBox;
    }

    // ____________________________________________________

    private Node displayChangeColorsAdmin() {

        VBox addTeacherBox = new VBox(15);
        addTeacherBox.setPadding(new Insets(10));
        addTeacherBox.setPrefWidth(Double.MAX_VALUE);
        addTeacherBox.setAlignment(Pos.TOP_LEFT);
        addTeacherBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; " +
                "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Change colors for user");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; " +
                "-fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        HBox inputBox = new HBox(20);
        inputBox.setPrefWidth(400);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Enter name, id or email");
        teacherInput.setPrefWidth(300);
        teacherInput.setPrefHeight(50);
        teacherInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Button addButton = new Button("Change");
        addButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        addButton.setPrefHeight(50);

        // Hover
        Animation.addHoverScaleEffectMore(addButton);

        // Action
        addButton.setOnAction(e -> {
            String username = teacherInput.getText().trim();

            if (username.isEmpty()) {
                alertForgot("Please enter a username");
                return;
            }

            // Open color picker dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Change Profile Colors");
            dialog.setHeaderText("Select new colors for " + username);

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            VBox content = new VBox(15);
            content.setPadding(new Insets(20));

            ColorPicker profileColorPicker = new ColorPicker(Color.web("#ADD8E6"));
            ColorPicker bannerColorPicker = new ColorPicker(Color.web("#D3D3D3"));
            ColorPicker roleColorPicker = new ColorPicker(Color.web("#d0e6f7"));

            Label profileLabel = new Label("Profile Background Color:");
            Label bannerLabel = new Label("Banner Color:");
            Label roleLabel = new Label("Role Background Color:");

            content.getChildren().addAll(
                    profileLabel, profileColorPicker,
                    bannerLabel, bannerColorPicker,
                    roleLabel, roleColorPicker
            );

            dialog.getDialogPane().setContent(content);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == saveButtonType) {
                String profileHex = toHexString(profileColorPicker.getValue());
                String bannerHex = toHexString(bannerColorPicker.getValue());
                String roleHex = toHexString(roleColorPicker.getValue());

                boolean success = Main.db.updateProfileColors(username, profileHex, bannerHex, roleHex, null);

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Profile colors updated successfully");
                    alert.showAndWait();
                } else {
                    alertForgot("Failed to update profile colors");
                }
            }
        });

        HBox.setHgrow(teacherInput, Priority.ALWAYS);
        inputBox.getChildren().addAll(teacherInput, addButton);

        addTeacherBox.getChildren().addAll(header, inputBox);
        return addTeacherBox;
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255),
                (int)(color.getOpacity() * 255));
    }

    // ____________________________________________________

    private Node displayExamReview() {

        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(15));
        contentBox.setPrefWidth(Double.MAX_VALUE);
        contentBox.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-radius: 25;" +
                        "-fx-border-color: #ccc;" +
                        "-fx-border-width: 1px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0.3, 0, 4);"
        );

        Label header = new Label("Exam title");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #333; -fx-border-width: 0 0 2px 0; -fx-border-color: orange;");
        header.setPadding(new Insets(0, 0, 10, 0));

        Label examTitle = new Label("Subtitle fuck mig altså..");
        examTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2a2a2a;");

        Label examText = new Label("Det her er en placeholder tekst.. Jeg hader livet. Det her er en placeholder tekst.. Jeg hader livet. Det her er en placeholder tekst.. Jeg hader livet.");
        examText.setWrapText(true);
        examText.setStyle("-fx-font-size: 14px; -fx-text-fill: #444;");

        Image examImage = new Image(getClass().getResource("/assets/exam/haircut1.jpg").toExternalForm());
        ImageView imageView = new ImageView(examImage);
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 4, 0.3, 0, 2);");

        Label underImgText = new Label("Det her er en placeholder tekst.. Jeg hader livet. Det her er en placeholder tekst.. Jeg hader livet. Det her er en placeholder tekst.. Jeg hader livet.");
        underImgText.setWrapText(true);
        underImgText.setStyle("-fx-font-size: 14px; -fx-text-fill: #444;");

        VBox imageWrapper = new VBox(imageView);
        imageWrapper.setPadding(new Insets(0, 0, 0, 0));

        // Grading controls
        HBox gradingBar = new HBox(10);
        gradingBar.setAlignment(Pos.CENTER_LEFT);
        gradingBar.setPadding(new Insets(10, 0, 0, 0));

        TextField gradeField = new TextField();
        gradeField.setPromptText("Grade");
        gradeField.setPrefWidth(70);
        gradeField.setAlignment(Pos.CENTER);
        gradeField.setStyle("-fx-prompt-text-fill: #2f2f2f; -fx-font-size: 16px; -fx-background-color: orange; -fx-text-fill: #171717; -fx-background-insets: 0; -fx-border-insets: 0; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Comment...");
        commentArea.setPrefRowCount(2);
        commentArea.setPrefWidth(330);
        commentArea.setWrapText(true);

        Button sendBtn = new Button("Send");
        sendBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 4 10; -fx-font-size: 12px; -fx-background-radius: 20px; -fx-border-radius: 20px;");

        // Hover
        Animation.addHoverScaleEffectMore(sendBtn);

        // Action
        sendBtn.setOnAction(e -> {
            String grade = gradeField.getText();
            String comment = commentArea.getText();
        });

        gradingBar.getChildren().addAll(gradeField, commentArea, sendBtn);

        contentBox.getChildren().addAll(header, examTitle, examText, imageWrapper, underImgText, gradingBar);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(500);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        return scrollPane;
    }

    // ____________________________________________________

    private Node displayAcceptStudentRequests() {
        VBox requestsBox = new VBox(15);
        requestsBox.setPadding(new Insets(10));
        requestsBox.setPrefHeight(480);
        requestsBox.setPrefWidth(Double.MAX_VALUE);
        requestsBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Accept Student");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; -fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        VBox requestList = new VBox(10);

        String[] requests = {
                "Jonas requests to be your student",
                "Andreas requests to be your student",
                "Carl Emil requests to be your student"
        };

        for (String request : requests) {
            VBox singleRequestBox = new VBox();
            singleRequestBox.setPadding(new Insets(10));
            singleRequestBox.setStyle("-fx-background-color: #d0d0d0; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0.3, 0, 3); -fx-background-radius: 15; -fx-border-color: #dddddd; -fx-border-radius: 15;");

            HBox requestLine = new HBox(10);
            requestLine.setAlignment(Pos.CENTER_LEFT);

            Label requestLabel = new Label(request);
            requestLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #444;");
            HBox.setHgrow(requestLabel, Priority.ALWAYS);
            requestLabel.setMaxWidth(Double.MAX_VALUE);

            Button acceptBtn = new Button("Accept");
            acceptBtn.setStyle("-fx-background-color: #41bd25; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-font-size: 12px; -fx-padding: 4 10; -fx-background-radius: 20px; -fx-border-radius: 20px;");

            Button denyBtn = new Button("Deny");
            denyBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-font-size: 12px; -fx-padding: 4 10; -fx-background-radius: 20px; -fx-border-radius: 20px;");

            // Hover
            Animation.addHoverScaleEffectMore(acceptBtn);
            Animation.addHoverScaleEffectMore(denyBtn);

            acceptBtn.setOnAction(e -> {
                String studentName = requestLabel.getText().split(" requests")[0];

                // Update student's accepted status
                boolean success = Main.db.changeAccepted(studentName, "Yes");

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText(studentName + " has been accepted");
                    alert.showAndWait();

                    // Refresh the request list
                    messageArea.getChildren().clear();
                    messageArea.getChildren().add(displayAcceptStudentRequests());
                } else {
                    alertForgot("Failed to accept student");
                }
            });

            denyBtn.setOnAction(e -> {
                String studentName = requestLabel.getText().split(" requests")[0];

                // Update student's accepted status
                boolean success = Main.db.changeAccepted(studentName, "No");

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText(studentName + " has been denied");
                    alert.showAndWait();

                    // Refresh the request list
                    messageArea.getChildren().clear();
                    messageArea.getChildren().add(displayAcceptStudentRequests());
                } else {
                    alertForgot("Failed to deny student");
                }
            });


            requestLine.getChildren().addAll(requestLabel, acceptBtn, denyBtn);
            singleRequestBox.getChildren().add(requestLine);

            requestList.getChildren().add(singleRequestBox);
        }

        VBox.setVgrow(requestList, Priority.ALWAYS);

        requestsBox.getChildren().addAll(header, requestList);
        return requestsBox;

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
        /*ArrayList<ArrayList<String>> bookings = getBookings();
        for (ArrayList<String> booking : bookings) {
            Label dateLabel = new Label(booking.get(0));
            Label timeLabel = new Label(booking.get(1));
            Label placeLabel = new Label(booking.get(2));
            Label studentLabel = new Label(booking.get(3));
            double rating = Double.parseDouble(booking.get(4))
            Label ratingLabel = new Label(convertToStars(rating));
        }*/

        String date = getDate();
        String time = getTime();

        Label dateLabel = new Label(date);
        Label timeLabel = new Label(time);
        Label placeLabel = new Label(getAdress());
        Label studentLabel = new Label(getStudentName());
        Label ratingLabel = new Label(convertToStars(user.getRating()));
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
            double ratingInt = booking.getReview(); // 0 if you haven't set up yet
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
        Main.db.updateStatus(username, "Online");
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
        this.userProfile = new Profile(this.username);
        String profilePicBgColor = userProfile.getProfilePictureHex();
        String bannerBgColor = userProfile.getProfileBannerHex();
        String roleBgColor = userProfile.getProfileRoleHex();
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
        leftPane.setStyle("-fx-background-color: " + profilePicBgColor + ";");
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
        leftPaneInfo.setStyle("-fx-background-color: " + roleBgColor + ";");

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
        rightPaneBanner.setStyle("-fx-border-color: rgb(0, 0, 0); -fx-border-width: 0 0 2px 2px; -fx-padding: 2px 0 0 0; -fx-background-color: " + bannerBgColor + ";");

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
        this.userProfile = new Profile(this.username);
        String profilePicBgColor = userProfile.getProfilePictureHex();
        String bannerBgColor = userProfile.getProfileBannerHex();
        String roleBgColor = userProfile.getProfileRoleHex();

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
        leftPane.setStyle("-fx-background-color: " + profilePicBgColor + ";");

        Image profileImage = new Image(getClass().getResource("/assets/profile/person1.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(150);
        profileImageView.setFitHeight(130);
        profileImageView.setPreserveRatio(true);

        Label roleLabel = new Label("Student");
        roleLabel.setPadding(new Insets(5,0,5,0));

        VBox leftPaneInfo = new VBox();
        leftPaneInfo.setAlignment(Pos.CENTER);
        leftPaneInfo.getChildren().add(roleLabel);
        leftPaneInfo.setStyle("-fx-background-color: " + roleBgColor + ";");

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
        rightPaneBanner.setStyle("-fx-border-color: rgb(0, 0, 0); -fx-border-width: 0 0 2px 2px; -fx-padding: 2px 0 0 0; -fx-background-color: " + bannerBgColor + ";");

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
        this.userProfile = new Profile(this.username);
        String profilePicBgColor = userProfile.getProfilePictureHex();
        String bannerBgColor = userProfile.getProfileBannerHex();
        String roleBgColor = userProfile.getProfileRoleHex();

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
        leftPane.setStyle("-fx-background-color: " + profilePicBgColor + ";");

        Image profileImage = new Image(getClass().getResource("/assets/profile/person1.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(150);
        profileImageView.setFitHeight(130);
        profileImageView.setPreserveRatio(true);

        Label roleLabel = new Label("School");
        roleLabel.setPadding(new Insets(5,0,5,0));

        VBox leftPaneInfo = new VBox();
        leftPaneInfo.setAlignment(Pos.CENTER);
        leftPaneInfo.getChildren().add(roleLabel);
        leftPaneInfo.setStyle("-fx-background-color: " + roleBgColor + ";");

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
        rightPaneBanner.setStyle("-fx-border-color: rgb(0, 0, 0); -fx-border-width: 0 0 2px 2px; -fx-padding: 2px 0 0 0; -fx-background-color: " + bannerBgColor + ";");

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
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            contactBox.getStyleClass().add("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            getChildren().clear();
        });

        aboutMeBox.setOnMouseClicked(e -> {
            contactBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            aboutMeBox.getStyleClass().add("nav-box-selected-first");
            bottomContentBox.getChildren().clear();
        });

        reviewsBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            contactBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
        });

        galleryBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            contactBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
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
        // bottomContentBox.getChildren().add(displayAboutMe()); ENABLE WHEN CORRECT

        return profileVBox;
    }

// ____________________________________________________

    public VBox displayProfileTeacher() {
        this.userProfile = new Profile(this.username);
        String profilePicBgColor = userProfile.getProfilePictureHex();
        String bannerBgColor = userProfile.getProfileBannerHex();
        String roleBgColor = userProfile.getProfileRoleHex();

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
        leftPane.setStyle("-fx-background-color: " + profilePicBgColor + ";");

        Image profileImage = new Image(getClass().getResource("/assets/profile/person1.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(150);
        profileImageView.setFitHeight(130);
        profileImageView.setPreserveRatio(true);

        Label roleLabel = new Label("Teacher");
        roleLabel.setPadding(new Insets(5,0,5,0));

        VBox leftPaneInfo = new VBox();
        leftPaneInfo.setAlignment(Pos.CENTER);
        leftPaneInfo.getChildren().add(roleLabel);
        leftPaneInfo.setStyle("-fx-background-color: " + roleBgColor + ";");

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
        rightPaneBanner.setStyle("-fx-border-color: rgb(0, 0, 0); -fx-border-width: 0 0 2px 2px; -fx-padding: 2px 0 0 0; -fx-background-color: " + bannerBgColor + ";");

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

        HBox faqBox = createNavBox("F&Q");
        HBox contactBox = createNavBox("Contact");
        HBox studentsBox = createNavBox("Students");
        HBox reviewsOfUsBox = createNavBox("Reviews of us");

        navRow.getChildren().addAll(faqBox, studentsBox, contactBox, reviewsOfUsBox);

        VBox rightPaneContent = new VBox(10);
        rightPaneContent.setPrefWidth(550);
        rightPaneContent.setAlignment(Pos.TOP_CENTER);

        // Actions
        contactBox.setOnMouseClicked(e -> {
            contactBox.getStyleClass().add("nav-box-selected");
            studentsBox.getStyleClass().remove("nav-box-selected");
            reviewsOfUsBox.getStyleClass().remove("nav-box-selected");
            faqBox.getStyleClass().remove("nav-box-selected-first");
            bottomContentBox.getChildren().clear();
        });

        faqBox.setOnMouseClicked(e -> {
            contactBox.getStyleClass().remove("nav-box-selected");
            studentsBox.getStyleClass().remove("nav-box-selected");
            reviewsOfUsBox.getStyleClass().remove("nav-box-selected");
            faqBox.getStyleClass().add("nav-box-selected-first");
            bottomContentBox.getChildren().clear();
        });

        studentsBox.setOnMouseClicked(e -> {
            faqBox.getStyleClass().remove("nav-box-selected-first");
            contactBox.getStyleClass().remove("nav-box-selected");
            reviewsOfUsBox.getStyleClass().remove("nav-box-selected");
            studentsBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
        });

        reviewsOfUsBox.setOnMouseClicked(e -> {
            faqBox.getStyleClass().remove("nav-box-selected-first");
            contactBox.getStyleClass().remove("nav-box-selected");
            studentsBox.getStyleClass().remove("nav-box-selected");
            reviewsOfUsBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
        });

        // CSS
        leftPane.getStyleClass().add("left-pane");
        leftPaneInfo.getStyleClass().add("left-pane-info");
        contactBox.getStyleClass().add("about-me-text-area");
        rightPane.getStyleClass().add("right-pane");

        // Default Selected Box
        faqBox.getStyleClass().add("nav-box-selected-first");

        // Add
        rightPane.getChildren().addAll(rightPaneBanner, navRow, rightPaneContent);
        profilePictureHBox.getChildren().addAll(leftPane, rightPane);
        profileVBox.getChildren().addAll(profilePictureHBox, bottomContentBox);

        // Load about me by default
        //bottomContentBox.getChildren().add(displayAboutMe()); ADD CORRECT FAQ WHEN ITS DONE

        return profileVBox;
    }

// ____________________________________________________

    public VBox displayProfileSupport() {
        this.userProfile = new Profile(this.username);
        String profilePicBgColor = userProfile.getProfilePictureHex();
        String bannerBgColor = userProfile.getProfileBannerHex();
        String roleBgColor = userProfile.getProfileRoleHex();

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
        leftPane.setStyle("-fx-background-color: " + profilePicBgColor + ";");

        Image profileImage = new Image(getClass().getResource("/assets/profile/person1.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(150);
        profileImageView.setFitHeight(130);
        profileImageView.setPreserveRatio(true);

        Label roleLabel = new Label("Support");
        roleLabel.setPadding(new Insets(5,0,5,0));

        VBox leftPaneInfo = new VBox();
        leftPaneInfo.setAlignment(Pos.CENTER);
        leftPaneInfo.getChildren().add(roleLabel);
        leftPaneInfo.setStyle("-fx-background-color: " + roleBgColor + ";");

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
        rightPaneBanner.setStyle("-fx-border-color: rgb(0, 0, 0); -fx-border-width: 0 0 2px 2px; -fx-padding: 2px 0 0 0; -fx-background-color: " + bannerBgColor + ";");

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
            contactBox.getStyleClass().add("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            bottomContentBox.getChildren().clear();
        });

        aboutMeBox.setOnMouseClicked(e -> {
            contactBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            aboutMeBox.getStyleClass().add("nav-box-selected-first");
            bottomContentBox.getChildren().clear();
        });

        reviewsBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            contactBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
        });

        galleryBox.setOnMouseClicked(e -> {
            aboutMeBox.getStyleClass().remove("nav-box-selected-first");
            contactBox.getStyleClass().remove("nav-box-selected");
            reviewsBox.getStyleClass().remove("nav-box-selected");
            galleryBox.getStyleClass().add("nav-box-selected");
            bottomContentBox.getChildren().clear();
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
        // bottomContentBox.getChildren().add(displayAboutMe()); // DISPLAY CORRECT WHEN DONE

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

        ArrayList<ArrayList<String>> reviews = user.getReviews();

        for (ArrayList<String> review : reviews) {
            VBox singleReview = new VBox(5);
            singleReview.setPadding(new Insets(30));
            singleReview.setStyle("-fx-background-color: white; -fx-background-radius: 10px; "
            + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

            Label header = new Label(review.get(0));
            header.setStyle("-fx-text-alignment: CENTER !important; -fx-font-weight: bold; -fx-font-size: 14px;");

            Label body = new Label(review.get(1));
            body.setStyle("-fx-font-size: 10px;");
            body.setWrapText(true);
            body.setMaxWidth(760/2);
            double rating = Double.parseDouble(review.get(2));
            Label stars = new Label(convertToStars(rating));
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

        ArrayList<ArrayList<String>> cuts = user.getCuts();
        for (ArrayList<String> cut : cuts) {
            VBox pictureBox = new VBox(5);
            pictureBox.setAlignment(Pos.TOP_CENTER);
            pictureBox.setPadding(new Insets(20));
            pictureBox.setStyle("-fx-background-color: white; -fx-background-radius: 10px; "
            + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
            pictureBox.setMaxWidth(760/2);

            Label header = new Label(cut.get(0));
            header.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-alignment: center");

            // Fix visuals a bit more at some point
            try {
                String path = cut.get(1);
                Image image = new Image(getClass().getResource(path).toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(180);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);

                pictureBox.getChildren().addAll(header, imageView);
                flowPane.getChildren().add(pictureBox);
            } catch (Exception e) {
                System.out.println("Failed to load picture: " + cut.get(1) + ".png");
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
        Label lastOnlineValue = new Label(user.getLastOnline());

        // Person info
        Label nameLabel = new Label(this.username);
        nameLabel.setStyle("-fx-text-fill: orange; -fx-font-size: 18px;");

        // Socials
        Label instagramLabel = new Label("Instagram");
        Label facebookLabel = new Label("Facebook");
        Label githubLabel = new Label("Github");

        instagramLabel.setOnMouseClicked(e -> {
            openLink(user.getSocial("Instagram"));
        });
        facebookLabel.setOnMouseClicked(e -> {
            openLink(user.getSocial("Facebook"));
        });
        githubLabel.setOnMouseClicked(e -> {
            openLink(user.getSocial("Github"));
        });

        List<VBox> socialButtons = new ArrayList<>();

        VBox instagramButton = createSelectableSocialButton(instagramLabel, socialButtons);
        VBox facebookButton = createSelectableSocialButton(facebookLabel, socialButtons);
        VBox githubButton = createSelectableSocialButton(githubLabel, socialButtons);

        socialButtons.addAll(List.of(instagramButton, facebookButton, githubButton));

        VBox socialsBox = new VBox(0,nameLabel, instagramButton, facebookButton, githubButton);
        socialsBox.setAlignment(Pos.CENTER);

        Label cityLabel = new Label(user.getCity());
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

        Label aboutHeader = new Label(user.getProfileAboutmeHeader());
        aboutHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: orange;");

        Label bioText = new Label(user.getProfileAboutDescription());
        bioText.setWrapText(true);
        bioText.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Label interestsHeader = new Label("Interests");
        interestsHeader.setStyle("-fx-font-size: 16px; -fx-text-fill: #ccc; -fx-padding: 10 0 5 0;");

        HBox tagsBox = new HBox(5);
        tagsBox.setPadding(new Insets(5, 0, 0, 0));
        tagsBox.setAlignment(Pos.CENTER_LEFT);

        String[] tags = user.getInterests();

        for (String tag : tags) {
            Label tagLabel = new Label("#" + tag);
            tagLabel.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 4 8 4 8; -fx-background-radius: 10; -fx-font-size: 14px");
            tagsBox.getChildren().add(tagLabel);
        }

        Label funFactHeader = new Label("Fun Fact");
        funFactHeader.setStyle("-fx-font-size: 16px; -fx-text-fill: #ccc; -fx-padding: 10 0 5 0;");

        Label funFactText = new Label(user.getFunFacts());
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

    private void openLink(String link) {
        try {
            Desktop desktop = Desktop.getDesktop();
            URI uri = new URI(link);
            desktop.browse(uri);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
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
            messageArea.getChildren().add(displayBanStudent());
        });

        admin2.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayUnban());
        });

        admin3.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayAddSchool());
        });

        admin4.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayChangeRole());
        });

        admin5.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayResetProfilePicture());
        });

        admin6.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayChangeColorsAdmin());
        });

        admin7.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayUsernameChange());
        });

        admin8.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayRemoveSchool());
        });

        admin9.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayMuteUser());
        });

        admin10.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayChangeNumberAdmin());
        });

        admin11.setOnAction(e -> {
            messageArea.getChildren().clear();
            messageArea.getChildren().add(displayChangeEmailAdmin());
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

        Label subHeader = new Label(user.getContactHeader());
        subHeader.setStyle("-fx-font-size: 16px; -fx-text-fill: #cccccc;");

        Label bioText = new Label(user.getContactDescription());
        bioText.setWrapText(true);
        bioText.setTextAlignment(TextAlignment.CENTER);
        bioText.setAlignment(Pos.CENTER);
        bioText.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Label emailLabel = new Label("Email: "+user.getEmail());
        emailLabel.setStyle("-fx-text-fill: #fff; -fx-font-size: 14px;");
        emailLabel.setTextAlignment(TextAlignment.CENTER);

        Label phoneLabel = new Label("Phone: "+user.getPhoneNumber());
        phoneLabel.setStyle("-fx-text-fill: #fff; -fx-font-size: 14px;");
        phoneLabel.setTextAlignment(TextAlignment.CENTER);

        HBox socialLinks = new HBox(10);
        socialLinks.setAlignment(Pos.CENTER);

        String[] socials = {"Instagram", "Facebook", "GitHub"};
        for (String social : socials) {
            Label link = new Label(social);
            link.setStyle("-fx-background-color: #595959; -fx-text-fill: white; -fx-padding: 6 12 6 12; -fx-background-radius: 8; -fx-font-size: 13px; -fx-cursor: hand;");
            link.setOnMouseClicked(e -> {
                openLink(user.getSocial(social));
            });
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

    // ____________________________________________________

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
        availableSection.setAlignment(Pos.CENTER_LEFT);
        availableSection.getChildren().add(bookingLabel);

        Button addBooking = new Button();
        addBooking.setStyle("-fx-border-radius: 25px; -fx-background-radius: 25px; -fx-background-insets: 0; -fx-border-insets: 0; -fx-background-color: orange");
        addBooking.setGraphic(plusView);

        // 50px Top Menu Header
        topMenuHBox.getChildren().addAll(availableSection, addBooking);

        addBooking.setOnAction(e -> {
            bookingPopup();
        });

        // First load
        // Check om personen har aktive bookings. Hvis ja så skal de jo også loades ved load af menuen.
        // Og tilføj nye bookings
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

    // ____________________________________________________

    private Node displayMuteUser() {

        VBox addTeacherBox = new VBox(15);
        addTeacherBox.setPadding(new Insets(10));
        addTeacherBox.setPrefWidth(Double.MAX_VALUE);
        addTeacherBox.setAlignment(Pos.TOP_LEFT);
        addTeacherBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; " +
                "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Mute user");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; " +
                "-fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        HBox inputBox = new HBox(20);
        inputBox.setPrefWidth(400);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Enter user name, id or email...");
        teacherInput.setPrefWidth(300);
        teacherInput.setPrefHeight(50);
        teacherInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Button addButton = new Button("Mute");
        addButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        addButton.setPrefHeight(50);

        // Hover
        Animation.addHoverScaleEffectMore(addButton);

        // Action
        addButton.setOnAction(e -> {
            String newTeacher = teacherInput.getText().trim();
            // BACKEND HERE
        });

        HBox.setHgrow(teacherInput, Priority.ALWAYS);
        inputBox.getChildren().addAll(teacherInput, addButton);

        addTeacherBox.getChildren().addAll(header, inputBox);
        return addTeacherBox;

    }

    // ____________________________________________________

    private Node displayRemoveSchool() {

        VBox addTeacherBox = new VBox(15);
        addTeacherBox.setPadding(new Insets(10));
        addTeacherBox.setPrefWidth(Double.MAX_VALUE);
        addTeacherBox.setAlignment(Pos.TOP_LEFT);
        addTeacherBox.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 0 20px 20px 0; -fx-border-radius: 0 20px 20px 0; " +
                "-fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.3, 0, 4);");

        Label header = new Label("Remove School");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #4d4d4d; " +
                "-fx-border-width: 0 0 2px 0; -fx-border-color: orange;");

        HBox inputBox = new HBox(20);
        inputBox.setPrefWidth(400);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Enter name, id or prefix");
        teacherInput.setPrefWidth(300);
        teacherInput.setPrefHeight(50);
        teacherInput.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Button addButton = new Button("Remove");
        addButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        addButton.setPrefHeight(50);

        // Hover
        Animation.addHoverScaleEffectMore(addButton);

        // Action
        addButton.setOnAction(e -> {
            String newTeacher = teacherInput.getText().trim();
            // BACKEND HERE
        });

        HBox.setHgrow(teacherInput, Priority.ALWAYS);
        inputBox.getChildren().addAll(teacherInput, addButton);

        addTeacherBox.getChildren().addAll(header, inputBox);
        return addTeacherBox;

    }

} // Class end
