// Packages
package App;

// Imports
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;

public class SideMenu extends Pane {

    // Attributes
    private int sceneWidth;
    private int sceneHeight;

    // ____________________________________________________

    public SideMenu(int sceneWidth, int sceneHeight) {

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        // Display setup
        this.setPrefWidth(sceneWidth);
        this.setPrefHeight(sceneHeight);

        // Create
        this.getChildren().add(display());
    }

    // ____________________________________________________

    public BorderPane display() {

        BorderPane layout = new BorderPane();
        layout.setPrefWidth(sceneWidth);
        layout.setPrefHeight(sceneHeight);

        VBox topBox = new VBox();
        topBox.setSpacing(20);
        // Change to CSS at some point
        topBox.setStyle("-fx-alignment: top-center;");
        topBox.setPadding(new javafx.geometry.Insets(15, 0, 0, 0)); // top, right, bottom, left

        Button profileBtn = imageButton("/assets/profile/person1.png", 50, 50, 60);
        Button btn1 = imageButton("/assets/icons/icon1.png", 30, 30, 50);
        Button btn2 = imageButton("/assets/icons/icon2.png", 30, 30, 50);
        Button btn3 = imageButton("/assets/icons/icon3.png", 30, 30, 50);
        Button btn4 = imageButton("/assets/icons/icon5.png", 30, 30, 50);

        topBox.getChildren().addAll(profileBtn, btn1, btn2, btn3, btn4);

        VBox bottomBox = new VBox();
        bottomBox.setStyle("-fx-alignment: bottom-center;");
        bottomBox.setPadding(new javafx.geometry.Insets(0, 0, 20, 0)); // top, right, bottom, left

        Button settingsBtn = imageButton("/assets/icons/icon4.png", 30, 30, 50);
        bottomBox.getChildren().add(settingsBtn);

        layout.setTop(topBox);
        layout.setBottom(bottomBox);

        return layout;

    }

    // ____________________________________________________

    private Button imageButton(String imagePath, int iconWidth, int iconHeight, int buttonSize) {

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        icon.setFitWidth(iconWidth);
        icon.setFitHeight(iconHeight);

        Button button = new Button();
        button.setGraphic(icon);
        // Change to CSS at some point
        button.setStyle("-fx-background-color: #707070; -fx-cursor: hand; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        button.setPrefSize(buttonSize, buttonSize);
        return button;

    }

} // Class end
