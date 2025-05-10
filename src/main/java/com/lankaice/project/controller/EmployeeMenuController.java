package com.lankaice.project.controller;

import com.lankaice.project.Validation.QRHttpServer;
import com.lankaice.project.dto.AttendanceDto;
import com.lankaice.project.model.AttendanceModel;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class EmployeeMenuController implements Initializable {

    @FXML
    private TableView<AttendanceDto> tableTodayAttendance;

    @FXML
    private TableColumn<AttendanceDto, String> colEmpId;

    @FXML
    private TableColumn<AttendanceDto, String> colName;

    @FXML
    private TableColumn<AttendanceDto, String> colStartTime;

    @FXML
    private TableColumn<AttendanceDto, String> colStartTime1;

    @FXML
    private AnchorPane ancEmployeeMenu;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnMark;

    @FXML
    private Button btnStopServer;

    @FXML
    private Label serverResponseLabel;

    @FXML
    private Label serverStatusLabel;

    @FXML
    private Label attendanceToday;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        serverStatusLabel.setText("Please Start Server Process");
        loadTodayAttendance();
    }

    private void loadTodayAttendance() {
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("inTime"));
        colStartTime1.setCellValueFactory(new PropertyValueFactory<>("outTime"));

        try {
            AttendanceModel attendanceModel = new AttendanceModel();
            ArrayList<AttendanceDto> attendanceDtos = attendanceModel.getAttendanceByDate(String.valueOf(LocalDate.now()));
            if (attendanceDtos != null && !attendanceDtos.isEmpty()) {
                attendanceToday.setText("Completed");
                ObservableList<AttendanceDto> observableList = FXCollections.observableList(attendanceDtos);
                tableTodayAttendance.setItems(observableList);
            } else {
                attendanceToday.setText("No attendance records for today");
            }

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> attendanceToday.setText(""));
            pause.play();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            attendanceToday.setText("Error loading attendance.");
        }
    }


    public void onMarkAttendance(ActionEvent actionEvent) throws IOException {
        btnMark.setDisable(true);
        btnStopServer.setDisable(false);
        QRHttpServer.setResponseListener(response -> {
            javafx.application.Platform.runLater(() -> {
                showServerResponse(response);
            });
        });

        QRHttpServer.startServer();
        updateServerStatusLabel();
    }

    private void showServerResponse(String response) {
        serverResponseLabel.setVisible(true);
        serverResponseLabel.setText(response);
        loadTodayAttendance();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> {
                    serverResponseLabel.setText("");
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    @FXML
    void onEditAttendance(ActionEvent event) {

    }

    @FXML
    void onStopServer(ActionEvent event) throws IOException {
        btnStopServer.setDisable(true);
        btnMark.setDisable(false);
        QRHttpServer.stopServer();
        updateServerStatusLabel();
    }
    private void updateServerStatusLabel() {
        if (QRHttpServer.isServerRunning()) {
            serverStatusLabel.setText("🟢 Server is Running");
        } else {
            serverStatusLabel.setText("🟢 Server is Stopped");
        }
    }

    public void onEmployeeManage(ActionEvent actionEvent) {
        navigateTo("/view/EmployeePage.fxml");
    }

    public void navigateTo(String path) {
        try {
            ancEmployeeMenu.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(ancEmployeeMenu.widthProperty());
            anchorPane.prefHeightProperty().bind(ancEmployeeMenu.heightProperty());
            ancEmployeeMenu.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found", ButtonType.OK).show();
            e.printStackTrace();
        }
    }
}
