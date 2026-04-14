package com.inventory;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application {

    private static final PseudoClass INVALID = PseudoClass.getPseudoClass("invalid");
    private static final PseudoClass LOW_STOCK = PseudoClass.getPseudoClass("low-stock");
    private static final int LOW_STOCK_THRESHOLD = 10;

    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final AtomicInteger idSeq = new AtomicInteger(1);

    private TextField nameField;
    private TextField quantityField;
    private TextField priceField;
    private TextField searchField;
    private Label statusLabel;
    private Label totalValueLabel;
    private Label lowStockLabel;
    private Button addBtn;
    private Button updateBtn;
    private Button deleteBtn;
    private Button clearBtn;
    private TableView<Product> table;

    @Override
    public void start(Stage stage) {
        // Header
        Label title = new Label("Inventory Management System");
        title.getStyleClass().add("app-title");
        Label subtitle = new Label("Track products, stock levels and inventory value");
        subtitle.getStyleClass().add("app-subtitle");
        VBox header = new VBox(2, title, subtitle);

        // Form card
        VBox formCard = buildFormCard();

        // Table card
        VBox tableCard = buildTableCard();

        // Status bar
        statusLabel = new Label("Ready.");
        statusLabel.getStyleClass().addAll("status", "status-info");
        statusLabel.setMaxWidth(Double.MAX_VALUE);

        VBox root = new VBox(14, header, formCard, tableCard, statusLabel);
        root.setPadding(new Insets(20));
        VBox.setVgrow(tableCard, Priority.ALWAYS);

        Scene scene = new Scene(root, 960, 720);
        File css = new File("resources/style.css");
        if (css.exists()) {
            scene.getStylesheets().add(css.toURI().toString());
        }

        seedSamples();
        refreshSummary();

        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.setMinWidth(820);
        stage.setMinHeight(620);
        stage.show();
    }

    private VBox buildFormCard() {
        Label section = new Label("Product details");
        section.getStyleClass().add("section-title");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(8);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(40);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(30);
        ColumnConstraints c3 = new ColumnConstraints();
        c3.setPercentWidth(30);
        grid.getColumnConstraints().addAll(c1, c2, c3);

        Label nameLbl = fieldLabel("Product Name");
        Label qtyLbl = fieldLabel("Quantity");
        Label priceLbl = fieldLabel("Price");

        nameField = new TextField();
        nameField.setPromptText("e.g. Wireless Mouse");
        quantityField = new TextField();
        quantityField.setPromptText("positive integer");
        priceField = new TextField();
        priceField.setPromptText("positive number");

        grid.add(nameLbl, 0, 0);
        grid.add(qtyLbl, 1, 0);
        grid.add(priceLbl, 2, 0);
        grid.add(nameField, 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(priceField, 2, 1);

        // Buttons
        addBtn = styledBtn("Add Product", "btn-success");
        updateBtn = styledBtn("Update", "btn-warning");
        deleteBtn = styledBtn("Delete", "btn-danger");
        clearBtn = styledBtn("Clear", "btn-ghost");

        updateBtn.setDisable(true);
        deleteBtn.setDisable(true);

        addBtn.setOnAction(e -> handleAdd());
        updateBtn.setOnAction(e -> handleUpdate());
        deleteBtn.setOnAction(e -> handleDelete());
        clearBtn.setOnAction(e -> clearForm());

        HBox actions = new HBox(10, addBtn, updateBtn, deleteBtn, clearBtn);
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(12, section, grid, actions);
        card.getStyleClass().add("card");
        return card;
    }

    private VBox buildTableCard() {
        Label section = new Label("Inventory");
        section.getStyleClass().add("section-title");

        searchField = new TextField();
        searchField.setPromptText("Search by name...");
        searchField.setPrefWidth(260);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        totalValueLabel = new Label("Total value: $0.00");
        totalValueLabel.getStyleClass().add("summary-value");
        lowStockLabel = new Label("Low stock: 0");
        lowStockLabel.getStyleClass().add("summary-label");

        HBox toolbar = new HBox(12, searchField, spacer, lowStockLabel, totalValueLabel);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        table = new TableView<>();
        table.setPlaceholder(new Label("No products yet. Add one above."));
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Product, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getId()));
        idCol.setPrefWidth(70);

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> c.getValue().productNameProperty());
        nameCol.setPrefWidth(320);

        TableColumn<Product, Number> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(c -> c.getValue().quantityProperty());
        qtyCol.setPrefWidth(120);

        TableColumn<Product, Number> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(c -> c.getValue().priceProperty());
        priceCol.setPrefWidth(140);
        priceCol.setCellFactory(col -> new javafx.scene.control.TableCell<Product, Number>() {
            @Override
            protected void updateItem(Number v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : String.format("$%.2f", v.doubleValue()));
            }
        });

        table.getColumns().add(idCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(qtyCol);
        table.getColumns().add(priceCol);

        // Low-stock row highlight
        table.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldP, newP) -> {
                if (newP == null) {
                    row.pseudoClassStateChanged(LOW_STOCK, false);
                } else {
                    row.pseudoClassStateChanged(LOW_STOCK, newP.getQuantity() < LOW_STOCK_THRESHOLD);
                    newP.quantityProperty().addListener((o, a, b) ->
                            row.pseudoClassStateChanged(LOW_STOCK, b.intValue() < LOW_STOCK_THRESHOLD));
                }
            });
            return row;
        });

        // Filtering + sorting pipeline
        FilteredList<Product> filtered = new FilteredList<>(products, p -> true);
        searchField.textProperty().addListener((obs, o, n) -> {
            String q = n == null ? "" : n.trim().toLowerCase();
            filtered.setPredicate(p -> q.isEmpty() || p.getProductName().toLowerCase().contains(q));
        });
        SortedList<Product> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);

        // Selection auto-fills form and enables update/delete
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldP, p) -> {
            boolean has = p != null;
            updateBtn.setDisable(!has);
            deleteBtn.setDisable(!has);
            if (has) {
                nameField.setText(p.getProductName());
                nameField.setDisable(true);
                quantityField.setText(String.valueOf(p.getQuantity()));
                priceField.setText(String.format("%.2f", p.getPrice()));
                clearInvalid();
            }
        });

        products.addListener((javafx.collections.ListChangeListener<Product>) c -> refreshSummary());

        VBox card = new VBox(12, section, toolbar, table);
        card.getStyleClass().add("card");
        VBox.setVgrow(card, Priority.ALWAYS);
        return card;
    }

    private Label fieldLabel(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("field-label");
        return l;
    }

    private Button styledBtn(String text, String variant) {
        Button b = new Button(text);
        b.getStyleClass().addAll("btn", variant);
        return b;
    }

    // --- Handlers ---

    private void handleAdd() {
        clearInvalid();
        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        boolean ok = true;

        if (name.isEmpty()) {
            markInvalid(nameField);
            ok = false;
        } else if (findByName(name) != null) {
            markInvalid(nameField);
            showError("A product named \"" + name + "\" already exists.");
            return;
        }

        Integer qty = parsePositiveInt(quantityField.getText());
        if (qty == null) {
            markInvalid(quantityField);
            ok = false;
        }

        Double price = parsePositiveDouble(priceField.getText());
        if (price == null) {
            markInvalid(priceField);
            ok = false;
        }

        if (!ok) {
            showError("Please fix the highlighted fields.");
            return;
        }

        Product p = new Product(idSeq.getAndIncrement(), name, qty, price);
        products.add(p);
        showSuccess("Product added successfully.");
        clearForm();
    }

    private void handleUpdate() {
        Product selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        clearInvalid();

        Integer qty = parsePositiveInt(quantityField.getText());
        Double price = parsePositiveDouble(priceField.getText());
        boolean ok = true;
        if (qty == null) { markInvalid(quantityField); ok = false; }
        if (price == null) { markInvalid(priceField); ok = false; }
        if (!ok) {
            showError("Quantity and price must be valid positive numbers.");
            return;
        }

        selected.setQuantity(qty);
        selected.setPrice(price);
        table.refresh();
        refreshSummary();
        showSuccess("Stock updated.");
    }

    private void handleDelete() {
        Product selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete \"" + selected.getProductName() + "\"? This cannot be undone.",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Confirm deletion");
        confirm.setTitle("Delete product");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            products.remove(selected);
            clearForm();
            showSuccess("Product deleted.");
        }
    }

    private void clearForm() {
        nameField.clear();
        nameField.setDisable(false);
        quantityField.clear();
        priceField.clear();
        table.getSelectionModel().clearSelection();
        updateBtn.setDisable(true);
        deleteBtn.setDisable(true);
        clearInvalid();
    }

    // --- Helpers ---

    private Product findByName(String name) {
        for (Product p : products) {
            if (p.getProductName().equalsIgnoreCase(name)) return p;
        }
        return null;
    }

    private Integer parsePositiveInt(String s) {
        if (s == null) return null;
        try {
            int v = Integer.parseInt(s.trim());
            return v >= 0 ? v : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parsePositiveDouble(String s) {
        if (s == null) return null;
        try {
            double v = Double.parseDouble(s.trim());
            return v >= 0 ? v : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void markInvalid(TextField f) {
        f.pseudoClassStateChanged(INVALID, true);
    }

    private void clearInvalid() {
        nameField.pseudoClassStateChanged(INVALID, false);
        quantityField.pseudoClassStateChanged(INVALID, false);
        priceField.pseudoClassStateChanged(INVALID, false);
    }

    private void refreshSummary() {
        double total = 0;
        int low = 0;
        for (Product p : products) {
            total += p.getQuantity() * p.getPrice();
            if (p.getQuantity() < LOW_STOCK_THRESHOLD) low++;
        }
        totalValueLabel.setText(String.format("Total value: $%.2f", total));
        lowStockLabel.setText("Low stock: " + low);
    }

    private void showSuccess(String msg) {
        statusLabel.setText(msg);
        statusLabel.getStyleClass().removeAll("status-error", "status-info");
        if (!statusLabel.getStyleClass().contains("status-success")) {
            statusLabel.getStyleClass().add("status-success");
        }
    }

    private void showError(String msg) {
        statusLabel.setText(msg);
        statusLabel.getStyleClass().removeAll("status-success", "status-info");
        if (!statusLabel.getStyleClass().contains("status-error")) {
            statusLabel.getStyleClass().add("status-error");
        }
    }

    private void seedSamples() {
        products.add(new Product(idSeq.getAndIncrement(), "Wireless Mouse", 25, 19.99));
        products.add(new Product(idSeq.getAndIncrement(), "USB-C Cable", 8, 9.50));
        products.add(new Product(idSeq.getAndIncrement(), "Mechanical Keyboard", 12, 79.00));
        products.add(new Product(idSeq.getAndIncrement(), "Monitor Stand", 4, 34.90));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
