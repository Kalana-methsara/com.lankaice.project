package com.lankaice.project.dto;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BookingRow {
    private final StringProperty customerName = new SimpleStringProperty();
    private final StringProperty[] days = new StringProperty[31];

    public BookingRow(String name) {
        customerName.set(name);
        for (int i = 0; i < 31; i++) {
            days[i] = new SimpleStringProperty("");
        }
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public void setDay(int dayIndex, String value) {
        days[dayIndex].set(value);
    }

    public StringProperty dayProperty(int index) {
        return days[index];
    }

    public String getDay(int i) {
        return days[i].get();
    }
}
