module com.adrian {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires transitive javafx.graphics;

    opens com.adrian to javafx.fxml;
    opens com.adrian.DataAccess to javafx.base;
    opens com.adrian.LogicBusiness to javafx.base;

    exports com.adrian;
}
