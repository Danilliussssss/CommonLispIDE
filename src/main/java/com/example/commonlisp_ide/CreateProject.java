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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateProject {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField NameProject;

    @FXML
    private TextField ProjectFolder;
    @FXML
    private Button Create;
    @FXML
    private Button Back;

    @FXML
    void initialize() {

        Create.setOnAction( e->{
            Create.getScene().getWindow().hide();
            try {
                File path = new File(ProjectFolder.getText());
                if (!path.exists())
                    path.mkdir();
                FileWriter writer = new FileWriter(ProjectFolder.getText()+NameProject.getText()+".lsp");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            FXMLLoader loader = new FXMLLoader(CreateProject.class.getResource("Main.fxml"));
            try {
                Scene scene = new Scene(loader.load(),777,541);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle(NameProject.getText());
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
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
}
