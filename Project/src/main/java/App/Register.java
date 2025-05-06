// Packages
package App;

// Imports
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.*;

import java.util.ArrayList;

public class Register extends VBox {

    // Attributes
    private TextField usernameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField passwordConfirmField;
    private FileIO io = new FileIO();
    private Button signUpButton;
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
        this.setStyle("-fx-background-color: #AAAAAAFF");

        // Create
        this.getChildren().add(display());

    } // Constructor end

    // ____________________________________________________

    public VBox display(){

        VBox loginBox = new VBox(15); // Padding / Margin
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPrefWidth(400); // Center of 600 (Scene)

        Label loginLabel = new Label("Create Account");
        loginLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: rgba(0,0,0,0.50);");

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
        signUpButton.setPrefWidth(80);

        // Events
        signUpButton.setOnAction(e -> registerUser());

        // Add
        loginBox.getChildren().addAll(loginLabel, usernameField, passwordField, passwordConfirmField, emailField, signUpButton); // VBox

        return loginBox;

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

    public void registerUser(){

        // ORDER MATTERS. KEEP THAT IN MIND || TOP -> BOTTOM

        userData = new ArrayList<>();

        if(!validateInformation()){
            return;
        }

        String status = "Active";
        String banned = "No";

        userData.add(getUsername());
        userData.add(getPassword());
        userData.add(getEmail());
        userData.add(status);
        userData.add(banned);

        io.saveData(userData, "src/main/java/data/userData.csv", "Username, Password, Email, Status, Banned");

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

} // Login Class End
