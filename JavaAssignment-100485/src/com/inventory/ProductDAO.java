package com.inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Data Access Object: runs the SQL against PostgreSQL using JDBC.
public class ProductDAO {

    // INSERT using PreparedStatement.
    public void saveProduct(Product p) throws SQLException {
        String sql = "INSERT INTO products (product_name, quantity, price) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getProductName());
            ps.setInt(2, p.getQuantity());
            ps.setDouble(3, p.getPrice());
            ps.executeUpdate();
        }
    }

    // SELECT all rows.
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT product_id, product_name, quantity, price FROM products ORDER BY product_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"));
                products.add(p);
            }
        }
        return products;
    }

    // UPDATE stock quantity for a given product id.
    public int updateStock(int productId, int newQuantity) throws SQLException {
        String sql = "UPDATE products SET quantity = ? WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, productId);
            return ps.executeUpdate();
        }
    }
}
