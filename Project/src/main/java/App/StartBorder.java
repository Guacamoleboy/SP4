// Packages
package App;

// Imports
import javafx.scene.layout.Pane;

public class StartBorder extends Pane {

    // Attributes
    private int sceneWidth;

    // ____________________________________________________

    public StartBorder(int sceneWidth){

        this.sceneWidth = sceneWidth;
        this.setPrefWidth(this.sceneWidth);
        this.setStyle("-fx-background-color: rgba(0,0,0,0.50);");


    } // Constructor End

} //StartInfo Class end
