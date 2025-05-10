package com.lankaice.project.controller;

import com.lankaice.project.dto.UserDto;
import com.lankaice.project.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginUserDetailsController implements Initializable {

    @FXML
    private Label idEmail;

    @FXML
    private Label idHI;

    @FXML
    private Label idName;

    @FXML
    private Label idRole;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserDto user = Session.getCurrentUser();

        idHI.setText("Hi " + user.getUsername());
        idName.setText(user.getName());
        idEmail.setText(user.getEmail());
        idRole.setText(user.getRole());

    }

    public void onCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
