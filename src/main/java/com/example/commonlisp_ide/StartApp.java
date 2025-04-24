package com.example.commonlisp_ide;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class StartApp {
    @FXML
    private Button CreateProjectButton;


    @FXML
    private Button OpenProjectButton;
    @FXML
    void initialize(){
        System.out.println(123);
        CreateProjectButton.setOnAction(actionEvent->{

            FXMLLoader loader = new FXMLLoader(StartApp.class.getResource("CreateProject.fxml"));
            try {
                CreateProjectButton.getScene().getWindow().hide();
                Scene scene = new Scene(loader.load(),600,400);

                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Создать проект");
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }
}
