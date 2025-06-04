package com.lankaice.project.controller;

import com.lankaice.project.Validation.AdbReverseHelper;
import com.lankaice.project.Validation.QRHttpServer;
import com.lankaice.project.dto.AttendanceDto;
import com.lankaice.project.model.AttendanceModel;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
    private Button btnStartServer;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnStopServer;

    @FXML
    private Label serverResponseLabel;

    @FXML
    private Label serverStatusLabel;

    @FXML
    private Label attendanceToday;

    @FXML
    private Label lblTotalEmployee;

    @FXML
    private Label lblOnTimeToday;

    @FXML
    private Label lblLateToday;

    @FXML
    private Label lblAbsent;

    @FXML
    private ProgressBar progressBar;

    private final AttendanceModel attendanceModel = new AttendanceModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serverResponseLabel.setVisible(false);
        btnStopServer.setDisable(true);
        loadTodayAttendance();
        updateServerStatusLabel();
        autoMarkAttendance();
        autoMarkAttendance1();
        try {
            loadAttendanceStats();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void autoMarkAttendance1() {
        LocalTime currentTime = LocalTime.now();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(11, 55);

        if (currentTime.isBefore(startTime) || currentTime.isAfter(endTime)) {
            return;
        }

        // Get yesterday's date
        LocalDate yesterday = LocalDate.now().minusDays(1);

        try {
            var attendanceList = attendanceModel.getAttendanceByDate(yesterday.toString());

            for (AttendanceDto dto : attendanceList) {
                if ("Night".equals(dto.getShift()) && dto.getOutTime() == null) {
                    LocalTime defaultOutTime = LocalTime.of(7, 59); // 7:59 AM as default
                    attendanceModel.updateOutTime(dto.getEmployeeId(), yesterday.toString(), "Night", defaultOutTime);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void autoMarkAttendance() {
        LocalTime currentTime = LocalTime.now();
        LocalTime startTime = LocalTime.of(18, 0);
        LocalTime endTime = LocalTime.of(23, 55); // 5-minute grace

        if (currentTime.isBefore(startTime) || currentTime.isAfter(endTime)) {
            return;
        }

        LocalDate today = LocalDate.now();

        try {
            var attendanceList = attendanceModel.getAttendanceByDate(today.toString());

            for (AttendanceDto dto : attendanceList) {
                if ("Morning".equals(dto.getShift()) && dto.getOutTime() == null) {

                    LocalTime defaultOutTime = LocalTime.of(17, 00);
                    attendanceModel.updateOutTime(dto.getEmployeeId(), today.toString(), "Morning", defaultOutTime);

                    AttendanceDto nightShiftAttendance = new AttendanceDto();
                    nightShiftAttendance.setEmployeeId(dto.getEmployeeId());
                    nightShiftAttendance.setName(dto.getName());
                    nightShiftAttendance.setDate(today);
                    nightShiftAttendance.setShift("Night");
                    nightShiftAttendance.setStatus("Present");
                    nightShiftAttendance.setInTime(LocalTime.of(17, 00));
                    nightShiftAttendance.setOutTime(null);

                    attendanceModel.markAttendance(nightShiftAttendance);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadTodayAttendance() {
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("inTime"));
        colStartTime1.setCellValueFactory(new PropertyValueFactory<>("outTime"));

        try {
            ArrayList<AttendanceDto> attendanceDtos = attendanceModel.getAttendanceByDate(String.valueOf(LocalDate.now()));

            if (attendanceDtos != null && !attendanceDtos.isEmpty()) {
                // ✅ Sort by inTime
                attendanceDtos.sort((a, b) -> {
                    if (a.getInTime() == null) return 1;
                    if (b.getInTime() == null) return -1;
                    return a.getInTime().compareTo(b.getInTime());
                });

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

    @FXML
    public void onStartServer(ActionEvent actionEvent) throws IOException {
        btnStartServer.setDisable(true);
        btnStopServer.setDisable(false);
        QRHttpServer.setResponseListener(response -> Platform.runLater(() -> {
            try {
                showServerResponse(response);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }));
        QRHttpServer.startServer();
        updateServerStatusLabel();
    }

    private void showServerResponse(String response) throws SQLException, ClassNotFoundException {
        System.out.println("Server response received: " + response);
        serverResponseLabel.setVisible(true);
        serverResponseLabel.setText(response);

        loadTodayAttendance();
loadAttendanceStats();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> serverResponseLabel.setText(""))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    @FXML
    void onEditAttendance(ActionEvent event) {
        navigateTo("/view/AttendancePage.fxml");
    }

    @FXML
    void onStopServer(ActionEvent event) {
        btnStopServer.setDisable(true);
        btnStartServer.setDisable(false);
        QRHttpServer.stopServer();
        updateServerStatusLabel();
    }

    private void updateServerStatusLabel() {
        Platform.runLater(() -> {
            boolean isRunning = QRHttpServer.isServerRunning();
            if (isRunning) {
                progressBar.setVisible(true);
                serverStatusLabel.setText("🟢 Server is Running");
                serverStatusLabel.setStyle("-fx-text-fill: #009432FF;");
                AdbReverseHelper.setupAdbReverse();
            } else {
                progressBar.setVisible(false);
                serverStatusLabel.setText("🟢 Server is Stopped");
                serverStatusLabel.setStyle("-fx-text-fill: #D63031FF;");
            }
            System.out.println("Server status updated: " + (isRunning ? "Running" : "Stopped"));
        });
    }

    @FXML
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

    private void loadAttendanceStats() throws SQLException, ClassNotFoundException {
        lblTotalEmployee.setText("15");

        LocalDate today = LocalDate.now();
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime midTime = LocalTime.of(8, 20);
        LocalTime endTime = LocalTime.of(17, 0);

        int lateCount = 0;
        int onTimeCount = 0;

        var attendanceList = attendanceModel.getAttendanceByDate(today.toString());

        for (AttendanceDto dto : attendanceList) {
            if ("Morning".equals(dto.getShift()) && dto.getInTime() != null) {
                LocalTime inTime = dto.getInTime();

                if (!inTime.isBefore(startTime) && !inTime.isAfter(midTime)) {
                    // On time: between 08:00 and 08:20 inclusive
                    onTimeCount++;
                } else if (inTime.isAfter(midTime) && !inTime.isAfter(endTime)) {
                    // Late: after 08:20 but before or at 17:00
                    lateCount++;
                }
            }
        }
        lblOnTimeToday.setText(String.valueOf(onTimeCount));
        lblLateToday.setText(String.valueOf(lateCount));
        lblAbsent.setText(String.valueOf(15 - (onTimeCount + lateCount)));
    }

}

