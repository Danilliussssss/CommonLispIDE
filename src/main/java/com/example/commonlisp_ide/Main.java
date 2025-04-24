package com.example.commonlisp_ide;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea InputArea;
    @FXML
    private TextArea OutputArea;

    @FXML
    private Button Run;
    private Process sbclProcess;
    private BufferedWriter processInput;
    private BufferedReader processOutput;
    private BufferedReader processError;
    private AtomicInteger packageCounter;
    ;

    @FXML
    void initialize() {
        //Run = new Button("",new ImageView(new Image("Run Icon.png")));
        Run.setOnAction(e -> sendCodeToLisp());

        startSBCL();


    }

    private void startSBCL() {

        try {

            ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\Steel Bank Common Lisp\\sbcl.exe", "--noinform", "--disable-debugger");
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
    private void restartSBCL(){
        if(sbclProcess!=null&&sbclProcess.isAlive()){
            sbclProcess.destroy();
            try {
                sbclProcess.waitFor(1, TimeUnit.SECONDS);
            }catch (InterruptedException e){
                appendToOutput("Ошибка при завершении SBCL:"+ e.getMessage());
            }
        }

        try {
            ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\Steel Bank Common Lisp\\sbcl.exe", "--noinform", "--disable-debugger");
            sbclProcess = pb.start();
            processInput = new BufferedWriter(new OutputStreamWriter(sbclProcess.getOutputStream()));
            processOutput = new BufferedReader(new InputStreamReader(sbclProcess.getInputStream()));
            processError = new BufferedReader(new InputStreamReader(sbclProcess.getErrorStream()));
            new Thread((this::readLispOutput)).start();
            new Thread(this::readLispError).start();
        } catch (IOException e) {
            appendToOutput("Ошибка перезапуска процесса SBCL: "+e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private void sendCodeToLisp() {
        OutputArea.clear();
        restartSBCL();
        String code = InputArea.getText().trim();
        try {
            processInput.write(code + "\n");
            processInput.flush();
        } catch (IOException e) {
            appendToOutput("Ошибка отправки кода: " + e.getMessage() + "\n");
            throw new RuntimeException(e);
        }
    }

    private void readLispOutput() {
        String line;
        try {
            while ((line = processOutput.readLine()) != null) {

                appendToOutput(line);
            }
        } catch (IOException e) {
            appendToOutput("Ошибка чтения stdout"+ "\n");
        }

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
        } catch (IOException e) {
            appendToOutput("Ошибка чтения stderr"+ e.getMessage());
            throw new RuntimeException(e);
        }

    }

}