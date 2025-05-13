package com.example.commonlisp_ide;

public class Project {
    private static Project instance = new Project();
    String name;
    String path;

    public static Project getInstance() {
        return instance;
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
}
