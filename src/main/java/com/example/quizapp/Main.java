package com.example.quizapp;

import com.example.quizapp.modules.CustomTabPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        CustomTabPane customTabPane = new CustomTabPane("src/main/java/com/example/testapp/config.xml");

        stage.setTitle("TestApp");
        stage.setScene(new Scene(customTabPane));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
