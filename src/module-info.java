module SummerTerm {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.swt;
    requires javafx.media;

    opens edu.ncu.yz.System.entity;
    opens edu.ncu.yz.System.controller;
    opens edu.ncu.yz.System.view;
    opens edu.ncu.yz.System;

    exports edu.ncu.yz.System.entity;
    exports edu.ncu.yz.System.controller;
    exports edu.ncu.yz.System;


}