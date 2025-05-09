// Packages
package App;

// Imports
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.*;

import java.util.ArrayList;

public class Forgot extends Pane {

    // Attributes
    private TextField usernameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField passwordConfirmField;
    private FileIO io = new FileIO();
    private Button signUpButton;
    private Button goBackButton;
    private Button forgotButton;
    private Button registerButton;
    private int sceneWidth;
    private int sceneHeight;
    private String username;
    private String password;
    private String passwordConfirmation;
    private String email;
    private ArrayList <String> userData;

    // ____________________________________________________

    public Forgot(int sceneWidth, int sceneHeight){

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.setPrefSize(sceneWidth, sceneHeight);

        VBox forgotBox = display();
        this.getChildren().add(forgotBox);
        forgotBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        // Center
        double boxHeight = 4 * 55 + 5 * 15;
        double layoutX = (sceneWidth - 300) / 2.0;
        double layoutY = (sceneHeight - boxHeight) / 2.0;

        forgotBox.setLayoutX(layoutX);
        forgotBox.setLayoutY(layoutY);
    }

    // ____________________________________________________

    public VBox display(){

        VBox registerBox = new VBox(15); // Padding / Margin
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setPrefWidth(300); // Center of 600 (Scene)

        HBox buttons = new HBox(15);

        Label forgotLabel = new Label("Forgot something?");
        forgotLabel.getStyleClass().add("label");

        usernameField = new TextField();
        usernameField.setPrefHeight(40);
        usernameField.getStyleClass().add("text-field");
        usernameField.setPromptText("Username");

        emailField = new TextField();
        emailField.setPrefHeight(40);
        emailField.getStyleClass().add("text-field");
        emailField.setPromptText("Email");

        signUpButton = new Button("Reset");
        signUpButton.getStyleClass().add("button");
        signUpButton.setPrefHeight(30);
        signUpButton.setPrefWidth(150);

        goBackButton = new Button("Go Back");
        goBackButton.getStyleClass().add("button");
        goBackButton.setPrefHeight(30);
        goBackButton.setPrefWidth(150);

        // Hover
        Animation.addHoverScaleEffect(signUpButton);
        Animation.addHoverScaleEffect(goBackButton);

        // HBox ADD
        buttons.getChildren().addAll(signUpButton, goBackButton);

        // Add
        registerBox.getChildren().addAll(forgotLabel, usernameField, emailField, buttons); // VBox

        goBackButton.setOnAction(e -> goBackButtonAction());

        return registerBox;

    }

    // ____________________________________________________

    public void goBackButtonAction(){
        Login login = new Login(600, 600);
        StartBorder sb = new StartBorder(3);
        StartInfo si = new StartInfo(300, 600);

        si.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        si.getStyleClass().add("orange");

        login.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        login.getStyleClass().add("body");

        HBox goBackHBOX = new HBox(si, sb, login);

        Scene goBackScene = new Scene(goBackHBOX, 900, 600);

        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(goBackScene);
    }

} // Login Class End
