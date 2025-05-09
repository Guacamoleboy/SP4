// Packages
package App;

// Imports
import javafx.scene.layout.Pane;

public class Menu extends Pane {

    // Attributes
    private int sceneWidth;
    private int sceneHeight;

    // ____________________________________________________

    public Menu(int sceneWidth, int sceneHeight) {

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        // Display setup
        this.setPrefWidth(sceneWidth);
        this.setPrefHeight(sceneHeight);

    } // Constructor end

}