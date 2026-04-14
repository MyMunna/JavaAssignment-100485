package com.inventory;

import java.util.ArrayList;
import java.util.List;

// Generic class. Demonstrates Java generics requirement.
// Can hold any type T (e.g. Product, Item, String...).
public class Repository<T> {
    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(item);
    }

    public List<T> getAll() {
        return items;
    }

    public int size() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }
}
