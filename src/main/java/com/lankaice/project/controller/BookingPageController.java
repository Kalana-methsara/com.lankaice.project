package com.lankaice.project.controller;

import com.lankaice.project.dto.BookingDto;
import com.lankaice.project.dto.BookingRow;
import com.lankaice.project.model.BookingModel;
import com.lankaice.project.model.CustomerModel;
import com.lankaice.project.model.ProductModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

public class BookingPageController implements Initializable {

    @FXML
    private ChoiceBox<String> textProductStatus;
    @FXML
    private TextField textProductQty;
    @FXML
    private Label lblProductName;
    @FXML
    private ComboBox<String> textProductId;
    @FXML
    private TextField textCustomerId;
    @FXML
    private Label lblCustomerName;
    @FXML
    private DatePicker textDate;
    @FXML
    private Label textTime;
    @FXML
    private ChoiceBox<String> textHours, textMinutes;
    @FXML
    private Label lblMonth;
    @FXML
    private Button btnPrevMonth, btnNextMonth;
    @FXML
    private TableView<BookingRow> tableView1, tableView11;
    @FXML
    private TableColumn<BookingRow, String> Customer1, Customer2;

    @FXML
    private TableColumn<BookingRow, String> col1, col2, col3, col4, col5, col6, col7, col8, col9, col10,
            col11, col12, col13, col14, col15,
            col16, col17, col18, col19, col20, col21, col22, col23, col24, col25,
            col26, col27, col28, col29, col30, col31;

    private final CustomerModel customerModel = new CustomerModel();
    private final ProductModel productModel = new ProductModel();
    private final BookingModel bookingModel = new BookingModel();

    private final List<TableColumn<BookingRow, String>> firstTableColumns = new ArrayList<>();
    private final List<TableColumn<BookingRow, String>> secondTableColumns = new ArrayList<>();
    private final List<BookingRow> bookingsTable1 = new ArrayList<>();
    private final List<BookingRow> bookingsTable11 = new ArrayList<>();

    private YearMonth currentMonth = YearMonth.now();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize column lists
        firstTableColumns.addAll(List.of(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10,
                col11, col12, col13, col14, col15));
        secondTableColumns.addAll(List.of(col16, col17, col18, col19, col20, col21, col22, col23,
                col24, col25, col26, col27, col28, col29, col30, col31));

        tableView1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView1.getSelectionModel().setCellSelectionEnabled(true);

        tableView11.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView11.getSelectionModel().setCellSelectionEnabled(true);

        // Buttons
        btnPrevMonth.setOnAction(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateTableHeaders();
        });

        btnNextMonth.setOnAction(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateTableHeaders();
        });

        // Load status options
        textProductStatus.setItems(FXCollections.observableArrayList("Pending", "Confirmed", "Cancel"));
        // Load product IDs
        try {
            textProductId.setItems(FXCollections.observableArrayList(productModel.getAllProductIds()));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Failed to load product IDs");
        }

        // Load time choices
        for (int i = 0; i <= 24; i++) textHours.getItems().add(String.format("%02d", i));
        for (int i = 0; i <= 59; i++) textMinutes.getItems().add(String.format("%02d", i));
        textDate.setValue(LocalDate.now());

        initTableColumns();
        updateTableHeaders();
        clearFields();
    }

    private void initTableColumns() {
        Customer1.setCellValueFactory(data -> data.getValue().customerNameProperty());
        Customer2.setCellValueFactory(data -> data.getValue().customerNameProperty());

        for (int i = 0; i < 15; i++) {
            final int index = i;
            firstTableColumns.get(i).setCellValueFactory(data -> data.getValue().dayProperty(index));
        }
        for (int i = 15; i < 31; i++) {
            final int index = i;
            secondTableColumns.get(i - 15).setCellValueFactory(data -> data.getValue().dayProperty(index));
        }
    }

    private void updateTableHeaders() {
        lblMonth.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + currentMonth.getYear());

        int daysInMonth = currentMonth.lengthOfMonth();

        for (int day = 1; day <= 31; day++) {
            String header = "";
            if (day <= daysInMonth) {
                LocalDate date = currentMonth.atDay(day);
                DayOfWeek dow = date.getDayOfWeek();
                header = day + " (" + dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + ")";
            }

            if (day <= 15) {
                firstTableColumns.get(day - 1).setText(header);
            } else {
                secondTableColumns.get(day - 16).setText(header);
            }
        }

        loadBookingsForMonth();
    }

    private void loadBookingsForMonth() {
        tableView1.getItems().clear();
        tableView11.getItems().clear();
        bookingsTable1.clear();
        bookingsTable11.clear();

        try {
            ArrayList<BookingDto> bookings = bookingModel.getAllBookings();
            for (BookingDto booking : bookings) {
                LocalDate date = booking.getRequestDate().toLocalDate();
                if (YearMonth.from(date).equals(currentMonth)) {
                    String customerName = getCustomerName(booking.getCustomerId());
                    addBooking(booking.getCustomerId(), customerName, booking.getQuantity(), date);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to load bookings");
        }
    }

    private void addBooking(String customerId, String customerName, int quantity, LocalDate date) {
        int day = date.getDayOfMonth();
        int index = day - 1;

        List<BookingRow> table = (day <= 15) ? bookingsTable1 : bookingsTable11;
        TableView<BookingRow> view = (day <= 15) ? tableView1 : tableView11;

        BookingRow row = table.stream().filter(r -> r.getCustomerName().equals(customerName)).findFirst().orElse(null);
        if (row == null) {
            row = new BookingRow(customerName);
            table.add(row);
            view.getItems().add(row);
        }

        row.setDay(index, String.valueOf(quantity));
    }

    private String getCustomerName(String customerId) {
        try {
            return bookingModel.getCustomerNameById(customerId);
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public void btnAddOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String customerId = textCustomerId.getText().trim();
        String productId = textProductId.getValue().trim();
        String date = String.valueOf(textDate.getValue()).trim();
        String time = textTime.getText().trim();
        String qtyStr = textProductQty.getText().trim();
        String status = textProductStatus.getValue().trim();


        if (customerId.isEmpty() || productId == null || date == null || time.isEmpty() || qtyStr.isEmpty() || status == null) {
            showAlert(Alert.AlertType.WARNING, "Please fill all fields before updating.");
            return;
        }
        if (bookingModel.isDuplicate(customerId, date)) {
            showAlert(Alert.AlertType.WARNING, "Duplicate booking found for this customer on the given date.");
            return;
        }


        try {
            int quantity = Integer.parseInt(qtyStr);
            BookingDto updatedBooking = new BookingDto(customerId, productId, date.toString(), time, quantity, status);
boolean isAdd = bookingModel.addBooking(updatedBooking);
            if (isAdd) {
                showAlert(Alert.AlertType.INFORMATION, "Booking updated successfully.");
                clearFields();
                loadBookingsForMonth();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to save booking.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,"An error occurred while saved the booking.");
            e.printStackTrace();

        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String customerId = textCustomerId.getText().trim();
        LocalDate dateValue = textDate.getValue();

        if (customerId.isEmpty() || dateValue == null) {
            showAlert(Alert.AlertType.WARNING, "Please fill all fields before deleting.");
            return;
        }

        String date = dateValue.toString();

        try {
            boolean isDelete = bookingModel.deleteBooking(customerId, date);
            if (isDelete) {
                showAlert(Alert.AlertType.INFORMATION, "Booking deleted successfully.");
                clearFields();
                loadBookingsForMonth();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to delete booking.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "An error occurred while deleting the booking.");
            e.printStackTrace(); // Optional: for debugging purposes
        }
    }


    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    public void btnUpdateOnAction(ActionEvent event) {
        String customerId = textCustomerId.getText().trim();
        String productId = textProductId.getValue().trim();
        String date = String.valueOf(textDate.getValue()).trim();
        String time = textTime.getText().trim();
        String qtyStr = textProductQty.getText().trim();
        String status = textProductStatus.getValue().trim();


        if (customerId.isEmpty() || productId == null || date == null || time.isEmpty() || qtyStr.isEmpty() || status == null) {
            showAlert(Alert.AlertType.WARNING, "Please fill all fields before updating.");
            return;
        }

        try {
            int quantity = Integer.parseInt(qtyStr);
            BookingDto updatedBooking = new BookingDto(customerId, productId, date.toString(), time, quantity, status);
boolean isUpdated = bookingModel.updateBooking(updatedBooking);
            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Booking updated successfully.");
                clearFields();
                loadBookingsForMonth();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to update booking.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Invalid quantity or data.");
        }
    }

    private void clearFields() {
        textCustomerId.clear();
        textProductId.setValue(null);
        textDate.setValue(LocalDate.now());
        textTime.setText(null);
        textProductQty.clear();
        textProductStatus.setValue(null);
        lblCustomerName.setText("");
        textHours.setValue(null);
        textMinutes.setValue(null);
        lblProductName.setText(getCustomerName(textProductId.getValue()));

    }

    public void onCustomerId(KeyEvent keyEvent) {
        String id = textCustomerId.getText();
        try {
            String name = customerModel.findNameById(id);
            lblCustomerName.setText(name != null ? name : "Not Found");
        } catch (Exception e) {
            lblCustomerName.setText("Error");
        }
    }

    public void onProductId(ActionEvent event) {
        String id = textProductId.getValue();
        if (id == null) return;
        try {
            String name = productModel.findNameById(id);
            lblProductName.setText(name != null ? name : "Not Found");
        } catch (Exception e) {
            lblProductName.setText("Error");
        }
    }

    public void onKeyHours(KeyEvent keyEvent) {
        textMinutes.requestFocus();
    }

    public void onKeyMinutes(KeyEvent keyEvent) {
        String hours = textHours.getValue();
        String minutes = textMinutes.getValue();
        if (hours != null && minutes != null) {
            textTime.setText(hours + ":" + minutes);
        }
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

    public void setData(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException {
        clearFields();
        TableView<BookingRow> sourceTable = null;

        // Determine which table was clicked
        if (mouseEvent.getSource() == tableView1) {
            sourceTable = tableView1;
        } else if (mouseEvent.getSource() == tableView11) {
            sourceTable = tableView11;
        }

        if (sourceTable != null && !sourceTable.getSelectionModel().getSelectedCells().isEmpty()) {
            TablePosition<?, ?> pos = sourceTable.getSelectionModel().getSelectedCells().get(0);
            int rowIndex = pos.getRow();
            int colIndex = pos.getColumn();

            if (rowIndex < 0 || rowIndex >= sourceTable.getItems().size()) return;

            BookingRow selectedRow = sourceTable.getItems().get(rowIndex);
            if (selectedRow == null) return;

            // Set customer name
            String customerName = selectedRow.getCustomerName();
            lblCustomerName.setText(customerName != null ? customerName : "Unknown");

            // Get customer ID
            String customerId;
            try {
                customerId = customerModel.findIdByName(customerName);
            } catch (Exception e) {
                customerId = "Unknown";
            }
            textCustomerId.setText(customerId);

            // Only proceed if a day cell (not customer name column) was clicked
            if (colIndex > 0) {
                int dayOfMonth = (sourceTable == tableView1 ? colIndex : colIndex + 15);
                String quantity = selectedRow.getDay(dayOfMonth - 1);
                quantity = (quantity != null && !quantity.isEmpty()) ? quantity : "0";

                LocalDate selectedDate = currentMonth.atDay(dayOfMonth);
                textDate.setValue(selectedDate);
                textProductQty.setText(quantity);

                try {
                    // Load booking based on customer ID and date
                    BookingDto booking = bookingModel.getBookingByCustomerIdAndDate(customerId, selectedDate.toString(), quantity);

                    if (booking != null) {
                        textProductId.setValue(booking.getProductId());
                        textProductStatus.setValue(booking.getStatus());
                        textTime.setText(booking.getRequestTime().toString());

                        // Set product name label
                        String productName = productModel.findNameById(booking.getProductId());
                        lblProductName.setText(productName != null ? productName : "Unknown");
                    }

                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Failed to load booking details: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

}





