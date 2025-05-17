// Packages
package App;

// Imports
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static App.UpdateChecker.*;

public class Login extends Pane {

    // Attributes

    // DATATYPE //
    private int sceneWidth;
    private int sceneHeight;
    private String username;
    private String password;

    // OBJECT //
    private TextField usernameField;
    private FileIO io = new FileIO();
    private PasswordField passwordField;
    private Button loginButton;
    private Button forgotButton;
    private Button registerButton;
    private Button updateVersionButton;
    private ProcessData processdata;

    // ____________________________________________________

    public Login(int sceneWidth, int sceneHeight){
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        // Display setup
        this.setPrefWidth(sceneWidth);
        this.setPrefHeight(sceneHeight);

        // Create
        this.getChildren().add(display());
    }

    // ____________________________________________________

    public VBox display(){
        VBox loginBox = new VBox(15); // Padding / Margin
        loginBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());

        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPrefWidth(300); // Center of 600 (Scene)
        loginBox.setLayoutX(150);
        loginBox.setLayoutY(100);

        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        // Version Control
        if (!Main.upToDate) {
            Label loginLabel = new Label("Outdated ("+getCurrentVersion()+")\nNew version: " + version);
            loginLabel.getStyleClass().add("label");
            updateVersionButton = createStyledButton("Update version", 300);
            updateVersionButton.getStyleClass().add("button");
            buttons.getChildren().addAll(updateVersionButton);
            loginBox.getChildren().addAll(loginLabel, buttons);
            updateVersionButton.setOnAction(e -> updateVersionButtonAction());
            return loginBox;
        }

        Label loginLabel = new Label("Log in");
        loginLabel.getStyleClass().add("label");

        usernameField = new TextField();
        usernameField.setPrefHeight(40);
        usernameField.getStyleClass().add("text-field");
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        passwordField.getStyleClass().add("text-field");
        passwordField.setPromptText("Password");

        // Buttons fix for repeated code
        registerButton = createStyledButton("Create Account", 300);
        loginButton = createStyledButton("Log in", 200);
        forgotButton = createStyledButton("Forgot something?", 300);

        // Hover
        Animation.addHoverScaleEffect(registerButton);
        Animation.addHoverScaleEffect(loginButton);
        Animation.addHoverScaleEffect(forgotButton);

        // Margins
        VBox.setMargin(registerButton, new Insets(30, 0, 0, 0)); // More padding for the register button

        // Add
        buttons.getChildren().addAll(loginButton, forgotButton); // HBox
        loginBox.getChildren().addAll(loginLabel, usernameField, passwordField, buttons, registerButton); // VBox


        // Events
        registerButton.setOnAction(e -> registerButtonAction());
        loginButton.setOnAction(e -> loginButtonAction());
        forgotButton.setOnAction(e -> forgotButtonAction());

        return loginBox;
    }

    // ____________________________________________________

    private Button createStyledButton(String text, double width) {
        Button button = new Button(text);
        button.getStyleClass().add("button");
        button.setPrefHeight(30);
        button.setPrefWidth(width);
        return button;
    }

    // ____________________________________________________

    public void registerButtonAction(){
        // If user presses the Register button!
        Register register = new Register(600, 600);
        StartBorder sb = new StartBorder(3);
        StartInfo si = new StartInfo(300, 600);
        HBox registerHBox = new HBox(register, sb, si);

        si.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        si.getStyleClass().add("orange");

        register.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        register.getStyleClass().add("body");

        Scene registerScene = new Scene(registerHBox, 900, 600);

        Stage stage = (Stage) getScene().getWindow(); // Main window
        stage.setScene(registerScene);
    }

    // ____________________________________________________

    public void forgotButtonAction(){
        // If user presses the Register button!
        Forgot forgot = new Forgot(600, 600);
        StartBorder sb = new StartBorder(3);
        StartInfo si = new StartInfo(300, 600);
        HBox forgotHBox = new HBox(forgot, sb, si);

        si.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        si.getStyleClass().add("orange");

        forgot.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        forgot.getStyleClass().add("body");

        Scene forgotScene = new Scene(forgotHBox, 900, 600);

        Stage stage = (Stage) getScene().getWindow(); // Main window
        stage.setScene(forgotScene);
    }

    // ____________________________________________________

    public void loginButtonAction(){

        String username = getUsername();
        String password = getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Please enter both username and password");
            return;
        }

        if (Main.db.isConnected() && Main.db.validateUser(username, password)) {

            Menu menu = new Menu(username, password, 800, 600);
            Support.setMenu(menu);
            SideMenu sm = new SideMenu(100, 600, menu, Main.db.getRole(username), username);

            sm.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            sm.getStyleClass().add("sideMenu-color");

            menu.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            menu.getStyleClass().add("mainMenu-background");

            HBox mainMenuHBOX = new HBox(sm, menu);
            Scene goBackScene = new Scene(mainMenuHBOX, 900, 600);

            Stage stage = (Stage) getScene().getWindow();
            stage.setScene(goBackScene);

        } else {
            showAlert("Invalid username or password");
        }
    }

    // ____________________________________________________

    private void showAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ____________________________________________________

    /*
        Doesnt download automaticly. It sends you to the URL so
        you can download the correct version.
    */

    public void updateVersionButtonAction() {
        try {
            Desktop desktop = Desktop.getDesktop();
            URI uri = new URI("https://github.com/Guacamoleboy/SP4/tree/main");
            desktop.browse(uri);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
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

    public Button getLoginButton(){
        return this.loginButton;
    }

    // ____________________________________________________

    public Button getForgotButton(){
        return this.forgotButton;
    }

    // ____________________________________________________

    public Button getRegisterButton(){
        return this.registerButton;
    }

    // ____________________________________________________

    public void goToLoginPage(Stage stage){
        Login login = new Login(600, 600);
        StartInfo si = new StartInfo(300, 600);

        si.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        si.getStyleClass().add("orange");

        login.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        login.getStyleClass().add("body");

        HBox goBackHBOX = new HBox(si, login);

        Scene goBackScene = new Scene(goBackHBOX, 900, 600);

        stage.setScene(goBackScene);

    }

}
