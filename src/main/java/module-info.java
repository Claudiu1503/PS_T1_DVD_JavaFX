module com.group.mvp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.ooxml;

    opens com.group.mvp.model to javafx.base;


    opens com.group.mvp.view to javafx.fxml;
    exports com.group.mvp.view;
}