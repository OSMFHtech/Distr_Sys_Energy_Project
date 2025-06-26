module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}