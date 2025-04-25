package com.example.commonlisp_ide;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Collections;


public class StartApp {
    @FXML
    private Button CreateProjectButton;
    @FXML
    private ListView<String> ListProject;
    ObservableList<String> ProjectParam;

    @FXML
    private Button OpenProjectButton;
    @FXML
    void initialize(){
        ProjectParam = FXCollections.observableArrayList();
        File directory = new File("C:\\Users\\Danilka\\IdeaProjects\\CommonLisp_IDE\\Projects");
        if(directory.isDirectory()){
            String[] files = directory.list();
            if (files!=null)
             Collections.addAll(ProjectParam, files);
            ListProject.setItems(ProjectParam);


        }
        else System.out.println("Директории не существует!");
        ListProject.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newVal)->{
            if(newVal!=null) {
                FXMLLoader loader = new FXMLLoader(StartApp.class.getResource("Main.fxml"));
                try {
                    CreateProjectButton.getScene().getWindow().hide();
                    Scene scene = new Scene(loader.load(), 777, 541);

                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle(newVal);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

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
