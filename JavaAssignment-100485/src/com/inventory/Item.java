package com.inventory;

// Parent class. Shows inheritance when Product extends it.
public class Item {
    protected int id;
    protected String name;

    public Item() {
    }

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Will be overridden in Product (polymorphism).
    public String getDisplayInfo() {
        return "Item: " + name;
    }
}
