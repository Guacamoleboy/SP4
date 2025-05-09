// Packages
package App;

// Import
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.DBConnector;

public class NextOption extends Pane {

    // Attributes
    private Button nextButton;
    private Button goBackButton;
    private Button tryAgainButton;
    private Button registerButton;
    private int sceneWidth;
    private int sceneHeight;
    private ComboBox<String> hairTypeDropdown;
    private ComboBox<String> genderDropdown;
    private ComboBox<String> lengthDropdown;
    private ComboBox<String> hairColorDropdown;
    private ComboBox<String> schoolDropdown;
    private ComboBox<String> yearDropdown;
    private Register register;
    private DBConnector db;
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";

    // ____________________________________________________

    public NextOption(int sceneWidth, int sceneHeight, Register register){

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.register = register;
        this.setPrefSize(sceneWidth, sceneHeight);

        this.db = new DBConnector();
        db.connect(DB_URL);

        VBox registerBox;

        String userType = register.getSelectedUserType();

        if ("Student".equalsIgnoreCase(userType)) {
            registerBox = displayStudent();
        } else if ("Customer".equalsIgnoreCase(userType)) {
            registerBox = displayCustomer();
        } else {
            registerBox = displayError();
        }

        registerBox.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        registerBox.getStyleClass().add("body");

        this.getChildren().add(registerBox);

        // Center
        double boxHeight = 4 * 55 + 5 * 15;
        double layoutX = (sceneWidth - 300) / 2.0;
        double layoutY = (sceneHeight - boxHeight) / 2.0;

        registerBox.setLayoutX(layoutX);
        registerBox.setLayoutY(layoutY);
    }

    // ____________________________________________________

    public VBox displayCustomer(){

        VBox registerBox = new VBox(15); // Padding / Margin
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setPrefWidth(300); // Center of 600 (Scene)

        HBox buttons = new HBox(15);

        Label customerLabel = new Label("Customer");
        customerLabel.getStyleClass().add("label");

        hairTypeDropdown = new ComboBox<>();
        hairTypeDropdown.getItems().addAll("Straight", "Wavy", "Curly", "Coily");
        hairTypeDropdown.setPromptText("Choose Hairtype");
        hairTypeDropdown.getStyleClass().add("combo-box");

        hairColorDropdown = new ComboBox<>();
        hairColorDropdown.getItems().addAll("Blonde", "Black", "Brown", "Red Hair", "Grey");
        hairColorDropdown.setPromptText("Hair Color");
        hairColorDropdown.getStyleClass().add("combo-box");

        lengthDropdown = new ComboBox<>();
        lengthDropdown.getItems().addAll("Bald", "Buzz", "Short", "Medium", "Lengthy", "Long", "Weird Length");
        lengthDropdown.setPromptText("Hair Length");
        lengthDropdown.getStyleClass().add("combo-box");

        genderDropdown = new ComboBox<>();
        genderDropdown.getItems().addAll("Male", "Female");
        genderDropdown.setPromptText("Gender");
        genderDropdown.getStyleClass().add("combo-box");

        goBackButton = new Button("Go Back");
        goBackButton.getStyleClass().add("button");
        goBackButton.setPrefHeight(30);
        goBackButton.setPrefWidth(150);

        registerButton = new Button("Register");
        registerButton.getStyleClass().add("button");
        registerButton.setPrefHeight(30);
        registerButton.setPrefWidth(150);

        // Width of ComboBox
        hairTypeDropdown.setMaxWidth(300);
        lengthDropdown.setMaxWidth(300);
        hairColorDropdown.setMaxWidth(300);
        genderDropdown.setMaxWidth(300);

        // Hover
        Animation.addHoverScaleEffect(goBackButton);
        Animation.addHoverScaleEffect(registerButton);

        // Actions
        goBackButton.setOnAction(e -> goBackButtonAction());
        registerButton.setOnAction(e -> registerButtonAction());

        // HBox
        buttons.getChildren().addAll(goBackButton, registerButton);

        // VBox
        registerBox.getChildren().addAll(customerLabel, hairTypeDropdown, hairColorDropdown, lengthDropdown, genderDropdown, buttons); // VBox

        return registerBox;

    }

    // ____________________________________________________

    public VBox displayStudent(){

        VBox registerBox = new VBox(15); // Padding / Margin
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setPrefWidth(300); // Center of 600 (Scene)

        HBox buttons = new HBox(15);

        Label studentLabel = new Label("Student");
        studentLabel.getStyleClass().add("label");

        schoolDropdown = new ComboBox<>();
        schoolDropdown.getItems().addAll("Next", "Idk", "Idk", "Idk");
        schoolDropdown.setPromptText("School");
        schoolDropdown.getStyleClass().add("combo-box");

        yearDropdown = new ComboBox<>();
        yearDropdown.getItems().addAll("New", "1", "2", "3");
        yearDropdown.setPromptText("Student Year");
        yearDropdown.getStyleClass().add("combo-box");

        goBackButton = new Button("Go Back");
        goBackButton.getStyleClass().add("button");
        goBackButton.setPrefHeight(30);
        goBackButton.setPrefWidth(150);

        registerButton = new Button("Register");
        registerButton.getStyleClass().add("button");
        registerButton.setPrefHeight(30);
        registerButton.setPrefWidth(150);

        // Hover
        Animation.addHoverScaleEffect(goBackButton);
        Animation.addHoverScaleEffect(registerButton);

        // Actions
        goBackButton.setOnAction(e -> goBackButtonAction());
        registerButton.setOnAction(e -> registerButtonAction());

        // Widths
        schoolDropdown.setMaxWidth(300);
        yearDropdown.setMaxWidth(300);

        // HBox
        buttons.getChildren().addAll(goBackButton, registerButton);

        // VBox
        registerBox.getChildren().addAll(studentLabel, schoolDropdown, yearDropdown, buttons); // VBox

        return registerBox;

    }

    // ____________________________________________________

    public VBox displayError(){

        VBox registerBox = new VBox(15); // Padding / Margin
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setPrefWidth(300); // Center of 600 (Scene)

        HBox buttons = new HBox(15);

        Label errorLabel = new Label("Error - Please choose a type!");
        errorLabel.getStyleClass().add("label-2");

        tryAgainButton = new Button("Try again");
        tryAgainButton.getStyleClass().add("button");
        tryAgainButton.setPrefHeight(30);
        tryAgainButton.setPrefWidth(300);

        // Hover
        Animation.addHoverScaleEffect(tryAgainButton);

        // Actions
        tryAgainButton.setOnAction(e -> goBackButtonAction());

        // VBox
        registerBox.getChildren().addAll(errorLabel, tryAgainButton); // VBox

        return registerBox;

    }

    // ____________________________________________________

    public void goBackButtonAction(){

        Register register = new Register(600, 600);
        StartBorder sb = new StartBorder(3);
        StartInfo si = new StartInfo(300, 600);

        si.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        si.getStyleClass().add("orange");

        register.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        register.getStyleClass().add("body");

        HBox goBackHBOX = new HBox(register, sb, si);

        Scene goBackScene = new Scene(goBackHBOX, 900, 600);

        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(goBackScene);

    }

    // ____________________________________________________

    public void registerButtonAction(){
        String username = register.getUsername();
        String password = register.getPassword();
        String email = register.getEmail();
        String userType = register.getSelectedUserType();
        
        if (db.isConnected()) {
            try {
                // Insert userdata
                String insertUserQuery = "INSERT INTO users (username, password, email, status, banned) VALUES (?, ?, ?, 'Offline', 'No')";
                PreparedStatement insertUserStmt = db.prepareStatement(insertUserQuery);
                insertUserStmt.setString(1, username);
                insertUserStmt.setString(2, password);
                insertUserStmt.setString(3, email);
                insertUserStmt.executeUpdate();
                
                // Insert for type
                if ("Customer".equalsIgnoreCase(userType)) {
                    String hairType = hairTypeDropdown.getValue();
                    String hairColor = hairColorDropdown.getValue();
                    String length = lengthDropdown.getValue();
                    String gender = genderDropdown.getValue();
                    
                    String insertCustomerQuery = "INSERT INTO customers (username, hair_type, hair_color, length, gender) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement insertCustomerStmt = db.prepareStatement(insertCustomerQuery);
                    insertCustomerStmt.setString(1, username);
                    insertCustomerStmt.setString(2, hairType);
                    insertCustomerStmt.setString(3, hairColor);
                    insertCustomerStmt.setString(4, length);
                    insertCustomerStmt.setString(5, gender);
                    insertCustomerStmt.executeUpdate();
                } else if ("Student".equalsIgnoreCase(userType)) {
                    String school = schoolDropdown.getValue();
                    String year = yearDropdown.getValue();
                    
                    String insertStudentQuery = "INSERT INTO students (username, school, year) VALUES (?, ?, ?)";
                    PreparedStatement insertStudentStmt = db.prepareStatement(insertStudentQuery);
                    insertStudentStmt.setString(1, username);
                    insertStudentStmt.setString(2, school);
                    insertStudentStmt.setString(3, year);
                    insertStudentStmt.executeUpdate();
                }
                
                // go to login screen
                Login login = new Login(600, 600);
                StartBorder sb = new StartBorder(3);
                StartInfo si = new StartInfo(300, 600);
                
                si.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                si.getStyleClass().add("orange");
                
                login.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                login.getStyleClass().add("body");
                
                HBox loginHBox = new HBox(si, sb, login);
                Scene loginScene = new Scene(loginHBox, 900, 600);
                
                Stage stage = (Stage) getScene().getWindow();
                stage.setScene(loginScene);
                
            } catch (SQLException e) {
                System.out.println("Error registering user: " + e.getMessage());
            }
        }
    }

} // Class end
