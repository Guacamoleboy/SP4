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

import java.util.ArrayList;
import java.util.Map;

public class NextOption extends Pane {

    // Attributes

    // DATATYPE //
    private int sceneWidth;
    private int sceneHeight;

    // OBJECT //
    private Button nextButton;
    private Button goBackButton;
    private Button tryAgainButton;
    private Button registerButton;
    private ComboBox<String> hairTypeDropdown;
    private ComboBox<String> genderDropdown;
    private ComboBox<String> lengthDropdown;
    private ComboBox<String> hairColorDropdown;
    private ComboBox<String> schoolDropdown;
    private ComboBox<String> yearDropdown;
    private Register register;
    private Map<String, Integer> schoolMap;

    // ____________________________________________________

    public NextOption(int sceneWidth, int sceneHeight, Register register){

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.register = register;
        this.setPrefSize(sceneWidth, sceneHeight);

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
        registerButton.setOnAction(e -> registerButtonActionCostumer());

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
        this.schoolMap = Main.db.getSchools();
        schoolDropdown.getItems().addAll(schoolMap.keySet());
        schoolDropdown.setPromptText("School");
        schoolDropdown.getStyleClass().add("combo-box");

        yearDropdown = new ComboBox<>();
        yearDropdown.getItems().addAll( "1", "2", "3");
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
        registerButton.setOnAction(e -> registerButtonActionStudent());

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
        Register register = new Register(600, 600, this.register.getUsername(), this.register.getPassword(), this.register.getEmail());
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

    public void registerButtonActionCostumer() {

        String username = register.getUsername();
        String password = register.getPassword();
        String role = register.getSelectedUserType();
        String email = register.getEmail();

        String hairType = hairTypeDropdown.getValue();
        String hairColor = hairColorDropdown.getValue();
        String hairLength = lengthDropdown.getValue();
        String gender = genderDropdown.getValue();

        int hairTypeId = Main.db.getOrCreateHairType(hairType, hairColor, hairLength, gender);

        // Default
        String status = "Offline";
        String banned = "No";
        String profileHex = "#ADD8E6FF";
        String bannerHex = "#D3D3D3FF";
        String roleHex = "#d0e6f7";
        String accepted = "null";
        int schoolId = -1;
        int studentYear = -1;
        String profilePicture = "person1.png";
        String language = "English";
        String darkmode = "Yes";
        String lastonline = "Not seen";
        String city = null;
        String abtmeheader = "Hello...";
        String abtmedesc = "Welcome to my profile";
        String abtmefunfacts = "No numbers before 1000 contains the letter A";
        int phone = 00000000;
        double rating = 0.0;
        String social = "";
        String contactheader = "I'm glad you're here!";
        String contactdesc = "Contact me here!";



        boolean connected = Main.db.isConnected();
        System.out.println("Database connected: " + connected);

        // Create user with extended method
        boolean userCreated = Main.db.createUser(username, password, email, status, banned, role,
                profileHex, bannerHex, roleHex,
                hairTypeId, schoolId, studentYear,
                profilePicture, language, accepted, darkmode,
                lastonline, city, abtmeheader, abtmedesc, abtmefunfacts,
                phone, rating, social, contactheader, contactdesc);

        if (connected && userCreated) {
            register.alert("Registration successful! You can now log in.");
            Stage stage = (Stage) getScene().getWindow();
            Main.loginPage(stage);
        } else {
            register.alert("Registration failed. Please try again.");
        }
    }

    // ____________________________________________________

    public void registerButtonActionStudent() {

        String username = register.getUsername();
        String password = register.getPassword();
        String role = register.getSelectedUserType();
        String email = register.getEmail();
        String schoolName = schoolDropdown.getValue();
        int schoolId = schoolMap.getOrDefault(schoolName, 0);
        String studentYearStr = yearDropdown.getValue();

        int studentYear = 0; // default before being set

        // Try-catch for int input
        try {
            studentYear = Integer.parseInt(studentYearStr);
        } catch (NumberFormatException e) {
            register.alert("Invalid student year!");
        }

        // Default (STUDENT)
        String status = "Offline";
        String banned = "No";
        String profileHex = "#ADD8E6FF";
        String bannerHex = "#D3D3D3FF";
        String roleHex = "#d0e6f7";
        String profilePicture = "person1.png";
        String language = "English";
        String accepted = "No";
        String darkmode = "Yes";
        String lastonline = "Not seen";
        String city = null;
        String abtmeheader = "Hello...";
        String abtmedesc = "Welcome to my profile";
        String abtmefunfacts = "No numbers before 1000 contains the letter A";
        int phone = 00000000;
        double rating = 0.0;
        String social = "";
        String contactheader = "I'm glad you're here!";
        String contactdesc = "Contact me here!";

        boolean connected = Main.db.isConnected();
        System.out.println("Database connected: " + connected);

        boolean userCreated = Main.db.createUser(
                username,
                password,
                email,
                status,
                banned,
                role,
                profileHex,
                bannerHex,
                roleHex,
                0,
                schoolId,
                studentYear,
                profilePicture,
                language,
                accepted,
                darkmode,
                lastonline,
                city,
                abtmeheader,
                abtmedesc,
                abtmefunfacts,
                phone,
                rating,
                social,
                contactheader,
                contactdesc
        );

        if (connected && userCreated) {
            register.alert("Registration successful! You can now log in.");
            Stage stage = (Stage) getScene().getWindow();
            Main.loginPage(stage);
        } else {
            register.alert("Registration failed. Please try again.");
        }

    }

} // Class end