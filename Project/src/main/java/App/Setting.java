package App;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import App.Animation.*;
import App.Forgot.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Optional;

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


