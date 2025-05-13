package App;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
import java.util.Optional;
import App.Menu.*;

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
        profileImageView.setFitWidth(50);
        profileImageView.setFitHeight(50);
        profileImageView.setPreserveRatio(true);
        redRegion.getChildren().add(profileImageView);

        // final HBox add
        fullWidthHBox.getChildren().addAll(greyRegion, redRegion);

        return fullWidthHBox;
    }

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
        redRegion.setStyle("-fx-background-color: rgba(176,72,72,0.73); -fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
        redRegion.setAlignment(Pos.CENTER);

        Image profileImage = new Image(getClass().getResource("/assets/icons/icon12.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(75);
        profileImageView.setFitHeight(75);
        profileImageView.setPreserveRatio(true);
        redRegion.getChildren().add(profileImageView);

        // final HBox add
        fullWidthHBox.getChildren().addAll(greyRegion, redRegion);

        return fullWidthHBox;
    }

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
        profileImageView.setFitWidth(75);
        profileImageView.setFitHeight(75);
        profileImageView.setPreserveRatio(true);
        redRegion.getChildren().add(profileImageView);

        // final HBox add
        fullWidthHBox.getChildren().addAll(greyRegion, redRegion);

        return fullWidthHBox;
    }

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
        profileImageView.setFitWidth(75);
        profileImageView.setFitHeight(75);
        profileImageView.setPreserveRatio(true);
        redRegion.getChildren().add(profileImageView);

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

} // Setting end


