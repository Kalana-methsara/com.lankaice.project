package com.lankaice.project.controller;

import com.lankaice.project.dto.OrderPaymentDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class BillPageController implements Initializable {

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label discountLabel;

    @FXML
    private Label itemsCountLabel;

    @FXML
    private Label subtotalLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private Label txtCustomerName;

    @FXML
    private Label txtInvoiceNo;

    @FXML
    private Label txtOrderId;

    @FXML
    private Label txtPaymentId;

    @FXML
    private Label txtPaymentMethod;


    @Getter
    @Setter
    private boolean isSave = false;


    @FXML
    void btnCancelOnAction(ActionEvent event) {
        setSave(false);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {
// print
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        setSave(true);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    void setlabel(OrderPaymentDto ordersPaymentDto, String customerId) {
        String paymentId = ordersPaymentDto.getPaymentId(); // PAY001
        String invoiceNo = "IN" + paymentId.substring(3);  // remove PAY, add IN
        txtInvoiceNo.setText(invoiceNo);                   // IN001

        txtOrderId.setText(String.valueOf(ordersPaymentDto.getOrderId()));
        txtCustomerName.setText(customerId);
        txtPaymentId.setText(paymentId);
        txtPaymentMethod.setText(ordersPaymentDto.getPaymentMethod());
        discountLabel.setText(String.valueOf(ordersPaymentDto.getDiscount()));
        itemsCountLabel.setText(String.valueOf(ordersPaymentDto.getItemCount()));
        subtotalLabel.setText(String.valueOf(ordersPaymentDto.getSubtotal()));
        totalLabel.setText(String.valueOf(ordersPaymentDto.getNetTotal()));
        datePicker.setValue(LocalDateTime.parse(ordersPaymentDto.getPaymentDate()).toLocalDate());


    }
}
