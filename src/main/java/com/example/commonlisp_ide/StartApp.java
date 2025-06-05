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



       if(Project.getInstance().loadGlobalSettings("theme","value")==null||Project.getInstance().loadGlobalSettings("theme","value").equals("value")) {

           Project.getInstance().saveGlobalSettings("theme", "dark");
       }
        if(Project.getInstance().loadGlobalSettings("defunColor","value")==null||Project.getInstance().loadGlobalSettings("defunColor","value").equals("value")) {

            Project.getInstance().saveGlobalSettings("defunColor", "purple");
        }
        if(Project.getInstance().loadGlobalSettings("keywordColor","value")==null||Project.getInstance().loadGlobalSettings("keywordColor","value").equals("value"))
            Project.getInstance().saveGlobalSettings("keywordColor","orange");
        if(Project.getInstance().loadGlobalSettings("codeColor","value")==null||Project.getInstance().loadGlobalSettings("codeColor","value").equals("value"))
            Project.getInstance().saveGlobalSettings("codeColor","white");
        if(Project.getInstance().loadGlobalSettings("text-size","value")==null||Project.getInstance().loadGlobalSettings("text-size","value").equals("value"))
            Project.getInstance().saveGlobalSettings("text-size","14px");
        if(Project.getInstance().loadGlobalSettings("theme","dark").equals("light"))
            lightTheme();
        else darkTheme();

        ProjectParam = FXCollections.observableArrayList();
        directory = new File("Projects");


        if(directory.isDirectory()){

            File[] projects = directory.listFiles();
            if (projects!=null) {
               for(File file : projects)
                   if(file.isDirectory())
                Collections.addAll(ProjectParam, file.getName());
                }

            ListProject.setItems(ProjectParam);


        }
        else {
            directory.mkdir();
            System.out.println("Директории не существует!");
        }

        ListProject.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newVal)->{
            if(newVal!=null) {
                FXMLLoader loader = new FXMLLoader(StartApp.class.getResource("Main.fxml"));



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

        haulst.widthProperty().addListener(((observable, oldValue, newValue) -> {

            AnchorPane.setRightAnchor(ListProject,(newValue.doubleValue()-200)/2);
            AnchorPane.setRightAnchor(CreateProjectButton,(newValue.doubleValue()-44)/2);
            AnchorPane.setRightAnchor(labelCreate,(newValue.doubleValue()-70)/2);


        }));


        haulst.heightProperty().addListener(((observable, oldValue, newValue) -> {

            AnchorPane.setTopAnchor(ListProject,(newValue.doubleValue()-150)/2);


            AnchorPane.setTopAnchor(CreateProjectButton,(newValue.doubleValue()-350)/2);
            AnchorPane.setTopAnchor(labelCreate,(newValue.doubleValue()-200)/2);

        }));
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
    //Функция, включающая тёмную тему
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
    //Функция, активирующая светлую тему для данного окна
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