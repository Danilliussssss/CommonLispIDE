package com.example.commonlisp_ide;

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
    public void saveGlobalSettings(String key,String value) {
        properties = new Properties();
        File file = new File("src/main/java/com/example/commonlisp_ide/global_settings.properties");

        if (file.exists()) {
            System.out.println(123);
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
    }
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
    public void saveSettings(String key,String value) throws IOException {
        properties = new Properties();
        File file = new File(path+"settings.properties");

        if(file.exists()) {
            System.out.println(123);
            try {
                InputStream input = new FileInputStream(file);
                properties.load(input);
                properties.setProperty(key, value);
                OutputStream output = new FileOutputStream(path + "settings.properties");
                properties.store(output,"Saved changes");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        else {
            System.out.println(456);
            try {
                OutputStream output = new FileOutputStream(path + "settings.properties");

                properties.setProperty(key, value);
                properties.store(output, "Saved changes");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public String loadSettings(String key,String value) throws IOException {
        properties = new Properties();
        try {
            InputStream input = new FileInputStream(path+"settings.properties");
            properties.load(input);
            return properties.getProperty(key);
        } catch (FileNotFoundException e) {
            return value;
        }

    }
    public void createProject(String name,String path){
        this.name = name;
        this.path = path;
        File pathProject = new File(path);

        if(!pathProject.exists())
            pathProject.mkdir();
        try {
            FileWriter writer = new FileWriter(path+name+".lsp");
            saveSettings("defunColor","purple");
            saveSettings("keywordColor","orange");
            saveSettings("theme","dark");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
}