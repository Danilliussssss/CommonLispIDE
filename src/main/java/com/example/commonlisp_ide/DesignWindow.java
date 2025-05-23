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
    private ComboBox<String> textSize;

    @FXML
    private Button Save;
    @FXML
    private Label defunLabel;
    @FXML
    private Label keywordLabel;
    @FXML
    private Label codeLabel;
    @FXML
    private Label sizeLabel;

    @FXML
    AnchorPane haulst;


    private static final Map<String,String> stylesItem = new HashMap<>();
    static {
        stylesItem.put("Зелёный","-fx-text-fill: #008000;");
        stylesItem.put("Красный","-fx-text-fill: #FF0000;");
        stylesItem.put("Синий","-fx-text-fill: #0000FF;");
        stylesItem.put("Жёлтый","-fx-text-fill: #FFFF00;");
        stylesItem.put("Фиолетовый","-fx-text-fill: purple;");
        stylesItem.put("Стандарт","-fx-text-fill: #292929;");
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
        styles.put("Стандарт","white");
        styles.put("Тёмный","dark");
    }
    private static final Map<String,String> sizeMap = new HashMap<>();
    static {
        sizeMap.put("14 px","14px");
        sizeMap.put("15 px","15px");
        sizeMap.put("16 px","16px");
        sizeMap.put("17 px","17px");
    }
    @FXML
    void initialize() throws IOException {

        if(Project.getInstance().loadGlobalSettings("theme","value")!=null&&Project.getInstance().loadGlobalSettings("theme","value").equals("light"))
            lightTheme();
        else darkTheme();

        defunColor.setValue(Project.getInstance().loadSettings("defunBoxValue","Фиолетовый"));
        keywordColor.setValue(Project.getInstance().loadSettings("keywordBoxValue","Оранжевый"));
        codeColor.setValue(Project.getInstance().loadSettings("codeBoxValue","Стандарт"));
         defunColor.getItems().addAll("Зелёный","Красный","Синий","Жёлтый","Фиолетовый");
         textSize.getItems().addAll("14 px","15 px", "16 px","17 px");
        textSize.setValue(Project.getInstance().loadSettings("text-size-value","14 px"));
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
                             setStyle(stylesItem.get(item)+"-fx-background-color: #F5FFFA;");
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

                            setStyle(stylesItem.get(item)+"-fx-background-color: #F5FFFA;");
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


                            setStyle(stylesItem.get(item)+"-fx-background-color: #F5FFFA;");

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
                    setStyle(stylesItem.get(item)+"-fx-background-color: #F5FFFA;");
                }
            }
        });
        codeColor.getItems().addAll("Зелёный","Красный","Синий","Жёлтый","Стандарт","Тёмный");
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
                    if(!item.equals("Стандарт"))
                     setStyle(stylesItem.get(item)+"-fx-background-color: #F5FFFA;");
                    else  setStyle("-fx-text-fill: #292929;"+"-fx-background-color: #F5FFFA;");
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
                    if(!item.equals("Стандарт"))
                        setStyle(stylesItem.get(item)+"-fx-background-color: #F5FFFA;");
                    else  setStyle("-fx-text-fill: #292929;"+"-fx-background-color: #F5FFFA;");
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
                Project.getInstance().saveSettings("text-size",sizeMap.get(textSize.getValue()));
                Project.getInstance().saveSettings("text-size-value",textSize.getValue());
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
