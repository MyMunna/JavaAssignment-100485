package com.inventory;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

// Child of Item. Uses JavaFX properties so TableView can bind to it.
public class Product extends Item {

    // Encapsulation: private fields with getters/setters below.
    private final SimpleStringProperty productName = new SimpleStringProperty();
    private final SimpleIntegerProperty quantity = new SimpleIntegerProperty();
    private final SimpleDoubleProperty price = new SimpleDoubleProperty();

    public Product() {
    }

    public Product(int id, String name, int quantity, double price) {
        super(id, name);
        this.productName.set(name);
        this.quantity.set(quantity);
        this.price.set(price);
    }

    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String value) {
        this.productName.set(value);
        this.name = value;
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int value) {
        this.quantity.set(value);
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double value) {
        this.price.set(value);
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    // Polymorphism: overrides Item.getDisplayInfo().
    @Override
    public String getDisplayInfo() {
        return "Product: " + getProductName() + " | Qty: " + getQuantity() + " | Price: " + getPrice();
    }
}
