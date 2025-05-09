module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens App to javafx.fxml;
    exports App;
}