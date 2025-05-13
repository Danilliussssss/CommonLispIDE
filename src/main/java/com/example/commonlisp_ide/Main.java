package com.example.commonlisp_ide;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.*;

public class Main {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private VBox vBox;

    @FXML
    private StyleClassedTextArea OutputArea;

    @FXML
    private Button Run;
    @FXML
    private MenuBar menuBar;

    private Process sbclProcess;
    private BufferedWriter processInput;
    private BufferedReader processOutput;
    private BufferedReader processError;
    private AtomicInteger packageCounter;
    private FileChooser fileChooser;
    private boolean flagLoadComand = false;
    @FXML
    BorderPane num;


    @FXML
    private StyleClassedTextArea InputArea;
    ArrayList<String> compile;

    @FXML
    void initialize() {
        compile = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Project.getInstance().getPath()));
            String line;
            while ((line = reader.readLine()) != null) {
                InputArea.appendText(line+"\n");
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        startSBCL();
        highLightText(InputArea,"defun","highlight-def");
        highLightText(InputArea,"cond","highlight-syntax-cons");
        highLightText(InputArea,"if","highlight-syntax-cons");
        highLightText(InputArea,"loop","highlight-syntax-cons");
        menuBar.getMenus().get(0).getItems().get(0).setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader(StartApp.class.getResource("CreateProject.fxml"));
            try {
                menuBar.getScene().getWindow().hide();
                Scene scene = new Scene(loader.load(),600,400);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Создать проект");
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        InputArea.setParagraphGraphicFactory(index->{
            Label lineNumber = new Label(String.valueOf(index+1));
            lineNumber.setStyle("-fx-padding: 0 10 0 0; -fx-text-fill: #424242; -fx-font-size: 14px;");
            return lineNumber;
        });

        menuBar.getMenus().get(0).getItems().get(1).setOnAction(actionEvent -> {
           fileChooser = new FileChooser();
           fileChooser.setTitle("Выберите файл");
           fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл CommonLisp","*lsp"));
           Stage stage = new Stage();
           File file = fileChooser.showOpenDialog(stage);
           if(file!=null) {
               FXMLLoader loader = new FXMLLoader(CreateProject.class.getResource("Main.fxml"));
               Project.getInstance().setName(file.getName());
               Project.getInstance().setPath(file.getPath());
               try {
                   Scene scene = new Scene(loader.load(), 777, 541);
                   menuBar.getScene().getWindow().hide();
                   stage.setScene(scene);
                   stage.setTitle(file.getName());
                   stage.show();
               } catch (IOException ex) {
                   throw new RuntimeException(ex);
               }
           }
        });
        menuBar.getMenus().get(0).getItems().get(2).setOnAction(actionEvent -> {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(Project.getInstance().getPath()));
                writer.write(InputArea.getText());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        menuBar.getMenus().get(1).getItems().get(0).setOnAction(actionEvent ->{
            sendCodeToLisp("(compile-file \"Projects/"+Project.getInstance().getName()+"\") ");

        });
        menuBar.getMenus().get(1).getItems().get(1).setOnAction(actionEvent->{
            fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите файл");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Скомпилированный файл","*fasl"));
            Stage stage = new Stage();
            File file = fileChooser.showOpenDialog(stage);
            if(file!=null) {
                sendCodeToLisp("(load \"Projects/"+file.getName()+"\") ");
                compile.add("(load \"Projects/"+file.getName()+"\") ");

            }

        });

        Run.setOnAction(e -> {


            System.out.println("\n\n\n\n\n\n\n\n\n");
            sendCodeToLisp(InputArea.getText());




        });

        InputArea.textProperty().addListener((obs,oldText,newText)->{
            InputArea.clearStyle(0,InputArea.getText().length());
            highLightText(InputArea,"defun","highlight-def");
            highLightText(InputArea,"defmacro","highlight-def");
            highLightText(InputArea,"macroexpand","highlight-def");
            highLightText(InputArea,"cond","highlight-syntax-cons");
            highLightText(InputArea,"if","highlight-syntax-cons");
            highLightText(InputArea,"loop","highlight-syntax-cons");


        });

    }

    private void highLightText(StyleClassedTextArea textArea,String searchText,String styleClass){
        String text = textArea.getText();
        int index = 0;

        while((index = text.indexOf(searchText,index))!=-1){
                    if(Character.isWhitespace(text.charAt(index+searchText.length()))
                            &&(index==0||Character.isWhitespace(text.charAt(index-1))||text.charAt(index-1)=='(')){

                        textArea.setStyleClass(index, index + searchText.length(), styleClass);
                    }
                    index += searchText.length();
        }
    }

    private void startSBCL() {


        try {
            ProcessBuilder pb = new ProcessBuilder("C:\\Users\\Danilka\\IdeaProjects\\CommonLisp_IDE\\Steel Bank Common Lisp\\sbcl.exe",
                    "--noinform",
                     "--eval","(sb-ext:disable-debugger)",
                   // "--eval", "(setq *debug-io* (make-broadcast-stream))",
                    "--eval", "(declaim (sb-ext:muffle-conditions style-warning))"
            );
            sbclProcess = pb.start();
            processInput = new BufferedWriter(new OutputStreamWriter(sbclProcess.getOutputStream()));
            processOutput = new BufferedReader(new InputStreamReader(sbclProcess.getInputStream()));
            processError = new BufferedReader(new InputStreamReader(sbclProcess.getErrorStream()));
            new Thread((this::readLispOutput)).start();
            new Thread(this::readLispError).start();


        } catch (IOException e) {
            OutputArea.appendText("Ошибка запуска SBCL: " + e.getMessage() + "\n");
            throw new RuntimeException(e);
        }
    }
    private void sendCodeToLisp(String param) {

       if(param.contains("load"))
           flagLoadComand = true;
       else {
           flagLoadComand = false;
           OutputArea.clear();
       }
       System.out.println("Флаг установлен: "+flagLoadComand);
    try {
        processInput.write(param + "\n");

        processInput.flush();
    } catch (IOException e) {
        appendToOutput("Ошибка отправки: " + e.getMessage() + "\n");
        throw new RuntimeException(e);
    }


    }

    private void readLispOutput() {
        String line;



            try {


                 while ((line = processOutput.readLine()) != null) {
                     if(!flagLoadComand) {

                         appendToOutput(line);
                     }

                }

            } catch (IOException e) {
                appendToOutput("Ошибка чтения stdout" + "\n");
            }
        System.out.println("Сработал метод readLispOutput : "+flagLoadComand);

    }
    private void appendToOutput(String text){
        Platform.runLater(()->OutputArea.appendText(text+"\n"));

    }
    private void readLispError(){
        String line;

        try {
            while ((line = processError.readLine())!=null){

                appendToOutput("[Ошибка]"+ line);

            }





            if(!sbclProcess.isAlive()) {
                startSBCL();
                for (String code : compile)
                    sendCodeToLisp(code);

            }



        } catch (IOException e) {
            appendToOutput("Ошибка чтения stderr"+ e.getMessage());
            throw new RuntimeException(e);
        }


    }

}