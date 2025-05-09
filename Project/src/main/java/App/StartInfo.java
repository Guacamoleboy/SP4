// Packages
package App;

// Imports
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class StartInfo extends VBox {

    // Attributes
    private int sceneWidth;
    private int sceneHeight;

    // ____________________________________________________

    public StartInfo(int sceneWidth, int sceneHeight){

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.setPrefSize(sceneWidth, sceneHeight);

        VBox loginBox = new VBox(15); // Padding / Margin

        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPrefWidth(150); // Center of 300 (Scene)
        this.getChildren().add(sidePane());

    } // Constructor End

    public VBox sidePane() {
        Image logo = new Image(getClass().getResource("/assets/logo/Elevtiden-nobg.png").toExternalForm());
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(300);
        logoView.setPreserveRatio(true);

        VBox sidePaneBox = new VBox(15); // Padding / Margin
        sidePaneBox.setAlignment(Pos.CENTER);

        //Placement micro adjustment
        sidePaneBox.setPadding(new Insets(10, 10, 20, 20)); // top, right, bottom, left

        //Adding to VBox
        sidePaneBox.getChildren().addAll(logoView);

        return sidePaneBox;
    } //logo() end

} //StartInfo Class end
