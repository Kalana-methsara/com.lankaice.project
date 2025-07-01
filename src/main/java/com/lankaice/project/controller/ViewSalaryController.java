package com.lankaice.project.controller;

import com.lankaice.project.dto.SalaryDto;
import com.lankaice.project.model.SalaryModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewSalaryController implements Initializable {

    @FXML
    private TableColumn colStatus;
    @FXML
    private Label txtTotalSalary;

    @FXML
    private ChoiceBox<String> textMonth;

    @FXML
    private TableColumn<SalaryDto, String> colSalId;

    @FXML
    private TableColumn<SalaryDto, String> colEmId;

    @FXML
    private TableColumn<SalaryDto, Double> colAmount;

    @FXML
    private TableColumn<SalaryDto, Void> colAction;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<SalaryDto> tableView;

    @FXML
    private TextField txtSearch;

    private final ObservableList<SalaryDto> obListSal = FXCollections.observableArrayList();
    private FilteredList<SalaryDto> combinedFilteredList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set month options
        textMonth.setItems(FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        ));
        int currentMonthIndex = java.time.LocalDate.now().getMonthValue() - 1;
        textMonth.getSelectionModel().select(currentMonthIndex);

        setupTableColumns();
        loadTableData();
        setupCombinedFilters();

        textMonth.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            applyCombinedFilters();
        });
    }

    private void setupTableColumns() {
        colSalId.setCellValueFactory(new PropertyValueFactory<>("salaryId"));
        colEmId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("netSalary"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colAction.setCellFactory(column -> new TableCell<>() {
            private final Button btnPaid = new Button("  PAID  ");
            private final Button btnCancel = new Button("CANCEL");

            {
                btnPaid.setStyle("-fx-background-color: white; -fx-text-fill: green; -fx-border-color: blue; -fx-font-weight: bold;");
                btnCancel.setStyle("-fx-background-color: white; -fx-text-fill: red; -fx-border-color: blue; -fx-font-weight: bold;");

                btnPaid.setOnAction(event -> {
                    SalaryDto salary = getTableView().getItems().get(getIndex());
                    if (!"COMPLETED".equals(salary.getStatus())) {
                        boolean updated = new SalaryModel().updateSalaryStatus(salary.getSalaryId(), "COMPLETED");
                        if (updated) {
                            salary.setStatus("COMPLETED");
                            showAlert(Alert.AlertType.INFORMATION,"Salary updated successfully");
                            tableView.refresh();
                        }
                    }
                });

                btnCancel.setOnAction(event -> {
                    SalaryDto salary = getTableView().getItems().get(getIndex());
                    if (!"CANCELLED".equals(salary.getStatus())) {
                        boolean updated = new SalaryModel().updateSalaryStatus(salary.getSalaryId(), "CANCELLED");
                        if (updated) {
                            salary.setStatus("CANCELLED");
                            showAlert(Alert.AlertType.INFORMATION,"Salary canceled successfully");
                            tableView.refresh();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, btnPaid, btnCancel);
                    buttons.setStyle("-fx-alignment: CENTER;");
                    setGraphic(buttons);
                }
            }
        });
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.initStyle(StageStyle.UNDECORATED);
        DialogPane pane = alert.getDialogPane();
        pane.setStyle(type == Alert.AlertType.INFORMATION
                ? "-fx-border-color: blue; -fx-border-width: 2px;"
                : "-fx-border-color: red; -fx-border-width: 2px;");
        alert.show();
    }

    private void loadTableData() {
        obListSal.clear();
        SalaryModel model = new SalaryModel();
        try {
            List<SalaryDto> dtoList = model.getAllSalaries();
            obListSal.addAll(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupCombinedFilters() {
        combinedFilteredList = new FilteredList<>(obListSal, b -> true);

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> applyCombinedFilters());

        SortedList<SalaryDto> sortedList = new SortedList<>(combinedFilteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        applyCombinedFilters();
    }

    private void applyCombinedFilters() {
        int selectedMonth = textMonth.getSelectionModel().getSelectedIndex() + 1;
        String searchText = txtSearch.getText() != null ? txtSearch.getText().toLowerCase() : "";

        combinedFilteredList.setPredicate(salary -> {
            boolean matchesMonth = salary.getPayMonth() == selectedMonth;
            boolean matchesSearch = searchText.isEmpty() ||
                    String.valueOf(salary.getSalaryId()).toLowerCase().contains(searchText) ||
                    salary.getEmployeeId().toLowerCase().contains(searchText) ||
                    String.valueOf(salary.getNetSalary()).toLowerCase().contains(searchText) ||
                    String.valueOf(salary.getPayMonth()).toLowerCase().contains(searchText);
            return matchesMonth && matchesSearch;
        });

        double total = combinedFilteredList.stream()
                .mapToDouble(SalaryDto::getNetSalary)
                .sum();
        txtTotalSalary.setText(String.format("%.2f", total));
    }

    public void searchSalDetails(ActionEvent actionEvent) {
        applyCombinedFilters();
    }

    public void onClose(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
