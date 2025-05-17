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
        Label headerLabel = new Label("You are trying to tip for your haircut the\nDate: " + date + "\nTime: " + time);
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

    public static void displayBook(String date,String place, String time, String rating){
        Dialog<ButtonType> tip = new Dialog<>();
        tip.setTitle("Request booking");

        // Header
        Label headerLabel = new Label("You are trying to apply a request for your new fresh fade\n\nDate:" + date + "\nTime: " + time +
                "\nPlace: " + place + "\n" + rating);
        headerLabel.setTextAlignment(TextAlignment.CENTER);
        headerLabel.setAlignment(Pos.CENTER);
        headerLabel.setWrapText(true);
        headerLabel.setPadding(new Insets(15, 20, 0, 20));

        VBox headerBox = new VBox(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        tip.getDialogPane().setHeader(headerBox);


        // Request
        TextArea commentInput = new TextArea();
        commentInput.setPromptText("Please type in your request for your new fresh fade here...");
        commentInput.setWrapText(true);
        commentInput.setMaxWidth(200);
        commentInput.setStyle(
                "-fx-text-fill: #3a3a3a;"+
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );

        VBox contentBox = new VBox(10, commentInput);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(10));
        contentBox.setStyle("-fx-background-color: transparent;-fx-border-width: 0; -fx-background-radius: 25px; -fx-border-radius: 25px; -fx-border-insets: 0; -fx-background-insets: 0");

        tip.getDialogPane().setContent(contentBox);

        // Buttons
        ButtonType saveButtonType = new ButtonType("Send request", ButtonBar.ButtonData.OK_DONE);
        tip.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Show and get result
        Optional<ButtonType> result = tip.showAndWait();

        // Tip result handle
        if (result.isPresent() && result.get() == saveButtonType) {
            String RequestText = commentInput.getText().trim();

            if (!RequestText.isEmpty()) {

                System.out.println("User request for:\n " + RequestText);

            } else {

                System.out.println("No tip entered.");

            }
        }
    }

    // ____________________________________________________

    public static void displayMoreInfo(){

    }

    public static String chooseLanguage(String Language){

        Dialog<ButtonType> tip = new Dialog<>();
        tip.setTitle("Choose language");

        // Header
        Label headerLabel = new Label("choose a different language.. Your current language is\n\n"+Language);
        headerLabel.setTextAlignment(TextAlignment.CENTER);
        headerLabel.setAlignment(Pos.CENTER);
        headerLabel.setWrapText(true);
        headerLabel.setPadding(new Insets(15, 20, 0, 20));

        VBox headerBox = new VBox(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        tip.getDialogPane().setHeader(headerBox);

        ComboBox<String> languages = new ComboBox<>();
        languages.setPromptText("Select you language");
        languages.getItems().addAll("Español", "English", "Dansk");

        VBox contentBox = new VBox(10, languages);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(10));
        contentBox.setStyle("-fx-background-color: transparent;-fx-border-width: 0; -fx-background-radius: 25px; -fx-border-radius: 25px; -fx-border-insets: 0; -fx-background-insets: 0");

        tip.getDialogPane().setContent(contentBox);

        // Buttons
        ButtonType payButtonType = new ButtonType("Change language", ButtonBar.ButtonData.OK_DONE);
        tip.getDialogPane().getButtonTypes().addAll(payButtonType, ButtonType.CANCEL);

        // Show and get result
        Optional<ButtonType> result = tip.showAndWait();
        return languages.getValue().trim();
    }

    // ____________________________________________________

    public static void displayPay(String date, String time, String amount){

        Dialog<ButtonType> tip = new Dialog<>();
        tip.setTitle("Tip your student!");

        // Header
        Label headerLabel = new Label("Pay ahead of time for your booking on the\n\nDate: " + date + "\nTime: " + time);
        headerLabel.setTextAlignment(TextAlignment.CENTER);
        headerLabel.setAlignment(Pos.CENTER);
        headerLabel.setWrapText(true);
        headerLabel.setPadding(new Insets(15, 20, 0, 20));

        VBox headerBox = new VBox(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        tip.getDialogPane().setHeader(headerBox);

        Label paymentAmount = new Label(amount);
        paymentAmount.setTextAlignment(TextAlignment.CENTER);
        paymentAmount.setAlignment(Pos.CENTER);
        paymentAmount.setPadding(new Insets(10, 10, 10, 10));
        paymentAmount.setWrapText(true);
        paymentAmount.setStyle("-fx-background-color: #808080; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-text-fill: #FFF; -fx-font-size: 16px; -fx-font-weight: bold");

        VBox contentBox = new VBox(10, paymentAmount);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(10));
        contentBox.setStyle("-fx-background-color: transparent;-fx-border-width: 0; -fx-background-radius: 25px; -fx-border-radius: 25px; -fx-border-insets: 0; -fx-background-insets: 0");

        tip.getDialogPane().setContent(contentBox);

        // Buttons
        ButtonType payButtonType = new ButtonType("Pay now", ButtonBar.ButtonData.OK_DONE);
        tip.getDialogPane().getButtonTypes().addAll(payButtonType, ButtonType.CANCEL);

        // Show and get result
        Optional<ButtonType> result = tip.showAndWait();

    }

} // Dialog Class end
