module org.example.bank_po {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.bank_po to javafx.fxml;
    exports org.example.bank_po;
    exports org.example.bank_po.gui;
    opens org.example.bank_po.gui to javafx.fxml;
    exports org.example.bank_po.model;
    opens org.example.bank_po.model to javafx.fxml;
    exports org.example.bank_po.exceptions;
    opens org.example.bank_po.exceptions to javafx.fxml;
    exports org.example.bank_po.interfaces;
    opens org.example.bank_po.interfaces to javafx.fxml;
}