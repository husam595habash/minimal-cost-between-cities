module com.example.algo1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.algo1 to javafx.fxml;
    exports com.example.algo1;
}