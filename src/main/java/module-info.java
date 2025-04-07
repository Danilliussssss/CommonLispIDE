module com.example.commonlisp_ide {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.commonlisp_ide to javafx.fxml;
    exports com.example.commonlisp_ide;
}