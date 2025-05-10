package com.lankaice.project.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddAdminController implements Initializable {
    @FXML
    private ChoiceBox<String> txtRole;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    public void btnSaveOnAction(ActionEvent actionEvent) {

    }

    public void close(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> roles = FXCollections.observableArrayList("Admin", "Supervisor", "Manager","Cashier");
        txtRole.setItems(roles);
        txtRole.setValue("");

    }
    public void fillFormFields(String username, String password, String name, String email, String role) {
        txtUsername.setText(username);
        txtPassword.setText(password);
        txtName.setText(name);
        txtEmail.setText(email);
        txtRole.setValue(role);
    }

}
