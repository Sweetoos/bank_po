module org.example.bank_po {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.bank_po to javafx.fxml;
    exports org.example.bank_po;
}