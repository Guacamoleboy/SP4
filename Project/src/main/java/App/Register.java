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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Register extends Pane {

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

    public Register(int sceneWidth, int sceneHeight){

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.setPrefSize(sceneWidth, sceneHeight);
        this.setStyle("-fx-background-color: "+Main.backgroundColor);

        VBox registerBox = display();
        this.getChildren().add(registerBox);

        // Center
        double boxHeight = 6 * 55 + 5 * 15;
        double layoutX = (sceneWidth - 300) / 2.0;
        double layoutY = (sceneHeight - boxHeight) / 2.0;

        registerBox.setLayoutX(layoutX);
        registerBox.setLayoutY(layoutY);
    }

    // ____________________________________________________

    public VBox display(){

        VBox registerBox = new VBox(15); // Padding / Margin
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setPrefWidth(300); // Center of 600 (Scene)

        HBox buttons = new HBox(15);

        Label loginLabel = new Label("Create Account");
        loginLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: rgba(0,0,0,0.50);");

        usernameField = new TextField();
        usernameField.setPrefHeight(40);
        usernameField.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; -fx-border-color: rgba(0,0,0,0.50);");
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        passwordField.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; -fx-border-color: rgba(0,0,0,0.50);");
        passwordField.setPromptText("Password");

        passwordConfirmField = new PasswordField();
        passwordConfirmField.setPrefHeight(40);
        passwordConfirmField.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; -fx-border-color: rgba(0,0,0,0.50);");
        passwordConfirmField.setPromptText("Password Confirmation");

        emailField = new TextField();
        emailField.setPrefHeight(40);
        emailField.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; -fx-border-color: rgba(0,0,0,0.50);");
        emailField.setPromptText("Email");

        signUpButton = new Button("Sign up");
        signUpButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: rgba(0,0,0,0.50); " +
                "-fx-border-radius: 15px; -fx-background-radius: 15px; -fx-border-color: rgba(0,0,0,0.50);");
        signUpButton.setPrefHeight(30);
        signUpButton.setPrefWidth(150);

        goBackButton = new Button("Go Back");
        goBackButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: rgba(0,0,0,0.50); " +
                "-fx-border-radius: 15px; -fx-background-radius: 15px; -fx-border-color: rgba(0,0,0,0.50);");
        goBackButton.setPrefHeight(30);
        goBackButton.setPrefWidth(150);

        usernameField.setMaxWidth(300);
        passwordField.setMaxWidth(300);
        passwordConfirmField.setMaxWidth(300);
        emailField.setMaxWidth(300);

        // Events
        signUpButton.setOnAction(e -> registerUser());
        goBackButton.setOnAction(e -> goBackButtonAction());

        // HBox
        buttons.getChildren().addAll(signUpButton, goBackButton);

        // Add
        registerBox.getChildren().addAll(loginLabel, usernameField, passwordField, passwordConfirmField, emailField, buttons); // VBox

        return registerBox;

    }

    // ____________________________________________________

    public boolean validateInformation(){

        if(getUsername().isEmpty() || getPassword().isEmpty() || getPasswordConfirmation().isEmpty() || getEmail().isEmpty()){
            alert("Fields must be filled out!");
            return false;
        } else if (!getPasswordConfirmation().equals(getPassword())){
            alert("Passwords don't match..");
            return false;
        } else if (!getEmail().contains("@")){
            alert("Invalid email input");
            return false;
        }

        return true;

    }

    // ____________________________________________________

    public void goBackButtonAction(){

        Login login = new Login(600, 600);
        StartBorder sb = new StartBorder(3);
        StartInfo si = new StartInfo(300, 600);

        HBox goBackHBOX = new HBox(si, sb, login);

        Scene goBackScene = new Scene(goBackHBOX, 900, 600);

        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(goBackScene);

    }

    // ____________________________________________________

    public void registerUser() {
        // ORDER MATTERS. KEEP THAT IN MIND || TOP -> BOTTOM

        if (!validateInformation()) {
            return;
        }

        try {
            // Check if user already exists
            String checkUserSQL = "SELECT * FROM users WHERE username = ? OR email = ?";
            DBConnector db = new DBConnector();
            db.connect("jdbc:sqlite:ElevTiden.sqlite");

            PreparedStatement checkStmt = db.prepareStatement(checkUserSQL);
            checkStmt.setString(1, getUsername());
            checkStmt.setString(2, getEmail());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                alert("Username or email already exists!");
                db.closeConnection();
                return;
            }

            // Create new user
            String insertUserSQL =
                    "INSERT INTO users (username, password, email, status, banned) " +
                            "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement insertStmt = db.prepareStatement(insertUserSQL);
            insertStmt.setString(1, getUsername());
            insertStmt.setString(2, getPassword());
            insertStmt.setString(3, getEmail());
            insertStmt.setString(4, "Offline");
            insertStmt.setString(5, "false");

            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected > 0) {
                alert("Account created successfully!");

                // Go back to login screen
                goBackButtonAction();
            } else {
                alert("Failed to create account. Please try again.");
            }

            db.closeConnection();

        } catch (Exception e) {
            alert("Error: " + e.getMessage());
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    // ____________________________________________________

    public void alert(String msg){

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error!");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

    }

    // ____________________________________________________

    public String getUsername(){
        this.username = usernameField.getText();
        return this.username;
    }

    // ____________________________________________________

    public String getPassword(){
        this.password = passwordField.getText();
        return this.password;
    }

    // ____________________________________________________

    public String getPasswordConfirmation(){
        this.passwordConfirmation = passwordConfirmField.getText();
        return this.passwordConfirmation;
    }

    // ____________________________________________________

    public String getEmail(){
        this.email = emailField.getText();
        return this.email;
    }


    public String getSelectedUserType() {
        return "tissemand lige nu";
    }
} // Login Class End
