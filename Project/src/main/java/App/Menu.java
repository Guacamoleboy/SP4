package App;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu extends Pane {

    // Attributes
    private String username;
    private String password;
    private int sceneWidth;
    private int sceneHeight;

    private DBConnector dbConnector = new DBConnector();

    // ____________________________________________________

    public Menu(String username, String password, int sceneWidth, int sceneHeight){

        // User data
        this.username = username;
        this.password = password;

        // Scene Setup
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.setPrefHeight(sceneHeight);
        this.setPrefWidth(sceneWidth);
        this.setStyle("-fx-background-color: "+Main.backgroundColor);

        // forbind til databasen
        connectToDatabase();

        // create
        this.getChildren().add(display());
    }

    // ____________________________________________________

    private void connectToDatabase() {
        // forbinder til database
        boolean connected = dbConnector.connect("jdbc:sqlite:ElevTiden.sqlite");

        if (!connected) {
            System.out.println("Failed to connect to database");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Connection Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to connect to the database. The application may not function correctly.");
            alert.showAndWait();
        }
    }

    // ____________________________________________________

    public HBox display(){
        HBox displayBox = new HBox();
        displayBox.setPrefSize(sceneWidth, sceneHeight);

        VBox userList = new VBox();
        userList.setPrefSize(250, sceneHeight);
        userList.setStyle("-fx-background-color: #202123");

        // henter online brugere
        try {
            ResultSet rs = dbConnector.executeQuery("SELECT username FROM users WHERE status = 'Online'");

            while (rs != null && rs.next()) {
                String accountName = rs.getString("username");

                Label onlineLabel = new Label(accountName);
                onlineLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-padding: 10px;");
                userList.getChildren().add(onlineLabel);
            }

            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving online users: " + e.getMessage());
        }

        BorderPane chatPane = new BorderPane();
        chatPane.setPrefSize(650, sceneHeight);
        chatPane.setStyle("-fx-background-color: #343541;");

        displayBox.getChildren().addAll(userList, chatPane);

        return displayBox;
    }

    public void cleanup() {
        dbConnector.closeConnection();
    }
} // Menu class end
