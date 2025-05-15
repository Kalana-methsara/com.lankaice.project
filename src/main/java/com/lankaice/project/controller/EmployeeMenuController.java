package com.lankaice.project.controller;

import com.lankaice.project.Validation.QRHttpServer;
<<<<<<< HEAD
import com.lankaice.project.dto.AttendanceDto;
import com.lankaice.project.model.AttendanceModel;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
=======
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
<<<<<<< HEAD
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
=======
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
<<<<<<< HEAD
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
=======
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
import java.util.ResourceBundle;


public class EmployeeMenuController implements Initializable {
<<<<<<< HEAD

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

=======
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
    @FXML
    private AnchorPane ancEmployeeMenu;

    @FXML
<<<<<<< HEAD
    private Button btnStartServer;
=======
    private Button btnEdit;
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805

    @FXML
    private Button btnMark;

    @FXML
    private Button btnStopServer;

    @FXML
    private Label serverResponseLabel;

    @FXML
    private Label serverStatusLabel;

<<<<<<< HEAD
    @FXML
    private Label attendanceToday;

    @FXML
    private ProgressBar progressBar;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        serverStatusLabel.setText("Please Start Server Process");
        serverStatusLabel.setStyle("-fx-text-fill: #009432FF;");
        progressBar.setVisible(false);
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


    public void onStartServer(ActionEvent actionEvent) throws IOException {
        btnStartServer.setDisable(true);
        btnStopServer.setDisable(false);
        progressBar.setVisible(true);
=======
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serverStatusLabel.setText("Please Start Server Process");
    }

    public void onMarkAttendance(ActionEvent actionEvent) throws IOException {
        btnMark.setDisable(true);
        btnStopServer.setDisable(false);
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
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
<<<<<<< HEAD
        loadTodayAttendance();
=======
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
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
<<<<<<< HEAD
        btnStartServer.setDisable(false);
        QRHttpServer.stopServer();
        updateServerStatusLabel();
        progressBar.setVisible(false);
=======
        btnMark.setDisable(false);
        QRHttpServer.stopServer();
        updateServerStatusLabel();
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
    }
    private void updateServerStatusLabel() {
        if (QRHttpServer.isServerRunning()) {
            serverStatusLabel.setText("🟢 Server is Running");
<<<<<<< HEAD
            serverStatusLabel.setStyle("-fx-text-fill: #009432FF;");
        } else {
            serverStatusLabel.setText("🟢 Server is Stopped");
            serverStatusLabel.setStyle("-fx-text-fill: #D63031FF;");
=======
        } else {
            serverStatusLabel.setText("🟢 Server is Stopped");
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        }
    }

    public void onEmployeeManage(ActionEvent actionEvent) {
        navigateTo("/view/EmployeePage.fxml");
    }
<<<<<<< HEAD

=======
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
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
