// Packages
package App;

// Imports
import javafx.application.Platform;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.DBConnector;

public class Register extends Pane {

    // Attributes
    private TextField usernameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField passwordConfirmField;
    private FileIO io = new FileIO();
    private Button nextButton;
    private Button goBackButton;
    private Button forgotButton;
    private Button registerButton;
    private int sceneWidth;
    private int sceneHeight;
    private String username;
    private String password;
    private String passwordConfirmation;
    private String email;
    private ComboBox<String> dropdownBox;
    private ArrayList <String> userData;
    private DBConnector db;
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";

    // ____________________________________________________

    public Register(int sceneWidth, int sceneHeight){

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.db = new DBConnector();
        db.connect(DB_URL);
        this.setPrefSize(sceneWidth, sceneHeight);

        VBox registerBox = display();
        registerBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        registerBox.getStyleClass().add("body");

        this.getChildren().add(registerBox);

        // Center
        double boxHeight = 7 * 55 + 5 * 15;
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
        loginLabel.getStyleClass().add("label");

        usernameField = new TextField();
        usernameField.setPrefHeight(40);
        usernameField.getStyleClass().add("text-field");
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        passwordField.getStyleClass().add("text-field");
        passwordField.setPromptText("Password");

        passwordConfirmField = new PasswordField();
        passwordConfirmField.setPrefHeight(40);
        passwordConfirmField.getStyleClass().add("text-field");
        passwordConfirmField.setPromptText("Password Confirmation");

        emailField = new TextField();
        emailField.setPrefHeight(40);
        emailField.getStyleClass().add("text-field");
        emailField.setPromptText("Email");

        dropdownBox = new ComboBox<>();
        dropdownBox.getItems().addAll("Student", "Customer");
        dropdownBox.setPromptText("Choose type");
        dropdownBox.getStyleClass().add("combo-box");

        nextButton = new Button("Next");
        nextButton.getStyleClass().add("button");
        nextButton.setPrefHeight(30);
        nextButton.setPrefWidth(150);

        goBackButton = new Button("Go Back");
        goBackButton.getStyleClass().add("button");
        goBackButton.setPrefHeight(30);
        goBackButton.setPrefWidth(150);

        usernameField.setMaxWidth(300);
        passwordField.setMaxWidth(300);
        passwordConfirmField.setMaxWidth(300);
        emailField.setMaxWidth(300);
        dropdownBox.setMaxWidth(300);

        // Border fix
        usernameField.setFocusTraversable(false);
        emailField.setFocusTraversable(false);
        passwordField.setFocusTraversable(false);
        passwordConfirmField.setFocusTraversable(false);
        Platform.runLater(registerBox::requestFocus);

        // Hover
        Animation.addHoverScaleEffect(nextButton);
        Animation.addHoverScaleEffect(goBackButton);

        // Events
        nextButton.setOnAction(e -> nextOption());
        goBackButton.setOnAction(e -> goBackButtonAction());

        // HBox
        buttons.getChildren().addAll(nextButton, goBackButton);

        // VBox
        registerBox.getChildren().addAll(loginLabel, usernameField, passwordField, passwordConfirmField, emailField, dropdownBox, buttons); // VBox

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

    public String getSelectedUserType() {
        return dropdownBox.getValue();
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

    // ____________________________________________________

    public void nextOption(){

        // If user presses the next button!
        NextOption nextoption = new NextOption(600, 600, this);
        StartBorder sb = new StartBorder(3);
        StartInfo si = new StartInfo(300, 600);
        HBox nextOptionHBox = new HBox(nextoption, sb, si);

        si.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        si.getStyleClass().add("orange");

        nextOptionHBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        nextOptionHBox.getStyleClass().add("body");

        Scene registerScene = new Scene(nextOptionHBox, 900, 600);

        Stage stage = (Stage) getScene().getWindow(); // Main window
        stage.setScene(registerScene);

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

    public boolean registerUser(String username, String password, String email) {
        if (!db.isConnected()) {
            db.connect(DB_URL);
        }
        
        try {
            //  username already exists
            String checkQuery = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = db.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                alert("Username already exists!");
                return false;
            }
            
            // inset new user
            String insertQuery = "INSERT INTO users (username, password, email, status, banned) VALUES (?, ?, ?, 'Offline', 'No')";
            PreparedStatement insertStmt = db.prepareStatement(insertQuery);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.setString(3, email);
            
            int result = insertStmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
    }


} // Login Class End
