package com.lankaice.project.controller;

import com.lankaice.project.db.DBConnection;
import com.lankaice.project.dto.OrderPaymentDto;
import com.lankaice.project.model.OrderPaymentModel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentPageController {

    @FXML
    private TableColumn<OrderPaymentDto, Double> colDiscount;

    @FXML
    private TableColumn<OrderPaymentDto, String> colId;

    @FXML
    private TableColumn<OrderPaymentDto, Integer> colItemCount;

    @FXML
    private TableColumn<OrderPaymentDto, Double> colNetTotal;

    @FXML
    private TableColumn<OrderPaymentDto, Integer> colOrderId;

    @FXML
    private TableColumn<OrderPaymentDto, String> colPaymentDate;

    @FXML
    private TableColumn<OrderPaymentDto, String> colPaymentMethod;

    @FXML
    private TableColumn<OrderPaymentDto, String> colStatus;

    @FXML
    private TableColumn<OrderPaymentDto, Double> colSubtotal;

    @FXML
    private TableView<OrderPaymentDto> tableView;

    @FXML
    private TextField textDiscount;

    @FXML
    private TextField textSubtotal;

    @FXML
    private Label txtId;

    @FXML
    private ComboBox<String> txtPaymentMethod;

    @FXML
    private TextField txtSearch;

    @FXML
    private ComboBox<String> txtStatus;

    private final OrderPaymentModel paymentModel = new OrderPaymentModel();

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        initComboBoxes();
        initTableColumns();
        loadTableData();
    }

    private void initComboBoxes() {
        txtPaymentMethod.setItems(FXCollections.observableArrayList("Cash", "Card", "Online"));
        txtStatus.setItems(FXCollections.observableArrayList("Paid", "Pending", "Failed"));
    }

    private void initTableColumns() {
        colId.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPaymentId()));
        colOrderId.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getOrderId()));
        colPaymentMethod.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPaymentMethod()));
        colItemCount.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getItemCount()));
        colSubtotal.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getSubtotal()));
        colDiscount.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getDiscount()));
        colNetTotal.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getNetTotal()));
        colPaymentDate.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPaymentDate()));
        colStatus.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getStatus()));
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        List<OrderPaymentDto> list = paymentModel.getAllPayment();
        tableView.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    void setData(MouseEvent event) {
        OrderPaymentDto dto = tableView.getSelectionModel().getSelectedItem();
        if (dto != null) {
            txtId.setText(dto.getPaymentId());
            txtPaymentMethod.setValue(dto.getPaymentMethod());
            txtStatus.setValue(dto.getStatus());
            textDiscount.setText(String.valueOf(dto.getDiscount()));
            textSubtotal.setText(String.valueOf(dto.getSubtotal()));
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String keyword = txtSearch.getText().trim();
        List<OrderPaymentDto> list = paymentModel.searchPayments(keyword);
        tableView.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        OrderPaymentDto dto = new OrderPaymentDto(
                txtId.getText(),
                tableView.getSelectionModel().getSelectedItem().getOrderId(),
                txtPaymentMethod.getValue(),
                tableView.getSelectionModel().getSelectedItem().getItemCount(),
                Double.parseDouble(textSubtotal.getText()),
                Double.parseDouble(textDiscount.getText()),
                Double.parseDouble(textSubtotal.getText()) - Double.parseDouble(textDiscount.getText()),
                tableView.getSelectionModel().getSelectedItem().getPaymentDate(),
                txtStatus.getValue()
        );

        boolean updated = paymentModel.updatePayment(dto);
        if (updated) {
            loadTableData();
            clearFields();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String id = txtId.getText();
        boolean deleted = paymentModel.deletePayment(id);
        if (deleted) {
            loadTableData();
            clearFields();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        clearFields();
        loadTableData();
        txtSearch.clear();
    }

    private void clearFields() {
        txtId.setText("");
        txtPaymentMethod.setValue(null);
        txtStatus.setValue(null);
        textSubtotal.clear();
        textDiscount.clear();
    }

    @FXML
    void btnReportOnAction(ActionEvent event) {

        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/report/SupplierReport.jrxml")
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
