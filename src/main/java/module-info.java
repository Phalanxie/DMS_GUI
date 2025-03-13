module com.swdprojects.romkaicode.dmsgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;

    opens com.swdprojects.romkaicode.dmsgui to javafx.fxml;
    exports com.swdprojects.romkaicode.dmsgui;
}