package App;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.*;
import java.util.Optional;
import App.Menu.*;
import javafx.util.Duration;

public class Setting extends Pane {

    // Attributes

    // OBJECT //
    Connection con;
    private Alert alert;
    private String username;

    public Setting(String username){
        this.username = username;
    }

    // _____________________________________________

    private boolean acceptAlert(String title, String message) {

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> response = alert.showAndWait();

        return response.isPresent() && response.get() == ButtonType.OK;
    }

    // _____________________________________________

    protected HBox displayLogOutJonas() {

        // HBox
        HBox fullWidthHBox = new HBox();
        fullWidthHBox.setAlignment(Pos.CENTER_LEFT);
        fullWidthHBox.setPrefHeight(134);
        fullWidthHBox.setPrefWidth(760);
        fullWidthHBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: #464646;");

        // GREY region
        VBox greyRegion = new VBox();
        greyRegion.setPrefWidth(760 * 0.70);
        greyRegion.setStyle("-fx-background-color: #7c7c7c; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646;");
        greyRegion.setAlignment(Pos.CENTER);

        Label logoutLabel = new Label("Log Out");
        logoutLabel.setStyle("-fx-font-size: 25px; -fx-text-fill: white; -fx-padding: 20px;");
        greyRegion.getChildren().add(logoutLabel);

        // RED region
        VBox redRegion = new VBox();
        redRegion.setPrefWidth(760 * 0.30);
        redRegion.setStyle("-fx-background-color: orange; -fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
        redRegion.setAlignment(Pos.CENTER);

        Image profileImage = new Image(getClass().getResource("/assets/icons/icon11.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        applyDropShadowEffect(profileImageView, 50, 50, 0.7);
        redRegion.getChildren().add(profileImageView);

        // :hover on imageView
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), profileImageView);
        scaleUp.setToX(1.08);
        scaleUp.setToY(1.08);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), profileImageView);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Action listener on imageView only *dab*
        redRegion.setOnMouseEntered(e -> scaleUp.playFromStart());
        redRegion.setOnMouseExited(e -> scaleDown.playFromStart());

        // final HBox add
        fullWidthHBox.getChildren().addAll(greyRegion, redRegion);

        return fullWidthHBox;
    }

    // _____________________________________________

    protected HBox displayDeleteJonas() {

        // HBox
        HBox fullWidthHBox = new HBox();
        fullWidthHBox.setAlignment(Pos.CENTER_LEFT);
        fullWidthHBox.setPrefHeight(134);
        fullWidthHBox.setPrefWidth(760);
        fullWidthHBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: #464646;");

        // GREY region
        VBox greyRegion = new VBox();
        greyRegion.setPrefWidth(760 * 0.70);
        greyRegion.setStyle("-fx-background-color: #7c7c7c; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646;");
        greyRegion.setAlignment(Pos.CENTER);

        Label logoutLabel = new Label("Delete account");
        logoutLabel.setStyle("-fx-font-size: 25px; -fx-text-fill: white; -fx-padding: 20px;");
        greyRegion.getChildren().add(logoutLabel);

        // RED region
        VBox redRegion = new VBox();
        redRegion.setPrefWidth(760 * 0.30);
        String normalBackground = "rgba(176,72,72,0.73)"; // Light red (Default lol)
        String hoverBackground = "rgba(120,30,30,0.9)"; // Dark red
        redRegion.setStyle("-fx-background-color: " + normalBackground + "; -fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
        redRegion.setAlignment(Pos.CENTER);

        // Image swap
        Image iconNormal = new Image(getClass().getResource("/assets/icons/icon13.png").toExternalForm());
        Image iconHover = new Image(getClass().getResource("/assets/icons/icon12.png").toExternalForm());

        ImageView profileImageView = new ImageView(iconNormal);
        applyDropShadowEffect(profileImageView, 75, 75, 0.7);
        redRegion.getChildren().add(profileImageView);

        // Action listener on enter / exit
        redRegion.setOnMouseEntered(e -> {
            profileImageView.setImage(iconHover);
            redRegion.setStyle("-fx-background-color: " + hoverBackground + "; -fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
        });
        redRegion.setOnMouseExited(e -> {
            profileImageView.setImage(iconNormal);
            redRegion.setStyle("-fx-background-color: " + normalBackground + "; -fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
        });

        // final HBox add
        fullWidthHBox.getChildren().addAll(greyRegion, redRegion);

        return fullWidthHBox;
    }



    // _____________________________________________

    protected HBox displayDarkmodeJonas() {

        // HBox
        HBox fullWidthHBox = new HBox();
        fullWidthHBox.setAlignment(Pos.CENTER_LEFT);
        fullWidthHBox.setPrefHeight(134);
        fullWidthHBox.setPrefWidth(760);
        fullWidthHBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: #464646;");

        // GREY region
        VBox greyRegion = new VBox();
        greyRegion.setPrefWidth(760 * 0.70);
        greyRegion.setStyle("-fx-background-color: #7c7c7c; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646;");
        greyRegion.setAlignment(Pos.CENTER);

        Label logoutLabel = new Label("Darkmode Active");
        logoutLabel.setStyle("-fx-font-size: 25px; -fx-text-fill: white; -fx-padding: 20px;");
        greyRegion.getChildren().add(logoutLabel);

        // RED region
        VBox redRegion = new VBox();
        redRegion.setPrefWidth(760 * 0.30);
        redRegion.setStyle("-fx-background-color: rgb(50,64,112); -fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
        redRegion.setAlignment(Pos.CENTER);

        Image profileImage = new Image(getClass().getResource("/assets/icons/icon15.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        redRegion.getChildren().add(profileImageView);
        applyDropShadowEffect(profileImageView, 75, 75, 0.7);

        // final HBox add
        fullWidthHBox.getChildren().addAll(greyRegion, redRegion);

        return fullWidthHBox;
    }

    // _____________________________________________

    private void applyDropShadowEffect(ImageView imageView, double width, double height, double opacity) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        dropShadow.setColor(Color.color(0, 0, 0, opacity));

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        imageView.setEffect(dropShadow);
    }

    // _____________________________________________

    protected HBox displayLightmodeJonas() {

        // HBox
        HBox fullWidthHBox = new HBox();
        fullWidthHBox.setAlignment(Pos.CENTER_LEFT);
        fullWidthHBox.setPrefHeight(134);
        fullWidthHBox.setPrefWidth(760);
        fullWidthHBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: #464646;");

        // GREY region
        VBox greyRegion = new VBox();
        greyRegion.setPrefWidth(760 * 0.70);
        greyRegion.setStyle("-fx-background-color: #7c7c7c; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646;");
        greyRegion.setAlignment(Pos.CENTER);

        Label logoutLabel = new Label("Lightmode Active");
        logoutLabel.setStyle("-fx-font-size: 25px; -fx-text-fill: white; -fx-padding: 20px;");
        greyRegion.getChildren().add(logoutLabel);

        // RED region
        VBox redRegion = new VBox();
        redRegion.setPrefWidth(760 * 0.30);
        redRegion.setStyle("-fx-background-color: rgb(222,201,104); -fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
        redRegion.setAlignment(Pos.CENTER);

        Image profileImage = new Image(getClass().getResource("/assets/icons/icon14.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        applyDropShadowEffect(profileImageView, 75, 75, 0.7);
        redRegion.getChildren().add(profileImageView);

        // final HBox add
        fullWidthHBox.getChildren().addAll(greyRegion, redRegion);

        return fullWidthHBox;
    }

    // _____________________________________________

    protected HBox displayLanguage(String language) {

        // HBox
        HBox fullWidthHBox = new HBox();
        fullWidthHBox.setAlignment(Pos.CENTER_LEFT);
        fullWidthHBox.setPrefHeight(134);
        fullWidthHBox.setPrefWidth(760);
        fullWidthHBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: #464646;");

        Image profileImage = new Image(getClass().getResource("/assets/flag/" + language + ".png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        applyDropShadowEffect(profileImageView, 75, 75, 0.7);

        // GREY region
        VBox greyRegion = new VBox();
        greyRegion.setPrefWidth(760 * 0.70);
        greyRegion.setStyle("-fx-background-color: #7c7c7c; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646;");
        greyRegion.setAlignment(Pos.CENTER);

        Label logoutLabel = new Label("Change language here...");
        logoutLabel.setStyle("-fx-font-size: 25px; -fx-text-fill: white; -fx-padding: 20px;");
        greyRegion.getChildren().add(logoutLabel);

        // RED region
        VBox redRegion = new VBox();
        redRegion.setPrefWidth(760 * 0.30);
        redRegion.setStyle("-fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
        redRegion.setAlignment(Pos.CENTER);
        redRegion.getChildren().add(profileImageView);

        switch(language.toLowerCase()) {
            case "english":
                redRegion.setStyle(redRegion.getStyle() + "-fx-background-color: rgb(52,71,117);");
                break;
            case "spanish":
                redRegion.setStyle(redRegion.getStyle() + "-fx-background-color: rgb(229, 69, 69);");
                break;
            case "nigger":
                redRegion.setStyle(redRegion.getStyle() + "-fx-background-color: rgb(255, 153, 51);");
                break;
            case "danish":
                redRegion.setStyle(redRegion.getStyle() + "-fx-background-color: rgb(198, 12, 48);");
                break;
            default:
                redRegion.setStyle(redRegion.getStyle() + "-fx-background-color: #e54545;");
        }

        // final HBox add
        fullWidthHBox.getChildren().addAll(greyRegion, redRegion);

        return fullWidthHBox;
    }

    // _____________________________________________

    protected VBox displayLogOut(){

        VBox logOutBox = new VBox(15); // Spacing between each element
        logOutBox.setAlignment(Pos.TOP_CENTER);
        logOutBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        logOutBox.getStyleClass().add("deletBox-fix");

        Label logOutText = new Label("Log out");
        logOutText.getStyleClass().add("deletetext-visuals");

        Button logOutButton = new Button("take me out");
        logOutButton.setMaxWidth(150);
        logOutButton.getStyleClass().add("deleteYesButton-visuals");

        // Hover
        Animation.addHoverScaleEffectMore(logOutButton);

        // Action
        //Stage stage = (Stage) getScene().getWindow();
        logOutButton.setOnAction(e -> {
            if (acceptAlert("Logging out", "Are you sure?")) { // (TITLE, MESSAGE) FUCK JER
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                Main.loginPage(stage);
            }
        });

        // Add to VBox
        logOutBox.getChildren().addAll(logOutText, logOutButton);

        return logOutBox;

    }

    // _____________________________________________

    protected VBox displayDelete(){

        VBox deleteBox = new VBox(15); // Spacing between each element
        deleteBox.setAlignment(Pos.TOP_CENTER);
        deleteBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        deleteBox.getStyleClass().add("deletBox-fix");

        Label deleteText = new Label("Delete account?");
        deleteText.getStyleClass().add("deletetext-visuals");

        Button deleteButton = new Button("Yes");
        deleteButton.setMaxWidth(80);
        deleteButton.getStyleClass().add("deleteYesButton-visuals");

        // Hover
        Animation.addHoverScaleEffectMore(deleteButton);

        // Action
        deleteButton.setOnAction(e -> {
            if (acceptAlert("Delete Account?", "Are you sure you want to delete your account? This can't be undone...")) {
                Main.db.deleteAccount(username);
            };
        });

        // Add to VBox
        deleteBox.getChildren().addAll(deleteText, deleteButton);

        return deleteBox;

    }

    // _____________________________________________

    protected static HBox displayBanUser() {

        // HBox
        HBox fullWidthHBox = new HBox();
        fullWidthHBox.setAlignment(Pos.CENTER_LEFT);
        fullWidthHBox.setPrefHeight(90);
        fullWidthHBox.setPrefWidth(760);
        fullWidthHBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: #464646;");

        // GREY region
        VBox searchRegion = new VBox();
        searchRegion.setPrefWidth(760 * 0.40);
        searchRegion.setStyle("-fx-background-color: #7c7c7c; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646;");
        searchRegion.setAlignment(Pos.CENTER);

        VBox greyRegion = new VBox();
        greyRegion.setPrefWidth(760 * 0.35);
        greyRegion.setStyle("-fx-background-color: #989898; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646;");
        greyRegion.setAlignment(Pos.CENTER);

        Label banStatusLabel = new Label("Not Banned");
        banStatusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-padding: 20px;");
        greyRegion.getChildren().add(banStatusLabel);

        TextArea usernameUnbanLabel = new TextArea("Enter Username");
        usernameUnbanLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        usernameUnbanLabel.getStyleClass().add("displayBanUser-label");
        searchRegion.getChildren().add(usernameUnbanLabel);

        // RED region
        VBox redRegion = new VBox();
        redRegion.setPrefWidth(760 * 0.25);
        redRegion.setStyle("-fx-background-color: orange; -fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646;");
        redRegion.setAlignment(Pos.CENTER);

        Image profileImage = new Image(Setting.class.getResource("/assets/icons/icon18.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(50);
        profileImageView.setFitHeight(50);
        profileImageView.setPreserveRatio(true);
        redRegion.getChildren().add(profileImageView);

        // final HBox add
        fullWidthHBox.getChildren().addAll(searchRegion, greyRegion, redRegion);

        return fullWidthHBox;
    }

} // Setting end


