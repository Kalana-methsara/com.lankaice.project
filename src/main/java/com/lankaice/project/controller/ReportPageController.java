package com.lankaice.project.controller;

import com.lankaice.project.db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ReportPageController {

    @FXML
    private Button btnAttendanceReport;

    @FXML
    private Button btnCustomerReport;

    @FXML
    private Button btnEmployeeReport;

    @FXML
    private Button btnInventoryReport;

    @FXML
    private Button btnItemReport;

    @FXML
    private Button btnOrderReport;

    @FXML
    private Button btnSalaryReport;

    @FXML
    private Button btnSalesReport;

    @FXML
    private Button btnSupplierReport;

    @FXML
    void CustomerReportOnAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/report/CustomerReport.jrxml")
            );

            Connection connection = DBConnection.getInstance().getConnection();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("p_date", LocalDate.now().toString());

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters, // null
                    connection
            );

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    void btnAttendanceReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnEmployeeReportOnAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/report/EmployeeReport.jrxml")
            );

            Connection connection = DBConnection.getInstance().getConnection();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("p_date", LocalDate.now().toString());

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters, // null
                    connection
            );

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    void btnInventoryReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnItemReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnOrderReportOnAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/report/OrderReport.jrxml")
            );

            Connection connection = DBConnection.getInstance().getConnection();

            Map<String, Object> parameters = new HashMap<>();
           // parameters.put("p_date", LocalDate.now().toString());

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters, // null
                    connection
            );

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    void btnSalaryReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnSalesReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnSupplierReportOnAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/report/SalesReport.jrxml")
            );

            Connection connection = DBConnection.getInstance().getConnection();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("p_date", LocalDate.now().toString());

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters, // null
                    connection
            );

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
