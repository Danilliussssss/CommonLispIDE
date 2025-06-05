package com.example.commonlisp_ide;

import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class Project {
    private static Project instance = new Project();
    String name;
    String path;
    Main main;
    CreateProject createProject;
    DesignWindow designWindow;
    private Properties properties;
    public static Project getInstance() {
        return instance;
    }
    public void setMain(Main param){
        main = param;
    }
    public Main getMain() {
        return main;
    }
    public DesignWindow getDesignWindow() {
        return designWindow;
    }

    public void setDesignWindow(DesignWindow designWindow) {
        this.designWindow = designWindow;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
    //Загрузка настроек в файл среды разработки .properties
    public void saveGlobalSettings(String key,String value) throws IOException {
        properties = new Properties();
        File file = new File("src/main/java/com/example/commonlisp_ide/global_settings.properties");
        if (!file.exists()) {
            file.createNewFile();
        }
            try {
                InputStream input = new FileInputStream(file);
                properties.load(input);
                properties.setProperty(key, value);
                OutputStream output = new FileOutputStream("src/main/java/com/example/commonlisp_ide/global_settings.properties");
                properties.store(output, "Saved changes");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
    //Считывание настроек из файла среды разработки .properties
    public String loadGlobalSettings(String key,String value) throws IOException {
        properties = new Properties();
        try {
            InputStream input = new FileInputStream("src/main/java/com/example/commonlisp_ide/global_settings.properties");
            properties.load(input);
            return properties.getProperty(key);
        } catch (FileNotFoundException e) {
            return value;
        }
    }
    //Загрузка настроек в файл проекта .properties
    public void saveSettings(String key,String value) throws IOException {
        properties = new Properties();

        File file = new File(path+"\\settings.properties");
        if (!file.exists())
            file.createNewFile();
            try {
                InputStream input = new FileInputStream(file);
                properties.load(input);
                properties.setProperty(key, value);
                OutputStream output = new FileOutputStream(path + "\\settings.properties");
                properties.store(output,"Saved changes");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
    //Считывание настроек из файла проекта .properties
    public String loadSettings(String key,String value) throws IOException {
        properties = new Properties();
        try {
            InputStream input = new FileInputStream(path+"\\settings.properties");
            properties.load(input);
            return properties.getProperty(key);
        } catch (FileNotFoundException e) {
            return value;
        }
    }
    //Функция создания проекта
    public void createProject(String name,String path){
        this.name = name;
        this.path = path;
        File pathProject = new File(path);
        if(!pathProject.exists())
            pathProject.mkdir();

        try {
            saveSettings("name",name);
            saveSettings("path",path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            FileWriter writer = new FileWriter(path+"\\"+name+".lsp");
            if(loadGlobalSettings("defunColor","empty").equals("empty"))
              saveGlobalSettings("defunColor","purple");
            if(loadGlobalSettings("keywordColor","empty").equals("empty"))
              saveGlobalSettings("keywordColor","orange");
            if(loadGlobalSettings("codeColor","empty").equals("empty"))
                saveGlobalSettings("codeColor","white");
            if(loadGlobalSettings("text-size","empty").equals("empty"))
                saveGlobalSettings("text-size","14px");
            if(loadGlobalSettings("theme","empty").equals("empty"))
                saveGlobalSettings("theme", "dark");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}