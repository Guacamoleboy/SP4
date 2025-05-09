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

public class Menu extends Pane {

    // Attributes
    private String username;
    private String password;
    private int sceneWidth;
    private int sceneHeight;
    private DBConnector db;
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";

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

        // db connection
        this.db = new DBConnector();
        db.connect(DB_URL);

        // Create
        this.getChildren().add(display());
    }

    // ____________________________________________________

    public HBox display(){
        HBox displayBox = new HBox();
        displayBox.setPrefSize(sceneWidth, sceneHeight);

        VBox userList = new VBox();

        userList.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        userList.setPrefSize(250, sceneHeight);
        userList.getStyleClass().add("body");

        // get online users
        if (db.isConnected()) {
            String query = "SELECT username, status FROM users WHERE status = 'Online'";
            ResultSet rs = db.executeQuery(query);
            
            try {
                while (rs != null && rs.next()) {
                    String accountName = rs.getString("username");
                    Label onlineLabel = new Label(accountName);
                    onlineLabel.getStyleClass().add("label");
                    userList.getChildren().add(onlineLabel);
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving online users: " + e.getMessage());
            }
        }

        BorderPane chatPane = new BorderPane();
        chatPane.setPrefSize(650, sceneHeight);
        chatPane.getStyleClass().add("label");

        displayBox.getChildren().addAll(userList, chatPane);

        return displayBox;
    }

    // ____________________________________________________

    public void cleanup() {
        if (db != null) {
            db.closeConnection();
        }
    }

} // Menu class end
