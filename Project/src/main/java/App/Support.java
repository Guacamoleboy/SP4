// Packages
package App;

// Import
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class Support {

    // Attributes

    // DATATYPE //
    private static final String ticketStatus = "Open";

    // OBJECT //
    private static Menu menu;

    // _______________________________________

    public static VBox displayTickets(Pane p, String username) {

        // VBox
        VBox ticketVBox = new VBox();
        ticketVBox.setLayoutX(20);
        ticketVBox.setLayoutY(100);
        ticketVBox.setPrefWidth(760);
        ticketVBox.setPrefHeight(480);
        ticketVBox.setStyle("-fx-border-color: #464646; -fx-border-width: 2px; -fx-border-radius: 20px; -fx-background-radius: 20px;");

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
        sidebar.setStyle("-fx-background-color: #696969; -fx-border-radius: 20 0 0 20; -fx-background-radius: 20 0 0 20; -fx-border-width: 0 2px 0 0; -fx-border-color: rgba(0, 0, 0, 0.5);");

        // Message load
        Label textLabel = new Label("This is a random text - forstår du det? This is a random text - forstår du det? This is a random text - forstår du det? This is a random text - forstår du det? This is a random text - forstår du det? This is a random text - forstår du det?");
        textLabel.setStyle("-fx-padding: 15px 0 0 15px; -fx-text-fill: #FFF; -fx-font-size: 16px;");
        textLabel.setWrapText(true);

        // HBox for title etc
        HBox senderInfoBox = new HBox();
        senderInfoBox.setAlignment(Pos.CENTER_LEFT);
        senderInfoBox.setPrefHeight(90);
        senderInfoBox.setPrefWidth(760);
        senderInfoBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: #464646; -fx-background-color: #7c7c7c; -fx-border-radius: 0 20px 0 0; -fx-background-radius: 0 20px 0 0;");

        // GREY region
        VBox greyRegion = new VBox();
        greyRegion.setPrefWidth(760 * 0.70);
        greyRegion.setStyle("-fx-background-color: #7c7c7c; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646;");
        greyRegion.setAlignment(Pos.CENTER);

        Label logoutLabel = new Label("Unban mig bro..");
        logoutLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-padding: 20px;");
        greyRegion.getChildren().add(logoutLabel);

        // RED region
        VBox redRegion = new VBox();
        redRegion.setPrefWidth(760 * 0.30);
        redRegion.setStyle("-fx-background-color: rgb(220,160,84); -fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
        redRegion.setAlignment(Pos.CENTER);

        Image profileImage = new Image(Support.class.getResource("/assets/icons/icon16.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(75);
        profileImageView.setFitHeight(75);
        profileImageView.setPreserveRatio(true);
        redRegion.getChildren().add(profileImageView);

        // final HBox add
        senderInfoBox.getChildren().addAll(greyRegion, redRegion);

        // Message area
        VBox messageArea = new VBox(0);
        messageArea.setPrefWidth(760 * 0.74);
        messageArea.setPadding(new Insets(0));
        messageArea.setAlignment(Pos.TOP_LEFT);
        messageArea.setStyle("-fx-background-color: #4b4747; -fx-border-radius: 0 20px 20px 0; -fx-background-radius: 0 20px 20px 0;");
        messageArea.getChildren().addAll(senderInfoBox, textLabel);

        // Add buttons (tickets)
        for (int i = 1; i <= 6; i++) {
            Button ticketButton = new Button(Integer.toString(i));

            // Apply styles
            if (i == 1) {
                ticketButton.getStyleClass().addAll("user-button", "user-button1");
            } else {
                ticketButton.getStyleClass().add("user-button");
            }

            // Apply color to the little line on the right
            if(ticketStatus.equalsIgnoreCase("Open")){
                ticketButton.getStyleClass().add("tickets-open");
            } else if (ticketStatus.equalsIgnoreCase("Closed")){
                ticketButton.getStyleClass().add("tickets-closed");
            }

            sidebar.getChildren().add(ticketButton);
        }

        redRegion.setOnMousePressed(e -> {
            p.getChildren().clear();
            p.getChildren().add(displayResponse(p, username));
        });

        // Build UI
        messageHBox.getChildren().addAll(sidebar, messageArea);
        ticketVBox.getChildren().add(messageHBox);

        return ticketVBox;
    }

    // _______________________________________

    public static VBox displayTicketStatus() {

        VBox outerBox = new VBox(0);
        outerBox.getStylesheets().add(Support.class.getResource("/css/style.css").toExternalForm());
        outerBox.getStyleClass().add("combo-box-book");
        outerBox.setAlignment(Pos.CENTER);
        outerBox.setPrefWidth(220);
        outerBox.setLayoutX(240);
        outerBox.setLayoutY(20);

        ComboBox<String> box1 = new ComboBox<>();
        box1.getItems().addAll("Open", "Closed");
        box1.setPromptText("Status");
        box1.getStyleClass().add("combo-box-ticket");
        box1.setPrefWidth(220);
        box1.setStyle("-fx-alignment: center;");

        // Add the rows to the outer VBox
        outerBox.getChildren().add(box1);

        return outerBox;
    }

    // _______________________________________

    public static VBox displayResponse(Pane p, String username) {

        VBox profileVBox = new VBox();
        profileVBox.getStylesheets().add(Support.class.getResource("/css/style.css").toExternalForm());
        profileVBox.getStyleClass().add("profile-vbox");
        profileVBox.setAlignment(Pos.TOP_CENTER);
        profileVBox.setLayoutX(20);
        profileVBox.setLayoutY(20);
        profileVBox.setPrefWidth(760);
        profileVBox.setPrefHeight(560);
        profileVBox.setStyle("-fx-background-color: #4b4747");

        HBox profilePictureHBox = new HBox();
        profilePictureHBox.setPrefHeight(134);
        profilePictureHBox.setPrefWidth(760);
        profilePictureHBox.setSpacing(0);
        profilePictureHBox.getStylesheets().add(Support.class.getResource("/css/style.css").toExternalForm());
        profilePictureHBox.getStyleClass().add("profilePictureHBox-visuals");
        profilePictureHBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: rgba(0, 0, 0, 0.5);");

        TextArea responseArea = new TextArea("Type your response here...");
        responseArea.getStyleClass().add("text-area");
        responseArea.setWrapText(true);
        responseArea.setPrefHeight(320);
        responseArea.setOnMouseClicked(e -> responseArea.clear());

        HBox placeholderWrapper = new HBox(responseArea);
        placeholderWrapper.setAlignment(Pos.TOP_LEFT);
        placeholderWrapper.setPadding(new Insets(0, 0, 0, 20));

        VBox leftPane = new VBox();
        leftPane.setPrefWidth(190);
        leftPane.setStyle("-fx-background-color: rgb(220,160,84); -fx-background-radius: 20px 0 0 0; -fx-border-radius: 20px 0 0 0; -fx-border-width: 0 2px 0 0; -fx-border-color: rgba(0, 0, 0, 0.5); ");
        leftPane.setAlignment(Pos.CENTER);
        Label ticketLabel = new Label("165112");
        ticketLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #FFF; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0.3, 0, 2);");
        leftPane.getChildren().add(ticketLabel);

        Image sendButtonIcon = new Image(Support.class.getResource("/assets/icons/icon17.png").toExternalForm());
        ImageView sendButtonIconView = new ImageView(sendButtonIcon);
        sendButtonIconView.setFitWidth(50);
        sendButtonIconView.setFitHeight(50);
        sendButtonIconView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0.3, 0, 2);");
        sendButtonIconView.setPreserveRatio(true);

        VBox rightPane = new VBox();
        rightPane.setPrefWidth(570);
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setStyle("-fx-background-color: #D3D3D3FF; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
        Label bannerLabel = new Label("Unban me bro...");
        bannerLabel.setStyle("-fx-font-size: 24px; ; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0.3, 0, 2);");
        rightPane.getChildren().add(bannerLabel);

        profilePictureHBox.getChildren().addAll(leftPane, rightPane);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        HBox bottomHBox = new HBox();
        bottomHBox.setPrefHeight(80);
        bottomHBox.setPrefWidth(760);
        bottomHBox.setAlignment(Pos.CENTER_LEFT);
        bottomHBox.setStyle("-fx-padding: 0; -fx-spacing: 0; -fx-border-width: 2px 0 0 0; -fx-border-color: rgba(0, 0, 0, 0.5);");

        Label statusLabel = new Label("Open");
        statusLabel.setStyle("-fx-text-fill: #FFF !important");

        VBox leftFooter = new VBox();
        leftFooter.setPrefWidth(760 * 0.85);
        leftFooter.setStyle("-fx-background-color: #D3D3D3FF; -fx-background-radius: 0 0 0 20px; -fx-border-radius: 0 0 0 20px;");
        leftFooter.setAlignment(Pos.CENTER_LEFT);

        HBox buttonGroup = new HBox(10);
        buttonGroup.setAlignment(Pos.CENTER_LEFT);
        buttonGroup.setPadding(new Insets(0, 0, 0, 20));

        Button banButton = new Button("Ban");
        Button unbanButton = new Button("Unban");

        // Button visuals
        unbanButton.setStyle("-fx-border-color: black; -fx-border-insets: 0; -fx-background-insets: 0; -fx-border-width: 2px; -fx-background-color: green; -fx-text-fill: white; -fx-font-size: 14px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0.3, 0, 2);");
        banButton.setStyle("-fx-border-color: black; -fx-border-insets: 0; -fx-background-insets: 0; -fx-border-width: 2px; -fx-background-color: red; -fx-text-fill: white; -fx-font-size: 14px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0.3, 0, 2);");

        // Action listener on buttons
        banButton.setOnAction(e -> {
            AdminMenu adminMenu = new AdminMenu(Main.db, username);
            adminMenu.banUser(username);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User has been banned");
            alert.setHeaderText(null); // No header
            alert.setContentText("The user has been banned. Continue with your ticket.");

            alert.showAndWait();
        });

        unbanButton.setOnAction(e -> {
            AdminMenu adminMenu = new AdminMenu(Main.db, username);
            adminMenu.unbanUser(username);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User has been banned");
            alert.setHeaderText(null); // No header
            alert.setContentText("The user has been banned. Continue with your ticket.");

            alert.showAndWait();
        });

        // Hover
        Animation.addHoverScaleEffectMore(banButton);
        Animation.addHoverScaleEffectMore(unbanButton);

        buttonGroup.getChildren().addAll(banButton, unbanButton);
        leftFooter.getChildren().add(buttonGroup);

        VBox rightFooter = new VBox();
        rightFooter.setPrefWidth(760 * 0.15);
        rightFooter.setStyle("-fx-background-color: orange; -fx-border-radius: 0 0 20px 0; -fx-background-radius: 0 0 20px 0; -fx-cursor: hand; -fx-border-width: 0 0 0 2px; -fx-border-color: rgba(0, 0, 0, 0.5);");
        rightFooter.setAlignment(Pos.CENTER);
        rightFooter.getChildren().add(sendButtonIconView);

        rightFooter.setOnMousePressed(e -> {
            p.getChildren().clear();
            p.getChildren().addAll(menu.displayHeader(), Support.displayTicketStatus(), Support.displayTickets(p, username));
        });

        bottomHBox.getChildren().addAll(leftFooter, rightFooter);
        profileVBox.getChildren().addAll(profilePictureHBox, placeholderWrapper, spacer, bottomHBox);

        return profileVBox;
    }

    // _______________________________________

    public static void setMenu(Menu m) {
        menu = m;
    }

}