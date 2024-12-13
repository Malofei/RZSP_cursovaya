/**
 * Информация о модуле. Определяет зависимости модуля.
 */
module com.example.cursach {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.logging.log4j;

    opens com.example.cursach to javafx.fxml;
    exports com.example.cursach;
}