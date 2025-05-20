package App;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.*;
import java.util.Optional;
import javafx.util.Duration;

import static App.DialogBox.profileSettings;
import static java.lang.Integer.parseInt;

public class Setting extends Pane {

    // Attributes
    private Alert alert;
    private String username;

    // OBJECT //
    Connection con;

    // save for later used methods
    private ColorPicker profileColorPicker;
    private ColorPicker bannerColorPicker;
    private ColorPicker roleColorPicker;
    private Profile profile;

    public Setting(String username){
        this.username = username;
        profile = new Profile(username);
        // instantiate color pickers with error handling
        try {
            profileColorPicker = new ColorPicker(Color.web(getProfileColor()));
        } catch (Exception e) {
            profileColorPicker = new ColorPicker(Color.web("#ADD8E6FF")); // Default light blue
        }
        profileColorPicker.setVisible(false);

        try {
            bannerColorPicker = new ColorPicker(Color.web(getBannerColor()));
        } catch (Exception e) {
            bannerColorPicker = new ColorPicker(Color.web("#D3D3D3FF")); // Default light gray
        }
        bannerColorPicker.setVisible(false);

        try {
            roleColorPicker = new ColorPicker(Color.web(getRoleColor()));
        } catch (Exception e) {
            roleColorPicker = new ColorPicker(Color.web("#d0e6f7")); // Default light blue
        }
        roleColorPicker.setVisible(false);
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
        applyDropShadowEffect(profileImageView, 50, 50, 0.7);
        redRegion.getChildren().add(profileImageView);

        // :hover on imageView
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), profileImageView);
        scaleUp.setToX(1.08);
        scaleUp.setToY(1.08);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), profileImageView);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Action listener on imageView only *dab*
        redRegion.setOnMouseEntered(e -> scaleUp.playFromStart());
        redRegion.setOnMouseExited(e -> scaleDown.playFromStart());
        redRegion.setOnMouseClicked(e -> {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Main.loginPage(stage);
        });

        // final HBox add
        fullWidthHBox.getChildren().addAll(greyRegion, redRegion);

        return fullWidthHBox;
    }

    // _____________________________________________

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
        String normalBackground = "rgba(176,72,72,0.73)"; // Light red (Default lol)
        String hoverBackground = "rgba(120,30,30,0.9)"; // Dark red
        redRegion.setStyle("-fx-background-color: " + normalBackground + "; -fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
        redRegion.setAlignment(Pos.CENTER);

        // Image swap
        Image iconNormal = new Image(getClass().getResource("/assets/icons/icon13.png").toExternalForm());
        Image iconHover = new Image(getClass().getResource("/assets/icons/icon12.png").toExternalForm());

        ImageView profileImageView = new ImageView(iconNormal);
        applyDropShadowEffect(profileImageView, 75, 75, 0.7);
        redRegion.getChildren().add(profileImageView);

        // :hover on imageView
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), profileImageView);
        scaleUp.setToX(1.10);
        scaleUp.setToY(1.10);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), profileImageView);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Action listener on enter / exit
        redRegion.setOnMouseEntered(e -> {
            profileImageView.setImage(iconHover);
            redRegion.setStyle("-fx-background-color: " + hoverBackground + "; -fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
            scaleUp.playFromStart();
        });
        redRegion.setOnMouseExited(e -> {
            profileImageView.setImage(iconNormal);
            redRegion.setStyle("-fx-background-color: " + normalBackground + "; -fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
            scaleDown.playFromStart();
        });

        redRegion.setOnMouseClicked(e -> {
            if (Main.db.deleteAccount(this.username)) {
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                Main.loginPage(stage);
            }
        });

        // final HBox add
        fullWidthHBox.getChildren().addAll(greyRegion, redRegion);

        return fullWidthHBox;
    }

    // _____________________________________________

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
        redRegion.getChildren().add(profileImageView);
        applyDropShadowEffect(profileImageView, 75, 75, 0.7);

        // :hover on imageView
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), profileImageView);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), profileImageView);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Action listener on imageView only *dab*
        redRegion.setOnMouseEntered(e -> scaleUp.playFromStart());
        redRegion.setOnMouseExited(e -> scaleDown.playFromStart());

        // final HBox add
        fullWidthHBox.getChildren().addAll(greyRegion, redRegion);

        return fullWidthHBox;
    }

    // _____________________________________________

    private void applyDropShadowEffect(ImageView imageView, double width, double height, double opacity) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        dropShadow.setColor(Color.color(0, 0, 0, opacity));

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        imageView.setEffect(dropShadow);
    }

    // _____________________________________________

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
        applyDropShadowEffect(profileImageView, 75, 75, 0.7);
        redRegion.getChildren().add(profileImageView);

        // :hover on imageView
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), profileImageView);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), profileImageView);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Action listener on imageView only *dab*
        redRegion.setOnMouseEntered(e -> scaleUp.playFromStart());
        redRegion.setOnMouseExited(e -> scaleDown.playFromStart());

        // final HBox add
        fullWidthHBox.getChildren().addAll(greyRegion, redRegion);

        return fullWidthHBox;
    }

    // _____________________________________________

    protected HBox displayLanguage(String language) {

        // HBox
        HBox fullWidthHBox = new HBox();
        fullWidthHBox.setAlignment(Pos.CENTER_LEFT);
        fullWidthHBox.setPrefHeight(134);
        fullWidthHBox.setPrefWidth(760);
        fullWidthHBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: #464646;");

        Image profileImage = new Image(getClass().getResource("/assets/flag/" + language + ".png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        applyDropShadowEffect(profileImageView, 75, 75, 0.7);

        // GREY region
        VBox greyRegion = new VBox();
        greyRegion.setPrefWidth(760 * 0.70);
        greyRegion.setStyle("-fx-background-color: #7c7c7c; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646;");
        greyRegion.setAlignment(Pos.CENTER);

        Label logoutLabel = new Label("Change language");
        logoutLabel.setStyle("-fx-font-size: 25px; -fx-text-fill: white; -fx-padding: 20px;");
        greyRegion.getChildren().add(logoutLabel);

        // RED region
        VBox redRegion = new VBox();
        redRegion.setPrefWidth(760 * 0.30);
        redRegion.setStyle("-fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
        redRegion.setAlignment(Pos.CENTER);
        redRegion.getChildren().add(profileImageView);

        switch(language.toLowerCase()) {
            case "english":
                redRegion.setStyle(redRegion.getStyle() + "-fx-background-color: rgb(52,71,117);");
                break;
            case "español":
                redRegion.setStyle(redRegion.getStyle() + "-fx-background-color: rgb(229, 69, 69);");
                break;
            case "dansk":
                redRegion.setStyle(redRegion.getStyle() + "-fx-background-color: rgb(198, 12, 48);");
                break;
            default:
                redRegion.setStyle(redRegion.getStyle() + "-fx-background-color: #e54545;");
        }

        // :hover on imageView
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), profileImageView);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), profileImageView);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Action listener on imageView only *dab*
        redRegion.setOnMouseEntered(e -> scaleUp.playFromStart());
        redRegion.setOnMouseExited(e -> scaleDown.playFromStart());

        redRegion.setOnMouseClicked(e -> {
            String newLang = DialogBox.chooseLanguage(Main.lang);

            if (newLang != null && !newLang.equalsIgnoreCase(Main.lang)) {
                Main.setLang(newLang);
            }
        });

        // final HBox add
        fullWidthHBox.getChildren().addAll(greyRegion, redRegion);

        return fullWidthHBox;
    }

    // _____________________________________________

    /*protected HBox displayProfileColors() {
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

        Label colorLabel = new Label("Profile Colors");
        colorLabel.setStyle("-fx-font-size: 25px; -fx-text-fill: white; -fx-padding: 20px;");
        greyRegion.getChildren().add(colorLabel);

        // Color region
        VBox colorRegion = new VBox();
        colorRegion.setPrefWidth(760 * 0.30);
        colorRegion.setStyle("-fx-background-color: rgb(94,159,196); -fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0;");
        colorRegion.setAlignment(Pos.CENTER);

        // gem icon
        Image saveIcon = new Image(getClass().getResource("/assets/icons/icon22.png").toExternalForm());
        ImageView saveIconView = new ImageView(saveIcon);
        applyDropShadowEffect(saveIconView, 100, 100, 0.7);
        colorRegion.getChildren().add(saveIconView);

        // hover
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), saveIconView);
        scaleUp.setToX(1.08);
        scaleUp.setToY(1.08);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), saveIconView);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        colorRegion.setOnMouseEntered(e -> scaleUp.playFromStart());
        colorRegion.setOnMouseExited(e -> scaleDown.playFromStart());

        // Actions
        colorRegion.setOnMouseClicked(e -> {
            showColorPickerDialog();
        });

        Tooltip greyTooltip = new Tooltip("Click to change profile colors");
        Tooltip.install(greyRegion, greyTooltip);

        Tooltip colorPickerTooltip = new Tooltip("Click to open color picker");
        Tooltip.install(colorRegion, colorPickerTooltip);

        // final HBox add
        fullWidthHBox.getChildren().addAll(greyRegion, colorRegion);

        return fullWidthHBox;
    }*/

    // _____________________________________________

    protected ScrollPane displayProfileColors() {
        VBox contentBox = new VBox();

        contentBox.getChildren().addAll(
                createProfileSettingBox("Profile Colors", "/assets/icons/icon22.png", this::showColorPickerDialog, true, false),
                createProfileSettingBox("Change About Me Header", "/assets/icons/icon22.png", this::editAboutMeHeader, false, false),
                createProfileSettingBox("Change About Me Description", "/assets/icons/icon22.png", this::editAboutMeDescription, false, false),
                createProfileSettingBox("Change About fun fact", "/assets/icons/icon22.png", this::editFunFact, false, false),
                createProfileSettingBox("Change City", "/assets/icons/icon22.png", this::editCity, false, false),
                createProfileSettingBox("Change Number", "/assets/icons/icon22.png", this::editNumber, false, false),
                createProfileSettingBox("Change Email", "/assets/icons/icon22.png", this::editEmail, false, false),
                createProfileSettingBox("Change Contact Me Header", "/assets/icons/icon22.png", this::editContactHeader, false, false),
                createProfileSettingBox("Change Contact Me Description", "/assets/icons/icon22.png", this::editContactDescription, false, true)
        );

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setStyle("-fx-background-color: transparent;");


        return scrollPane;
    }

    // _____________________________________________

    private HBox createProfileSettingBox(String labelText, String iconPath, Runnable action, boolean isFirst, boolean isLast) {
        HBox fullWidthHBox = new HBox();
        fullWidthHBox.setAlignment(Pos.CENTER_LEFT);

        if (!isLast) {
            fullWidthHBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: #464646;");
        }

        VBox greyRegion = new VBox();
        greyRegion.setAlignment(Pos.CENTER_LEFT);
        greyRegion.setStyle("-fx-background-color: #7c7c7c ; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646;");
        greyRegion.setPrefWidth(760 * 0.70);

        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 25px; -fx-text-fill: white; -fx-padding: 20px;");
        greyRegion.getChildren().add(label);

        VBox colorRegion = new VBox();
        colorRegion.setAlignment(Pos.CENTER);


        String borderRadius = "";
        if (isFirst) borderRadius = "-fx-background-radius: 0 20 0 0; -fx-border-radius: 0 20 0 0;";
        else if (isLast) borderRadius = "-fx-background-radius: 0 0 20 0; -fx-border-radius: 0 0 20 0;";

        colorRegion.setStyle("-fx-background-color: rgb(94,159,196); -fx-cursor: hand;" + borderRadius);

        ImageView IconView = new ImageView(new Image(getClass().getResource(iconPath).toExternalForm()));
        applyDropShadowEffect(IconView, 100, 100, 0.7);
        colorRegion.getChildren().add(IconView);


        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), IconView);
        scaleUp.setToX(1.08);
        scaleUp.setToY(1.08);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), IconView);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        colorRegion.setOnMouseEntered(e -> scaleUp.playFromStart());
        colorRegion.setOnMouseExited(e -> scaleDown.playFromStart());
        colorRegion.setOnMouseClicked(e -> action.run());


        fullWidthHBox.getChildren().addAll(greyRegion, colorRegion);
        return fullWidthHBox;
    }

    // _____________________________________________

    private void editAboutMeHeader() {
        String content = profileSettings("About me", "Change header", profile.getProfileAboutmeHeader());

        Main.db.updateABTMeHeader(username, content);
        profile.updateData(username);
    }

    // _____________________________________________

    private void editAboutMeDescription() {
        String content = profileSettings("About me", "Change description", profile.getProfileAboutDescription());

        Main.db.updateABTMeDesc(username, content);
        profile.updateData(username);
    }

    // _____________________________________________

    private void editContactHeader() {
        String content = profileSettings("Contact me", "Change header", profile.getContactHeader());

        Main.db.updateContactHeader(username, content);
        profile.updateData(username);
    }

    // _____________________________________________

    private void editContactDescription() {
        String content = profileSettings("Contact me", "Change description", profile.getContactDescription());

        Main.db.updateContactDesc(username, content);
        profile.updateData(username);
    }

    // _____________________________________________

    private void editFunFact() {
        String content = profileSettings("About me", "Change fun fact", profile.getFunFacts());

        Main.db.updateABTMeFunFact(username, content);
        profile.updateData(username);
    }

    // _____________________________________________

    private void editCity() {
        String content = profileSettings("Change city", "Enter city name", profile.getCity());

        Main.db.setCity(username, content);
        profile.updateData(username);
    }

    // _____________________________________________

    private void editNumber() {
        String content = profileSettings("Change number", "Enter your number", profile.getPhoneNumber());

        try {
            int number = Integer.parseInt(content);
            Main.db.updatePhoneNumber(username, number);
            profile.updateData(username);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        }
    }

    // _____________________________________________

    private void editEmail() {
        String content = profileSettings("Change email", "Enter your email", profile.getEmail());

        Main.db.setEmail(username, content);
        profile.updateData(username);
    }

    // _____________________________________________

    private void editSocial() {
        String content = profileSettings("Social media", "Change your socials", profile.getSocial("instagram"));

        Main.db.updateABTMeFunFact(username, content);
        profile.updateData(username);
    }

    // _____________________________________________

    private void showColorPickerDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Profile Colors");
        dialog.setHeaderText("Select colors for your profile");

        ButtonType saveButtonType = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20, 20, 10, 20));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 0, 20, 0));

        Label profileColorLabel = new Label("Profile Background:");
        ColorPicker profileColorPickerDialog = new ColorPicker(profileColorPicker.getValue());
        grid.add(profileColorLabel, 0, 0);
        grid.add(profileColorPickerDialog, 1, 0);

        Label bannerColorLabel = new Label("Banner Background:");
        ColorPicker bannerColorPickerDialog = new ColorPicker(bannerColorPicker.getValue());
        grid.add(bannerColorLabel, 2, 0);
        grid.add(bannerColorPickerDialog, 3, 0);

        Label roleColorLabel = new Label("Role Label:");
        ColorPicker roleColorPickerDialog = new ColorPicker(roleColorPicker.getValue());
        grid.add(roleColorLabel, 0, 1);
        grid.add(roleColorPickerDialog, 1, 1);

        // add the grid to the content
        content.getChildren().add(grid);

        // create preview
        Label previewLabel = new Label("Preview:");
        previewLabel.setStyle("-fx-font-weight: bold;");
        content.getChildren().add(previewLabel);

        // create the profile preview card
        HBox previewCard = new HBox();
        previewCard.setPrefHeight(225);
        previewCard.setMaxWidth(500);
        previewCard.setStyle("-fx-border-radius: 8px; -fx-background-radius: 8px; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");

        // profile picture zone
        VBox profileArea = new VBox();
        profileArea.setPrefWidth(100);
        profileArea.setAlignment(Pos.TOP_CENTER);
        profileArea.setStyle("-fx-background-color: " + toHexString(profileColorPickerDialog.getValue()) + "; -fx-background-radius: 8px 0 0 0;");

        // add profile picture to preview
        Image profileImage = new Image(getClass().getResource("/assets/profile/person1.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(80);
        profileImageView.setFitHeight(80);
        profileImageView.setPreserveRatio(true);
        profileImageView.setStyle("-fx-padding: 10px;");
        profileArea.getChildren().add(profileImageView);

        // banner color-area
        VBox bannerArea = new VBox();
        bannerArea.setAlignment(Pos.CENTER_LEFT);
        bannerArea.setPrefWidth(400);
        bannerArea.setStyle("-fx-background-color: " + toHexString(bannerColorPickerDialog.getValue()) + "; -fx-background-radius: 0 8px 0 0; -fx-padding: 10px;");

        // bottom navigation bar
        HBox navBar = new HBox();
        navBar.setPrefHeight(40);
        navBar.setPrefWidth(500);

        // role prev
        Label roleNavLabel = new Label("Role");
        roleNavLabel.setPrefWidth(100);
        roleNavLabel.setPrefHeight(40);
        roleNavLabel.setAlignment(Pos.CENTER);
        roleNavLabel.setStyle("-fx-background-color: " + toHexString(roleColorPickerDialog.getValue()) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 0 0 0 8px;");

        // Padding fixed
        String navItemStyle = "-fx-background-color: #c2c2c2; -fx-padding: 12px 0 11px 0; -fx-alignment: center;";
        Label aboutLabel = new Label("Text");
        aboutLabel.setPrefWidth(100);
        aboutLabel.setStyle(navItemStyle);

        Label bookingsLabel = new Label("Text");
        bookingsLabel.setPrefWidth(100);
        bookingsLabel.setStyle(navItemStyle);

        Label reviewsLabel = new Label("Text");
        reviewsLabel.setPrefWidth(100);
        reviewsLabel.setStyle(navItemStyle);

        Label galleryLabel = new Label("Text");
        galleryLabel.setPrefWidth(100);
        galleryLabel.setStyle(navItemStyle + "-fx-background-radius: 0 0 8px 0;");

        navBar.getChildren().addAll(roleNavLabel, aboutLabel, bookingsLabel, reviewsLabel, galleryLabel);

        // preview
        VBox fullPreview = new VBox();
        fullPreview.getChildren().addAll(
                new HBox(profileArea, bannerArea),
                navBar
        );

        content.getChildren().add(fullPreview);

        // update preview colors change
        profileColorPickerDialog.setOnAction(e -> {
            profileArea.setStyle("-fx-background-color: " + toHexString(profileColorPickerDialog.getValue()) + "; -fx-background-radius: 8px 0 0 0;");
        });

        bannerColorPickerDialog.setOnAction(e -> {
            bannerArea.setStyle("-fx-background-color: " + toHexString(bannerColorPickerDialog.getValue()) + "; -fx-background-radius: 0 8px 0 0; -fx-padding: 10px;");
        });

        roleColorPickerDialog.setOnAction(e -> {
            roleNavLabel.setStyle("-fx-background-color: " + toHexString(roleColorPickerDialog.getValue()) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 0 0 0 8px;");
        });

        dialog.getDialogPane().setContent(content);

        // show focus automatically
        dialog.setOnShown(e -> profileColorPickerDialog.requestFocus());

        // result to colors
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == saveButtonType) {
            profileColorPicker.setValue(profileColorPickerDialog.getValue());
            bannerColorPicker.setValue(bannerColorPickerDialog.getValue());
            roleColorPicker.setValue(roleColorPickerDialog.getValue());

            //  change immediately
            String profileHex = toHexString(profileColorPicker.getValue());
            String bannerHex = toHexString(bannerColorPicker.getValue());
            String roleHex = toHexString(roleColorPicker.getValue());

            boolean success = saveProfileColors(profileHex, bannerHex, roleHex, null);

            if (success) {
                showSuccessAlert("Profile colors applied and saved successfully!");
            } else {
                showErrorAlert("Failed to apply profile colors. Please try again.");
            }
        }
    }

    // _____________________________________________

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                (int) (color.getOpacity() * 255));
    }

    // _____________________________________________

    private String getProfileColor() {
        try {
            Profile profile = new Profile(username);
            String color = profile.getProfilePictureHex();
            if (color == null || color.isEmpty() || !isValidColorFormat(color)) {
                return "#ADD8E6FF"; //  light blue
            }
            return color;
        } catch (Exception e) {
            return "#ADD8E6FF"; // light blue
        }
    }

    // _____________________________________________

    private String getBannerColor() {
        try {
            Profile profile = new Profile(username);
            String color = profile.getProfileBannerHex();
            if (color == null || color.isEmpty() || !isValidColorFormat(color)) {
                return "#D3D3D3FF"; //light gray
            }
            return color;
        } catch (Exception e) {
            return "#D3D3D3FF"; // light gray
        }
    }

    // _____________________________________________

    private String getRoleColor() {
        try {
            Profile profile = new Profile(username);
            String color = profile.getProfileRoleHex();
            if (color == null || color.isEmpty() || !isValidColorFormat(color)) {
                return "#d0e6f7"; // light blue
            }
            return color;
        } catch (Exception e) {
            return "#d0e6f7"; // light blue
        }
    }

    // _____________________________________________

    private boolean isValidColorFormat(String color) {
        // is valid hex format (#RRGGBB or #RRGGBBAA)
        return color.matches("^#([0-9A-Fa-f]{6}|[0-9A-Fa-f]{8})$");
    }

    // _____________________________________________

    private boolean saveProfileColors(String profileHex, String bannerHex, String roleHex, String bannerUrl) {
        return Main.db.updateProfileColors(username, profileHex, bannerHex, roleHex, bannerUrl);
    }

    // _____________________________________________

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // _____________________________________________

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    // _____________________________________________

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

    // _____________________________________________

    protected static HBox displayBanUser() {

        // HBox
        HBox fullWidthHBox = new HBox();
        fullWidthHBox.setAlignment(Pos.CENTER_LEFT);
        fullWidthHBox.setPrefHeight(90);
        fullWidthHBox.setPrefWidth(760);
        fullWidthHBox.setStyle("-fx-border-width: 0 0 2px 0; -fx-border-color: #464646;");

        // GREY region
        VBox searchRegion = new VBox();
        searchRegion.setPrefWidth(760 * 0.40);
        searchRegion.setStyle("-fx-background-color: #7c7c7c; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646;");
        searchRegion.setAlignment(Pos.CENTER);

        VBox greyRegion = new VBox();
        greyRegion.setPrefWidth(760 * 0.35);
        greyRegion.setStyle("-fx-background-color: #989898; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646;");
        greyRegion.setAlignment(Pos.CENTER);

        Label banStatusLabel = new Label("Not Banned");
        banStatusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-padding: 20px;");
        greyRegion.getChildren().add(banStatusLabel);

        TextArea usernameUnbanLabel = new TextArea("Enter Username");
        usernameUnbanLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        usernameUnbanLabel.getStyleClass().add("displayBanUser-label");
        searchRegion.getChildren().add(usernameUnbanLabel);

        // RED region
        VBox redRegion = new VBox();
        redRegion.setPrefWidth(760 * 0.25);
        redRegion.setStyle("-fx-background-color: orange; -fx-cursor: hand; -fx-background-radius: 0 20px 0 0; -fx-border-radius: 0 20px 0 0; -fx-border-width: 0 2px 0 0; -fx-border-color: #464646;");
        redRegion.setAlignment(Pos.CENTER);

        Image profileImage = new Image(Setting.class.getResource("/assets/icons/icon18.png").toExternalForm());
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(50);
        profileImageView.setFitHeight(50);
        profileImageView.setPreserveRatio(true);
        redRegion.getChildren().add(profileImageView);

        // final HBox add
        fullWidthHBox.getChildren().addAll(searchRegion, greyRegion, redRegion);

        return fullWidthHBox;
    }

} // Setting end
