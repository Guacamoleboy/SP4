module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.mail;
    requires java.sql;
    requires java.net.http;

    opens App to javafx.fxml;
    exports App;
}