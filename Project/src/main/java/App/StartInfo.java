// Packages
package App;

// Imports
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
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
        this.setStyle("-fx-background-color: orange;");

        VBox loginBox = new VBox(15); // Padding / Margin
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPrefWidth(150); // Center of 300 (Scene)


    } // Constructor End

} //StartInfo Class end
