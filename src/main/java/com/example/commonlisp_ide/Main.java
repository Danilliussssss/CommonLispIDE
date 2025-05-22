package com.example.commonlisp_ide;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
    private Button RunDebug;
    @FXML
    private Button Run;
    @FXML
    private MenuBar menuBar;
    @FXML
    AnchorPane haulst;
    private Process sbclProcess;
    private BufferedWriter processInput;
    private BufferedReader processOutput;
    private BufferedReader processError;
    private AtomicInteger packageCounter;
    private FileChooser fileChooser;
    @FXML
    private ToggleButton ThemeSwitch;

    @FXML
    ImageView DarkView;
    @FXML
    ImageView LightView;
    @FXML
    ImageView DarkViewDebug;
    @FXML
    ImageView LightViewDebug;

    private boolean flagLoadComand = false;
    @FXML
    BorderPane num;


    @FXML
    private StyleClassedTextArea InputArea;
    ArrayList<String> compile;

    @FXML
    void initialize() throws IOException {


        Platform.runLater(()->{
            try {
                if(Project.getInstance().loadGlobalSettings("theme","value").equals("light"))
                    lightTheme();
                else
                    darkTheme();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        compile = new ArrayList<>();
        Project.getInstance().setMain(this);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Project.getInstance().getPath()+Project.getInstance().getName()+".lsp"));
            String line;
            while ((line = reader.readLine()) != null) {
                InputArea.appendText(line+"\n");
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        startSBCL();
        highLightText_changed();


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
                Project.getInstance().setName(file.getName().substring(0,file.getName().length()-4));
                Project.getInstance().setPath(file.getPath().substring(0,file.getPath().length() - file.getName().length()));
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
                BufferedWriter writer = new BufferedWriter(new FileWriter(Project.getInstance().getPath()+Project.getInstance().getName()+".lsp"));
                writer.write(InputArea.getText());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        menuBar.getMenus().get(1).getItems().get(0).setOnAction(actionEvent ->{
            System.out.println("Имя файла: "+Project.getInstance().getName());
            sendCodeToLisp("(compile-file \"Projects/"+Project.getInstance().getName()+".lsp"+"\") ");

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
        menuBar.getMenus().get(2).getItems().get(0).setOnAction(actionEvent->{
            FXMLLoader fxmlLoader = new FXMLLoader(CreateProject.class.getResource("DesignWindow.fxml"));
            Stage stage = new Stage();
            try {
                Scene scene = new Scene(fxmlLoader.load(),600,400);
                stage.setScene(scene);
                stage.setTitle("Настройки вида");
                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Run.setOnAction(e -> {


            System.out.println("\n\n\n\n\n\n\n\n\n");
            sendCodeToLisp(InputArea.getText());




        });

        InputArea.textProperty().addListener((obs,oldText,newText)->{
            InputArea.clearStyle(0,InputArea.getText().length());
            try {
                highLightText_changed();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ThemeSwitch.setOnAction(actionEvent-> {
            Platform.runLater(()->{
                try {
                    System.out.println(Project.getInstance().loadGlobalSettings("theme","value"));
                    if(Project.getInstance().loadGlobalSettings("theme","value").equals("dark"))
                        lightTheme();
                    else darkTheme();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

        });
    }
    private void lightTheme() throws IOException {
        LightView = new ImageView(getClass().getResource("/com/example/commonlisp_ide/RunIconLight.png").toExternalForm());
        LightView.setFitHeight(16);
        LightView.setFitWidth(18);
        Run.setGraphic(LightView);
        LightViewDebug = new ImageView(getClass().getResource("/com/example/commonlisp_ide/Debug Icon Light.png").toExternalForm());
        LightViewDebug.setFitHeight(17);
        LightViewDebug.setFitWidth(20);
        RunDebug.setGraphic(LightViewDebug);

        Project.getInstance().saveGlobalSettings("theme", "light");
            ThemeSwitch.setStyle("-fx-background-color: #F5FFFA;-fx-text-fill: #292929;");
            InputArea.setStyleClass(0,InputArea.getText().length(),"dark");
            OutputArea.setStyleClass(0,InputArea.getText().length(),"dark");

            highLightText_changed();
            InputArea.setStyle("-fx-background-color: #F5FFFA;");
            OutputArea.setStyle("-fx-background-color: #F5FFFA;");
            haulst.setStyle("-fx-background-color: #FFDAB9;");
            menuBar.setStyle("-fx-background-color: #F5FFFA;");
            for(int i=0;i<3;i++) {
                menuBar.getMenus().get(i).getStyleClass().remove("light");
                menuBar.getMenus().get(i).getStyleClass().remove("darkHover");
                menuBar.getMenus().get(i).getStyleClass().remove("darkPressed");
                menuBar.getMenus().get(i).getStyleClass().remove("custom-menu-dark");
                menuBar.getMenus().get(i).getStyleClass().add("dark");
                menuBar.getMenus().get(i).getStyleClass().add("lightHover");
                menuBar.getMenus().get(i).getStyleClass().add("lightPressed");
                menuBar.getMenus().get(i).getStyleClass().add("custom-menu-light");

            }
            Run.setStyle("-fx-background-color: #FFDAB9;");
        RunDebug.setStyle("-fx-background-color: #FFDAB9;");
    }
    private void darkTheme() throws IOException {
        System.out.println("darkTheme");
        DarkView = new ImageView(getClass().getResource("/com/example/commonlisp_ide/RunIcon.png").toExternalForm());
        DarkView.setFitHeight(16);
        DarkView.setFitWidth(18);
        Run.setGraphic(DarkView);
        DarkViewDebug = new ImageView(getClass().getResource("/com/example/commonlisp_ide/Debug Icon.png").toExternalForm());
        DarkViewDebug.setFitHeight(17);
        DarkViewDebug.setFitWidth(20);
        RunDebug.setGraphic(DarkViewDebug);
        Project.getInstance().saveGlobalSettings("theme", "dark");
        ThemeSwitch.setStyle("-fx-background-color: #292929;-fx-text-fill: #F5FFFA;");
        InputArea.setStyleClass(0,InputArea.getText().length(),"white");
        OutputArea.setStyleClass(0,InputArea.getText().length(),"white");
        highLightText_changed();
        InputArea.setStyle("-fx-background-color: #292929;");
        OutputArea.setStyle("-fx-background-color: #292929;");
        haulst.setStyle("-fx-background-color: #343434;");
        menuBar.setStyle("-fx-background-color: #292929;");
        for(int i=0;i<3;i++) {

            menuBar.getMenus().get(i).getStyleClass().remove("dark");
            menuBar.getMenus().get(i).getStyleClass().remove("lightHover");
            menuBar.getMenus().get(i).getStyleClass().remove("lightPressed");
            menuBar.getMenus().get(i).getStyleClass().remove("custom-menu-light");
            menuBar.getMenus().get(i).getStyleClass().add("light");
            menuBar.getMenus().get(i).getStyleClass().add("darkHover");
            menuBar.getMenus().get(i).getStyleClass().add("darkPressed");
            menuBar.getMenus().get(i).getStyleClass().add("custom-menu-dark");
        }
        Run.setStyle("-fx-background-color: #343434;");
        RunDebug.setStyle("-fx-background-color: #343434;");
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
     public void highLightText_changed() throws IOException {
        String color;
         if(Project.getInstance().loadGlobalSettings("theme","value").equals("light"))
           color = "dark";
         else color = "light";
         InputArea.setStyleClass(0,InputArea.getText().length(),Project.getInstance().loadSettings("codeColor",color));
         try {
             highLightText(InputArea,"defun",Project.getInstance().loadSettings("defunColor","purple"));
             highLightText(InputArea,"defmacro",Project.getInstance().loadSettings("defunColor","purple"));
             highLightText(InputArea,"macroexpand",Project.getInstance().loadSettings("defunColor","purple"));
             highLightText(InputArea,"cond",Project.getInstance().loadSettings("keywordColor","orange"));
             highLightText(InputArea,"if",Project.getInstance().loadSettings("keywordColor","orange"));
             highLightText(InputArea,"loop",Project.getInstance().loadSettings("keywordColor","orange"));
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
     }
    private void startSBCL() {


        try {
            ProcessBuilder pb = new ProcessBuilder("Steel Bank Common Lisp/sbcl.exe",
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