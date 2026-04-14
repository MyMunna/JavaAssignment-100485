SIMPLE INVENTORY MANAGEMENT SYSTEM REPORT

1. WHAT THE SYSTEM DOES
This is a Simple Inventory Management System. A shop can:
- Add products with a name, quantity, and price
- View all products in a table
- Update the stock of any product

If a product runs out of stock (quantity is zero), its name is shown in red so the user notices immediately.

2. WHY I CHOSE OPTION 2
I chose the inventory project because it uses:
- One simple table (products)
- One main entity (Product)
- A clear JavaFX + JDBC flow

This makes it easy to explain while still meeting all project requirements:
- Object-Oriented Programming (OOP)
- Generics
- Exception handling
- JavaFX GUI
- PostgreSQL with JDBC

3. HOW JAVAFX IS USED
- Main.java extends Application and overrides start(Stage)
- Layouts:
  - GridPane for the form
  - HBox for the buttons
  - VBox as the root container
- Controls:
  - Label
  - TextField
  - Button
  - TableView<Product>
- A custom TableCell paints the product name red when quantity == 0
- messageLabel shows errors in red and success messages in green

4. HOW JDBC IS USED
- DBConnection.getConnection() calls DriverManager.getConnection(url, user, password)

- ProductDAO uses PreparedStatement with ? placeholders:
  - saveProduct → executeUpdate() (INSERT)
  - getAllProducts → executeQuery() + ResultSet (SELECT)
  - updateStock → executeUpdate() (UPDATE)

- Resources are handled using try-with-resources for automatic closing

5. WHERE OOP PRINCIPLES APPEAR
- Encapsulation – Product uses private fields with getters and setters
- Inheritance – Product extends Item
- Polymorphism – Product overrides Item.getDisplayInfo()
- Abstraction – ProductDAO hides SQL details behind methods

6. WHERE GENERICS ARE USED
- Repository<T> is a generic class
- Used as Repository<Product> in Main.java
- Can also store other types like String or Item

7. WHERE EXCEPTION HANDLING IS USED
- try/catch handles NumberFormatException for invalid input
- DAO methods throw SQLException, handled in Main
- Try-with-resources safely handles JDBC resource closing

8. DEMO SCRIPT
1. Enter Sugar, 10, 120 → Click Save Product
2. Click Load Products → Sugar appears
3. Add Rice, 0, 200 → Save Product
4. Load Products → Rice appears in red
5. Update Sugar quantity to 0 → it turns red
6. Try invalid input → red validation message appears

9. CLOSING STATEMENT
All requirements are implemented:
- JavaFX GUI
- PostgreSQL with JDBC
- Inheritance (Item → Product)
- Encapsulation
- Polymorphism
- Generic Repository<T>
- Input validation
- Exception handling
- Red highlight for out-of-stock products
