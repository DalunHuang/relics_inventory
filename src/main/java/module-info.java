module land.test.inv {

    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires odfdom.java;
    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;
    requires org.apache.tika.core;
    requires org.jsoup;
    requires com.opencsv;
    requires org.apache.commons.lang3;

    opens land.test.inv;
    opens land.test.inv.common;
    opens land.test.inv.controller;
    opens land.test.inv.dao;
    opens land.test.inv.dto;
    opens land.test.inv.domain;
    opens land.test.inv.service;

    exports land.test.inv;
}