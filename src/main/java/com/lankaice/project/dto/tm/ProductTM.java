package com.lankaice.project.dto.tm;

import javafx.beans.property.*;

public class ProductTM {
    private final StringProperty product;
    private final DoubleProperty price;
    private final IntegerProperty qty;
    private final DoubleProperty discount;

    public ProductTM(String product, double price, int qty, double discount) {
        this.product = new SimpleStringProperty(product);
        this.price = new SimpleDoubleProperty(price);
        this.qty = new SimpleIntegerProperty(qty);
        this.discount = new SimpleDoubleProperty(discount);
    }

    public String getProduct() { return product.get(); }
    public double getPrice() { return price.get(); }
    public int getQty() { return qty.get(); }
    public double getDiscount() { return discount.get(); }

    public StringProperty productProperty() { return product; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty qtyProperty() { return qty; }
    public DoubleProperty discountProperty() { return discount; }
}
