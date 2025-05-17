// Packages
package App;

// Imports
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

    // DATATYPE //
    private int sceneWidth;
    private int sceneHeight;
    private String username;
    private String password;
    private String passwordConfirmation;
    private String email;

    // OBJECT //
    private TextField usernameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField passwordConfirmField;
    private FileIO io = new FileIO();
    private Button usernameButton;
    private Button emailButton;
    private Button passwordButton;
    private Button goBackButton;
    private Button resetButton;
    private Button forgotButton;
    private Button registerButton;
    private ArrayList <String> userData; // IDIOT
    private VBox forgotBox;

    // ____________________________________________________

    public Forgot(int sceneWidth, int sceneHeight){

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.setPrefSize(sceneWidth, sceneHeight);

        forgotBox = initialDisplay();
        this.getChildren().add(forgotBox);
        forgotBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        // Center
        double boxWidth = 450;
        double boxHeight = 4 * 55 + 5 * 15;
        double layoutX = (sceneWidth - boxWidth) / 2.0;
        double layoutY = (sceneHeight - boxHeight) / 2.0;

        forgotBox.setLayoutX(layoutX);
        forgotBox.setLayoutY(layoutY);
    }

    // ____________________________________________________

    public VBox initialDisplay(){

        VBox registerBox = new VBox(15); // Padding / Margin
        registerBox.setAlignment(Pos.TOP_CENTER);
        registerBox.setPrefWidth(450); // Center of 600 (Scene)

        HBox buttons = new HBox(15);

        Label forgotLabel = new Label("Choose");
        forgotLabel.getStyleClass().add("label");

        usernameButton = new Button("Username");
        usernameButton.getStyleClass().add("button");
        usernameButton.setStyle("-fx-font-size: 18px;");
        usernameButton.setPrefHeight(30);
        usernameButton.setPrefWidth(150);

        emailButton = new Button("Email");
        emailButton.getStyleClass().add("button");
        emailButton.setStyle("-fx-font-size: 18px;");
        emailButton.setPrefHeight(30);
        emailButton.setPrefWidth(150);

        passwordButton = new Button("Password");
        passwordButton.getStyleClass().add("button");
        passwordButton.setStyle("-fx-font-size: 18px;");
        passwordButton.setPrefHeight(30);
        passwordButton.setPrefWidth(150);

        // Hover
        Animation.addHoverScaleEffect(usernameButton);
        Animation.addHoverScaleEffect(emailButton);
        Animation.addHoverScaleEffect(passwordButton);

        // HBox ADD
        buttons.getChildren().addAll(usernameButton, emailButton, passwordButton);
        buttons.setAlignment(Pos.CENTER);

        // Add
        registerBox.getChildren().addAll(forgotLabel, buttons); // VBox

        usernameButton.setOnAction(e -> {
            forgotBox.getChildren().clear();
            forgotBox.getChildren().add(displayUsername());
        });

        emailButton.setOnAction(e -> {
            forgotBox.getChildren().clear();
            forgotBox.getChildren().add(displayEmail());
        });

        passwordButton.setOnAction(e -> {
            forgotBox.getChildren().clear();
            forgotBox.getChildren().add(displayPassword());
        });

        return registerBox;

    }

    // ____________________________________________________

    public VBox displayUsername(){

        VBox registerBox = new VBox(15); // Padding / Margin
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setPrefWidth(450); // Center of 600 (Scene)

        HBox buttons = new HBox(15);

        Label forgotLabel = new Label("Type your email");
        forgotLabel.getStyleClass().add("label");

        usernameField = new TextField();
        usernameField.setPrefHeight(40);
        usernameField.getStyleClass().add("text-field");
        usernameField.setPromptText("Email");

        resetButton = new Button("Reset");
        resetButton.getStyleClass().add("button");
        resetButton.setPrefHeight(30);
        resetButton.setPrefWidth(150);

        goBackButton = new Button("Go Back");
        goBackButton.getStyleClass().add("button");
        goBackButton.setPrefHeight(30);
        goBackButton.setPrefWidth(150);

        // Hover
        Animation.addHoverScaleEffect(resetButton);
        Animation.addHoverScaleEffect(goBackButton);

        // HBox ADD
        buttons.getChildren().addAll(resetButton, goBackButton);
        buttons.setAlignment(Pos.CENTER);

        // Add
        registerBox.getChildren().addAll(forgotLabel, usernameField, buttons); // VBox

        goBackButton.setOnAction(e -> {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Main.loginPage(stage);
        });

        return registerBox;

    }

    // ____________________________________________________

    public VBox displayPassword(){

        VBox registerBox = new VBox(15); // Padding / Margin
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setPrefWidth(450); // Center of 600 (Scene)

        HBox buttons = new HBox(15);

        Label forgotLabel = new Label("Reset Password");
        forgotLabel.getStyleClass().add("label");

        usernameField = new TextField();
        usernameField.setPrefHeight(40);
        usernameField.getStyleClass().add("text-field");
        usernameField.setPromptText("Email");

        resetButton = new Button("Reset");
        resetButton.getStyleClass().add("button");
        resetButton.setPrefHeight(30);
        resetButton.setPrefWidth(150);

        goBackButton = new Button("Go Back");
        goBackButton.getStyleClass().add("button");
        goBackButton.setPrefHeight(30);
        goBackButton.setPrefWidth(150);

        // Hover
        Animation.addHoverScaleEffect(resetButton);
        Animation.addHoverScaleEffect(goBackButton);

        // HBox ADD
        buttons.getChildren().addAll(resetButton, goBackButton);
        buttons.setAlignment(Pos.CENTER);

        // Add
        registerBox.getChildren().addAll(forgotLabel, usernameField, buttons); // VBox

        goBackButton.setOnAction(e -> {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Main.loginPage(stage);
        });

        return registerBox;

    }

    // ____________________________________________________

    public VBox displayEmail(){

        VBox registerBox = new VBox(15); // Padding / Margin
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setPrefWidth(450); // Center of 600 (Scene)

        HBox buttons = new HBox(15);

        Label forgotLabel = new Label("Type your username");
        forgotLabel.getStyleClass().add("label");

        usernameField = new TextField();
        usernameField.setPrefHeight(40);
        usernameField.getStyleClass().add("text-field");
        usernameField.setPromptText("Username");

        resetButton = new Button("Reset");
        resetButton.getStyleClass().add("button");
        resetButton.setPrefHeight(30);
        resetButton.setPrefWidth(150);

        goBackButton = new Button("Go Back");
        goBackButton.getStyleClass().add("button");
        goBackButton.setPrefHeight(30);
        goBackButton.setPrefWidth(150);

        // Hover
        Animation.addHoverScaleEffect(resetButton);
        Animation.addHoverScaleEffect(goBackButton);

        // HBox ADD
        buttons.getChildren().addAll(resetButton, goBackButton);
        buttons.setAlignment(Pos.CENTER);

        // Add
        registerBox.getChildren().addAll(forgotLabel, usernameField, buttons); // VBox

        goBackButton.setOnAction(e -> {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Main.loginPage(stage);
        });

        return registerBox;

    }

} // Login Class End
