package App;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu extends Pane {

    // Attributes
    private String username;
    private String password;
    private int sceneWidth;
    private int sceneHeight;

    private FileIO io = new FileIO();

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
        this.setStyle("-fx-background-color: #AAAAAAFF");

        // Create
        this.getChildren().add(display());

    }

    // ____________________________________________________

    public HBox display(){

        ArrayList <String> data = io.readData("src/main/java/data/userData.csv");

        HBox displayBox = new HBox();
        displayBox.setPrefSize(sceneWidth, sceneHeight);

        VBox userList = new VBox();
        userList.setPrefSize(250, sceneHeight);
        userList.setStyle("-fx-background-color: #202123");

        for (String s : data){

            if(!data.isEmpty()){

                String[] values = s.split(", ");
                String accountName = values[0].trim();
                String accountStatus = values[3].trim();

                if(accountStatus.equalsIgnoreCase("Online")){

                    Label onlineLabel = new Label(accountName);
                    onlineLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-padding: 10px;");
                    userList.getChildren().add(onlineLabel);

                }

            }

        }

        BorderPane chatPane = new BorderPane();
        chatPane.setPrefSize(650, sceneHeight);
        chatPane.setStyle("-fx-background-color: #343541;");

        displayBox.getChildren().addAll(userList, chatPane);

        return displayBox;

    }


} // Menu class end
