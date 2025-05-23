// Package
package App;

// Import
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Animation {

    // Attributes

    // ____________________________________________________

    public static void addHoverScaleEffect(Button button) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), button);
        scaleUp.setToX(1.02);
        scaleUp.setToY(1.02);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        button.setOnMouseEntered(e -> scaleUp.playFromStart());
        button.setOnMouseExited(e -> scaleDown.playFromStart());
    }

    // ____________________________________________________

    public static void addHoverScaleEffectMore(Button button) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), button);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        button.setOnMouseEntered(e -> scaleUp.playFromStart());
        button.setOnMouseExited(e -> scaleDown.playFromStart());
    }

    // ____________________________________________________

    public static void addHoverScaleEffectVBox(VBox vbox) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), vbox);
        scaleUp.setToX(1.02);
        scaleUp.setToY(1.02);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), vbox);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        vbox.setOnMouseEntered(e -> scaleUp.playFromStart());
        vbox.setOnMouseExited(e -> scaleDown.playFromStart());
    }

    // ____________________________________________________

    public static void addHoverScaleEffectVBoxExtra(VBox vbox) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), vbox);
        scaleUp.setToX(1.04);
        scaleUp.setToY(1.04);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), vbox);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        vbox.setOnMouseEntered(e -> scaleUp.playFromStart());
        vbox.setOnMouseExited(e -> scaleDown.playFromStart());
    }

    // ____________________________________________________

    public static void addHoverScaleEffectImage(ImageView imageView) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), imageView);
        scaleUp.setToX(1.04);
        scaleUp.setToY(1.04);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), imageView);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        imageView.setOnMouseEntered(e -> scaleUp.playFromStart());
        imageView.setOnMouseExited(e -> scaleDown.playFromStart());
    }

} // Animation end