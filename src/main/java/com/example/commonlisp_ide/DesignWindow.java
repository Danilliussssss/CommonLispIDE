package com.example.commonlisp_ide;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class DesignWindow {



    @FXML
    private ComboBox<String> defunColor;

    @FXML
    private ComboBox<String> keywordColor;
    @FXML
    private ComboBox<String> codeColor;

    @FXML
    private Button Save;
    @FXML
    private Label defunLabel;
    @FXML
    private Label keywordLabel;
    @FXML
    private Label codeLabel;
    @FXML
    AnchorPane haulst;


    private static final Map<String,String> stylesItem = new HashMap<>();
    static {
        stylesItem.put("Зелёный","-fx-text-fill: #008000;");
        stylesItem.put("Красный","-fx-text-fill: #FF0000;");
        stylesItem.put("Синий","-fx-text-fill: #0000FF;");
        stylesItem.put("Жёлтый","-fx-text-fill: #FFFF00;");
        stylesItem.put("Фиолетовый","-fx-text-fill: purple;");
        stylesItem.put("Белый","-fx-text-fill: #FAFFF5;");
        stylesItem.put("Оранжевый","-fx-text-fill: #FF8C00;");
        stylesItem.put("Тёмный","-fx-text-fill: #292929;");



    }
    private static final Map<String,String> styles = new HashMap<>();
    static {
        styles.put("Зелёный","green");
        styles.put("Красный","red");
        styles.put("Синий","blue");
        styles.put("Жёлтый","yellow");
        styles.put("Фиолетовый","purple");
        styles.put("Оранжевый","orange");
        styles.put("Белый","white");
        styles.put("Тёмный","dark");
    }

    @FXML
    void initialize() throws IOException {

        if(Project.getInstance().loadGlobalSettings("theme","value")!=null&&Project.getInstance().loadGlobalSettings("theme","value").equals("light"))
            lightTheme();
        else darkTheme();
        defunColor.setValue(Project.getInstance().loadSettings("defunBoxValue","Фиолетовый"));
        keywordColor.setValue(Project.getInstance().loadSettings("keywordBoxValue","Оранжевый"));
        codeColor.setValue(Project.getInstance().loadSettings("codeBoxValue","Белый"));
         defunColor.getItems().addAll("Зелёный","Красный","Синий","Жёлтый","Фиолетовый");
         defunColor.setCellFactory(listView -> new ListCell<>(){
             @Override
             protected void updateItem(String item, boolean empty) {
                 super.updateItem(item, empty);
                 if(empty||item==null) {
                     setText(null);
                     setStyle("");
                 }
                 else {
                     System.out.println(stylesItem.get(item));
                     setText(item);

                     try {
                         if(Project.getInstance().loadGlobalSettings("theme","value").equals("dark"))
                             setStyle(stylesItem.get(item)+"-fx-background-color: #5F9EA0;");
                     } catch (IOException e) {
                         throw new RuntimeException(e);
                     }
                 }
             }
         });
        defunColor.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty||item==null) {
                    setText(null);
                    setStyle("");
                }
                else {

                    setText(item);
                    try {
                        if(Project.getInstance().loadGlobalSettings("theme","value").equals("dark"))
                         setStyle(stylesItem.get(item)+"-fx-background-color: #5F9EA0;");

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
        keywordColor.getItems().addAll("Зелёный","Красный","Синий","Жёлтый","Оранжевый");
        keywordColor.setCellFactory(listView -> new ListCell<>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty||item==null) {
                    setText(null);
                    setStyle("");
                }
                else {

                    setText(item);
                    try {
                        if(Project.getInstance().loadGlobalSettings("theme","value").equals("dark"))
                            setStyle(stylesItem.get(item)+"-fx-background-color: #5F9EA0;");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        keywordColor.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty||item==null) {
                    setText(null);
                    setStyle("");
                }
                else {
                    System.out.println(stylesItem.get(item));
                    setText(item);
                    setStyle(stylesItem.get(item)+"-fx-background-color: #5F9EA0;");
                }
            }
        });
        codeColor.getItems().addAll("Зелёный","Красный","Синий","Жёлтый","Белый","Тёмный");
        codeColor.setCellFactory(listView -> new ListCell<>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty||item==null) {
                    setText(null);
                    setStyle("");
                }
                else {

                    setText(item);
                    setStyle(stylesItem.get(item)+"-fx-background-color: #5F9EA0;");
                }
            }
        });
        codeColor.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty||item==null) {
                    setText(null);
                    setStyle("");
                }
                else {
                    System.out.println(stylesItem.get(item));
                    setText(item);
                    setStyle(stylesItem.get(item)+"-fx-background-color: #5F9EA0;");
                }
            }
        });
        Save.setOnAction(actionEvent->{

            try {
                Project.getInstance().saveSettings("defunColor",styles.get(defunColor.getValue()));
                Project.getInstance().saveSettings("keywordColor",styles.get(keywordColor.getValue()));
                Project.getInstance().saveSettings("codeColor",styles.get(codeColor.getValue()));
                Project.getInstance().saveSettings("defunBoxValue",defunColor.getValue());
                Project.getInstance().saveSettings("keywordBoxValue",keywordColor.getValue());
                Project.getInstance().saveSettings("codeBoxValue",codeColor.getValue());
                Project.getInstance().getMain().highLightText_changed();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
    public void darkTheme(){
        haulst.setStyle("-fx-background-color: #343434;");
        defunLabel.setStyle("-fx-text-fill: #F5FFFA;");
        keywordLabel.setStyle("-fx-text-fill: #F5FFFA;");
        codeLabel.setStyle("-fx-text-fill: #F5FFFA;");

        keywordColor.setStyle("-fx-background-color: #292929;");
        codeColor.setStyle("-fx-background-color: #292929;");

        defunColor.getStyleClass().add("dark");


    }
    public void lightTheme(){
        haulst.setStyle("-fx-background-color:  #FFDAB9;");
        defunLabel.setStyle("-fx-text-fill: #343434;");
        keywordLabel.setStyle("-fx-text-fill: #343434;");
        codeLabel.setStyle("-fx-text-fill: #343434;");
        defunColor.getStyleClass().add("light");


    }
}
