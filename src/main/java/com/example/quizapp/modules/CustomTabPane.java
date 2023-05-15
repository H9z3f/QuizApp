package com.example.quizapp.modules;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CustomTabPane extends TabPane {
    private String filePath;

    private ArrayList<CustomTab> tabs = new ArrayList<>();
    private Stage stage;

    public CustomTabPane(String filePath) {
        this.filePath = filePath;

        initialize();
    }

    private void initialize() {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filePath));

            NodeList nodeList = document.getElementsByTagName("task");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                String question = element.getElementsByTagName("question").item(0).getTextContent();
                String imageURL = element.getElementsByTagName("imageURL").item(0).getTextContent();
                String answersType = element.getElementsByTagName("answersType").item(0).getTextContent();
                ArrayList<String> answers = new ArrayList<>();
                ArrayList<Boolean> correctAnswers = new ArrayList<>();

                NodeList answersList = element.getElementsByTagName("answers").item(0).getChildNodes();
                for (int j = 0; j < answersList.getLength(); j++) {
                    Node node = answersList.item(j);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        answers.add(node.getTextContent());
                    }
                }
                NodeList correctAnswersList = element.getElementsByTagName("correctAnswers").item(0).getChildNodes();
                for (int j = 0; j < correctAnswersList.getLength(); j++) {
                    Node node = correctAnswersList.item(j);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        correctAnswers.add(Boolean.valueOf(node.getTextContent()));
                    }
                }

                CustomTab customTab = new CustomTab("Question " + (tabs.size() + 1), question, imageURL, answersType, answers, correctAnswers);
                tabs.add(customTab);
                this.getTabs().add(customTab);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println(e);
        }

        addReaction();
    }

    private void addReaction() {
        for (CustomTab tab : tabs) {
            String answersType = tab.getAnswersType();
            ObservableList<javafx.scene.Node> answers = tab.gethBox().getChildren();
            ArrayList<Boolean> correctAnswers = tab.getCorrectAnswers();
            Button button = tab.getButton();

            if (tabs.indexOf(tab) == 0) {
                tab.setDisable(false);
            }

            button.setOnMouseClicked(mouseEvent -> {
                boolean b = true;

                switch (answersType) {
                    case "CheckBox" -> {
                        for (javafx.scene.Node node : answers) {
                            CheckBox checkBox = (CheckBox) node;
                            if (checkBox.isSelected() != correctAnswers.get(answers.indexOf(checkBox))) {
                                b = false;
                            }
                        }
                    }
                    case "RadioButton" -> {
                        for (javafx.scene.Node node : answers) {
                            RadioButton radioButton = (RadioButton) node;
                            if (radioButton.isSelected() != correctAnswers.get(answers.indexOf(radioButton))) {
                                b = false;
                            }
                        }
                    }
                    case "ComboBox" -> {
                        ComboBox<String> comboBox = (ComboBox<String>) answers.get(0);
                        int i = comboBox.getSelectionModel().getSelectedIndex();
                        if (i < 0 || !correctAnswers.get(i)) {
                            b = false;
                        }
                    }
                }

                createSecondaryStage(b, tab);
            });
        }
    }

    private void createSecondaryStage(boolean b, CustomTab tab) {
        stage = new Stage();

        Label label;
        if (b && tabs.indexOf(tab) != tabs.size() - 1) {
            label = new Label("Correct answer!");
            label.setTextFill(Color.GREEN);
        } else if (b && tabs.indexOf(tab) == tabs.size() - 1) {
            label = new Label("Congratulations on your successful passing the test!");
            label.setTextFill(Color.GREEN);
        } else  {
            label = new Label("Think again!");
            label.setTextFill(Color.RED);
        }

        Button button = new Button("OK");
        button.setOnMouseClicked(mouseEvent -> {
            destroyStage(b, tab);
        });

        VBox vBox = new VBox(label, button);
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Result");
        stage.setScene(new Scene(vBox));
        stage.showAndWait();
    }

    private void destroyStage(boolean b, CustomTab tab) {
        stage.close();

        if (b && tabs.indexOf(tab) != tabs.size() - 1) {
            tab.setDisable(true);
            tabs.get(tabs.indexOf(tab) + 1).setDisable(false);
            this.getSelectionModel().select(tabs.indexOf(tab) + 1);
        } else if (b && tabs.indexOf(tab) == tabs.size() - 1) {
            tab.setDisable(true);
        }
    }
}
