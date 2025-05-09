module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens App to javafx.fxml;
    exports App;
}