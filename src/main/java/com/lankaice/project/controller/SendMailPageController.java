package com.lankaice.project.controller;

import com.lankaice.project.util.SendMail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.MessagingException;

public class SendMailPageController {

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtSubject;

    @FXML
    private TextField txtTo;

    private String mail;

    public void setMail(String mail) {
        this.mail = mail;
        if (txtTo != null && mail != null) {
            txtTo.setText(mail);
        }
    }

    @FXML
    public void initialize() {
        if (mail != null) {
            txtTo.setText(mail);
        }
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        String to = txtTo.getText().trim();
        String subject = txtSubject.getText().trim();
        String description = txtDescription.getText().trim();

        if (to.isEmpty() || subject.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields are required!");
            return;
        }

        if (!isValidEmail(to)) {
            showAlert(Alert.AlertType.ERROR, "Invalid email address format!");
            return;
        }

        try {
            SendMail.outMail(description, to, subject);
            showAlert(Alert.AlertType.INFORMATION, "Email sent successfully to " + to);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (MessagingException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to send email: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    @FXML
    public void btnBackOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
