package com.example.commonlisp_ide;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class StartApp {
    @FXML
    private Button CreateProjectButton;
    @FXML
    private ListView<String> ListProject;
    ObservableList<String> ProjectParam;
    private File directory;
    @FXML
    private Button OpenProjectButton;
    @FXML
    AnchorPane haulst;
    @FXML
    ImageView LightView;
    @FXML
    ImageView darkView;
    @FXML
    Text labelCreate;
    @FXML
    void initialize() throws IOException {


        if(Project.getInstance().loadGlobalSettings("theme","value")!=null&&Project.getInstance().loadGlobalSettings("theme","value").equals("light"))
            lightTheme();
        else darkTheme();

        ProjectParam = FXCollections.observableArrayList();
        directory = new File("Projects");
        FilenameFilter filter = (dir,name) -> name.endsWith(".lsp");

        if(directory.isDirectory()){
            String[] files = directory.list(filter);
            File[] projects = directory.listFiles();
            if (projects!=null) {
               for(File file : projects)
                   if(file.isDirectory())
                Collections.addAll(ProjectParam, file.getName());
                }

            ListProject.setItems(ProjectParam);


        }
        else System.out.println("Директории не существует!");
        ListProject.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newVal)->{
            if(newVal!=null) {
                FXMLLoader loader = new FXMLLoader(StartApp.class.getResource("Main.fxml"));
                 System.out.println(newVal);


                try {
                    Project.getInstance().setName(newVal);
                    Project.getInstance().setPath("Projects\\"+newVal);
                    CreateProjectButton.getScene().getWindow().hide();
                    Scene scene = new Scene(loader.load(), 777, 620);


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
    public void darkTheme(){


        labelCreate.setStyle("-fx-fill: #F5FFFA;");
        darkView = new ImageView(getClass().getResource("/com/example/commonlisp_ide/CreateProject.png").toExternalForm());
        darkView.setFitHeight(44);
        darkView.setFitWidth(44);
        CreateProjectButton.setStyle("-fx-background-color: #393939;");
        CreateProjectButton.setGraphic(darkView);
        haulst.setStyle("-fx-background-color: #343434;");
        ListProject.getStyleClass().add("dark");
        ListProject.getStyleClass().add("dark-theme");


    }
    public void lightTheme(){

        labelCreate.setStyle("-fx-fill: #343434;");
        LightView = new ImageView(getClass().getResource("/com/example/commonlisp_ide/CreateProjectLight.png").toExternalForm());
        LightView.setFitHeight(44);
        LightView.setFitWidth(44);
        CreateProjectButton.setStyle("-fx-background-color: #F5FFFA;");
        CreateProjectButton.setGraphic(LightView);
        haulst.setStyle("-fx-background-color:  #FFDAB9;");
        ListProject.getStyleClass().add("light");
        ListProject.getStyleClass().add("light-theme");

    }
}