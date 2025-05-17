// Packages
package App;

// Imports
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.Optional;

public class DialogBox extends Pane {

    // Attributes

    // ____________________________________________________

    public DialogBox(){ // Needed (?)

    }

    // ____________________________________________________

    public static void displayTip(String date, String time){

        Dialog<ButtonType> tip = new Dialog<>();
        tip.setTitle("Tip your student!");

        // Header
        Label headerLabel = new Label("You are trying to tip for your haircut the\n\nDate: " + date + "\nTime: " + time);
        headerLabel.setTextAlignment(TextAlignment.CENTER);
        headerLabel.setAlignment(Pos.CENTER);
        headerLabel.setWrapText(true);
        headerLabel.setPadding(new Insets(15, 20, 0, 20));

        VBox headerBox = new VBox(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        tip.getDialogPane().setHeader(headerBox);

        // Tip
        TextArea tipInput = new TextArea();
        tipInput.setPromptText("Type here...");
        tipInput.setWrapText(false);
        tipInput.setPrefRowCount(1);
        tipInput.setMaxWidth(200);
        tipInput.setStyle(
        "-fx-text-fill: black;"+
        "-fx-font-size: 18px;" +
        "-fx-font-weight: bold;"
        );

        // Tip
        TextArea commentInput = new TextArea();
        commentInput.setPromptText("Comment");
        commentInput.setWrapText(true);
        commentInput.setMaxWidth(200);
        commentInput.setStyle(
        "-fx-text-fill: #3a3a3a;"+
        "-fx-font-size: 14px;" +
        "-fx-font-weight: bold;"
        );

        VBox contentBox = new VBox(10, tipInput, commentInput);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(10));
        contentBox.setStyle("-fx-background-color: transparent;-fx-border-width: 0; -fx-background-radius: 25px; -fx-border-radius: 25px; -fx-border-insets: 0; -fx-background-insets: 0");

        tip.getDialogPane().setContent(contentBox);

        // Buttons
        ButtonType saveButtonType = new ButtonType("Tip", ButtonBar.ButtonData.OK_DONE);
        tip.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Show and get result
        Optional<ButtonType> result = tip.showAndWait();

        // Tip result handle
        if (result.isPresent() && result.get() == saveButtonType) {
            String tipAmount = tipInput.getText().trim();

            if (!tipAmount.isEmpty()) {

                System.out.println("User tipped: " + tipAmount);

            } else {

                System.out.println("No tip entered.");

            }
        }

    }

    // ____________________________________________________

    public static void displayAlert(String date, String time) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancelation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to cancel your booking?\n\n" + "Booking date: " + date + "\nTime: " + time);

        alert.getButtonTypes().clear();
        ButtonType yesButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Go Back", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().addAll(yesButton, noButton);

        Optional<ButtonType> response = alert.showAndWait();

        if(response.isPresent() && response.get() == yesButton){

        }

    }

    // ____________________________________________________

    public static void displayBook(){

    }

    // ____________________________________________________

    public static void displayMoreInfo(){

    }

    // ____________________________________________________

    public static void displayPay(){

    }

} // Dialog Class end
