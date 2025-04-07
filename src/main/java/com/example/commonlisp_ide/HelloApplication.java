package com.example.commonlisp_ide;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.io.*;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 777, 529);
        stage.setTitle("CommonLisp IDE");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}