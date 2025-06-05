package com.example.commonlisp_ide;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CreateProject {

    @FXML
    AnchorPane haulst;
    @FXML
    Text nameLabel;
    @FXML
    Text pathLabel;

    @FXML
    private TextField NameProject;

    @FXML
    private TextField ProjectFolder;
    @FXML
    private Button Create;
    @FXML
    private Button Back;

    @FXML
    void initialize() throws IOException {
        if(Project.getInstance().loadGlobalSettings("theme","value")!=null&&Project.getInstance().loadGlobalSettings("theme","value").equals("light"))
            lightTheme();
        else darkTheme();
        haulst.widthProperty().addListener(((observable, oldValue, newValue) -> {

            AnchorPane.setLeftAnchor(nameLabel,(newValue.doubleValue() - 480)/2);
            AnchorPane.setLeftAnchor(pathLabel,(newValue.doubleValue() - 480)/2);
            AnchorPane.setLeftAnchor(NameProject,(newValue.doubleValue()-200)/2);
            AnchorPane.setLeftAnchor(ProjectFolder,(newValue.doubleValue()-200)/2);
            AnchorPane.setLeftAnchor(Back,20.0);
            AnchorPane.setRightAnchor(Create,20.0);




        }));
        haulst.heightProperty().addListener(((observable, oldValue, newValue) -> {

            AnchorPane.setTopAnchor(nameLabel,(newValue.doubleValue()-260)/2);
            AnchorPane.setTopAnchor(NameProject,(newValue.doubleValue()-275)/2);


            AnchorPane.setTopAnchor(pathLabel,(newValue.doubleValue()-110)/2);
            AnchorPane.setTopAnchor(ProjectFolder,(newValue.doubleValue()-125)/2);
            AnchorPane.setBottomAnchor(Back,20.0);
            AnchorPane.setBottomAnchor(Create,20.0);


        }));
        Create.setOnAction( e->{
            if(NameProject.getText()!=null&&!NameProject.getText().equals("")) {
                Create.getScene().getWindow().hide();
                try {
                    File path = new File(ProjectFolder.getText());
                    Project.getInstance().createProject(NameProject.getText(), ProjectFolder.getText() + NameProject.getText());


                    if (!path.exists())
                        path.mkdir();
                    FileWriter writer = new FileWriter(ProjectFolder.getText() + NameProject.getText() + "\\" + NameProject.getText() + ".lsp");


                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                FXMLLoader loader = new FXMLLoader(CreateProject.class.getResource("Main.fxml"));
                try {
                    Scene scene = new Scene(loader.load(), 777, 620);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle(NameProject.getText());
                    stage.show();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Ошибка в имени проекта");
                error.setContentText("Ошибка в имени проекта");
                error.showAndWait();

            }
        });
        Back.setOnAction(e->{
            Back.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(CreateProject.class.getResource("StartApp.fxml"));
            try {
                Scene scene = new Scene(loader.load(),777,541);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("CommonLisp IDE");
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        });

    }
    public void darkTheme(){
        nameLabel.setStyle("-fx-fill: #F5FFFA;");
        pathLabel.setStyle("-fx-fill: #F5FFFA;");
        NameProject.getStyleClass().add("dark");
        ProjectFolder.getStyleClass().add("dark");
        haulst.setStyle("-fx-background-color: #343434;");

    }
    public void lightTheme(){
        nameLabel.setStyle("-fx-fill: #343434;");
        pathLabel.setStyle("-fx-fill: #343434;");
        NameProject.getStyleClass().add("light");
        ProjectFolder.getStyleClass().add("light");
        haulst.setStyle("-fx-background-color:  #FFDAB9;");

    }
}