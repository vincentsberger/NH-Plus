module de.hitec.nhplus {
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires spire.doc.free;

    opens de.hitec.nhplus.controller to javafx.fxml;
    opens de.hitec.nhplus.controller.scenes to javafx.fxml;
    opens de.hitec.nhplus.controller.patient to javafx.fxml;
    opens de.hitec.nhplus.controller.caregiver to javafx.fxml;
    opens de.hitec.nhplus.controller.treatment to javafx.fxml;
    opens de.hitec.nhplus.model to javafx.base;

    exports de.hitec.nhplus;
    exports de.hitec.nhplus.controller;
    exports de.hitec.nhplus.model;
}
