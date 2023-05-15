module com.example.quizapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens com.example.quizapp to javafx.fxml;
    exports com.example.quizapp;
    exports com.example.quizapp.modules;
    opens com.example.quizapp.modules to javafx.fxml;
}
