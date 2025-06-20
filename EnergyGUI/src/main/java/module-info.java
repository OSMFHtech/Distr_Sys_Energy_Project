module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http; // Ajout du module pour les appels HTTP
    requires com.fasterxml.jackson.databind;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}