// Packages
package App;

// Imports
import javafx.scene.layout.Pane;

public class StartBorder extends Pane {

    // Attributes

    // DATATYPE //
    private int sceneWidth;

    // ____________________________________________________

    public StartBorder(int sceneWidth){

        this.sceneWidth = sceneWidth;
        this.setPrefWidth(this.sceneWidth);

    } // Constructor End

} //StartInfo Class end
