package com.example.quizapp.modules;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CustomTab extends Tab {
    private final String question;
    private final String imageURL;
    private final String answersType;
    private final ArrayList<String> answers;
    private final ArrayList<Boolean> correctAnswers;

    private HBox hBox;
    private Button button;

    public CustomTab(String s, String question, String imageURL, String answersType, ArrayList<String> answers, ArrayList<Boolean> correctAnswers) {
        super(s);
        this.question = question;
        this.imageURL = imageURL;
        this.answersType = answersType;
        this.answers = answers;
        this.correctAnswers = correctAnswers;

        initialize();
    }

    private void initialize() {
        Label label = new Label(question);
        label.setWrapText(true);

        ImageView imageView = null;
        if (!imageURL.isEmpty()) {
            imageView = new ImageView(new Image(imageURL));
        }

        hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        switch (answersType) {
            case "CheckBox" -> {
                ArrayList<CheckBox> checkBoxes = new ArrayList<>();
                for (String answer : answers) {
                    checkBoxes.add(new CheckBox(answer));
                }
                hBox.getChildren().addAll(checkBoxes);
            }
            case "RadioButton" -> {
                ArrayList<RadioButton> radioButtons = new ArrayList<>();
                RadioButton radioButton;
                ToggleGroup toggleGroup = new ToggleGroup();
                for (String answer : answers) {
                    radioButton = new RadioButton(answer);
                    radioButton.setToggleGroup(toggleGroup);
                    radioButtons.add(radioButton);
                }
                hBox.getChildren().addAll(radioButtons);
            }
            case "ComboBox" -> {
                ObservableList<String> observableList = FXCollections.observableArrayList(answers);
                ComboBox<String> comboBox = new ComboBox<>(observableList);
                hBox.getChildren().add(comboBox);
            }
        }

        button = new Button("Next");

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        if (!imageURL.isEmpty()) {
            vBox.getChildren().addAll(label, imageView, hBox, button);
        } else {
            vBox.getChildren().addAll(label, hBox, button);
        }

        this.setContent(vBox);
        this.setClosable(false);
        this.setDisable(true);
    }

    public String getAnswersType() {
        return answersType;
    }

    public ArrayList<Boolean> getCorrectAnswers() {
        return correctAnswers;
    }

    public HBox gethBox() {
        return hBox;
    }

    public Button getButton() {
        return button;
    }
}
