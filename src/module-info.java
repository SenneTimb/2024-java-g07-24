open module SDPII_Java_G07_24 {
    exports domain;
    exports gui;
    exports main;
    exports repository;
    exports testen;

    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    requires jakarta.persistence;
    requires java.sql;
    requires java.instrument;

    requires org.junit.jupiter.api;
    requires org.mockito;
    requires org.mockito.junit.jupiter;
    requires org.junit.jupiter.params;
	requires java.desktop;
    requires java.mail;
	requires de.mkammerer.argon2;
}