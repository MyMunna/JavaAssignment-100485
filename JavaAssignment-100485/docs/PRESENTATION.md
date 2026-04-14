# Presentation Guide (3–5 minutes)

## 1. What the system does
"This is a Simple Inventory Management System. A shop can add products
with a name, quantity, and price, view all products in a table, and
update the stock of any product. If a product runs out of stock (quantity
is zero), its name is shown in red so the user notices immediately."

## 2. Why I chose Option 2
"I chose the inventory project because it uses one simple table
(`products`), one main entity (`Product`), and a clear JavaFX + JDBC
flow. That makes it easy to explain while still meeting every project
requirement: OOP, generics, exception handling, JavaFX GUI, and
PostgreSQL with JDBC."

## 3. How JavaFX is used
- `Main.java` extends `Application` and overrides `start(Stage)`.
- Layouts: `GridPane` for the form, `HBox` for the buttons, `VBox` as
  the root container.
- Controls: `Label`, `TextField`, `Button`, `TableView<Product>`.
- A custom `TableCell` paints the product name **red** when
  `quantity == 0`.
- The `messageLabel` shows validation errors in red and success
  messages in green.

## 4. How JDBC is used
- `DBConnection.getConnection()` calls
  `DriverManager.getConnection(url, user, password)`.
- `ProductDAO` uses `PreparedStatement` with `?` placeholders to avoid
  SQL injection:
  - `saveProduct` → `executeUpdate()` for INSERT
  - `getAllProducts` → `executeQuery()` + `ResultSet` for SELECT
  - `updateStock` → `executeUpdate()` for UPDATE
- Connections, statements, and result sets are closed automatically
  with **try-with-resources**.

## 5. Where OOP principles appear
- **Encapsulation** – `Product` uses private fields with getters and
  setters.
- **Inheritance** – `Product extends Item`.
- **Polymorphism** – `Product` overrides `Item.getDisplayInfo()`.
- **Abstraction** – `ProductDAO` hides all SQL details behind clean
  methods like `saveProduct`, `getAllProducts`, `updateStock`.

## 6. Where generics are used
- `Repository<T>` is a generic class that can store any type. In
  `Main.java` I use it as `Repository<Product>` to keep an in-memory
  copy of the loaded products. The same class could just as easily hold
  `Repository<String>` or `Repository<Item>`.

## 7. Where exception handling is used
- Input parsing is wrapped in `try/catch` for `NumberFormatException`
  so invalid numbers show a friendly red message.
- All DAO methods throw `SQLException`; `Main` catches these and
  displays "Database error: ..." in the message label.
- JDBC resources use try-with-resources, which handles close-time
  exceptions safely.

## 8. Demo script (what to click)
1. Type `Sugar`, `10`, `120`, click **Save Product** → green success.
2. Click **Load Products** → the table fills; `Sugar` appears.
3. Add another product: `Rice`, `0`, `200`, **Save Product**.
4. Click **Load Products** → `Rice` appears with its name in **red**
   because quantity is 0.
5. Enter the `Sugar` Product ID, new quantity `0`, click **Update
   Stock** → the table reloads and `Sugar` is now also red.
6. Try an empty name or letters in the quantity field → red validation
   message.

## 9. Closing line
"Everything the brief asks for is here: JavaFX GUI, PostgreSQL with
JDBC, inheritance between `Item` and `Product`, encapsulation,
polymorphism, a generic `Repository<T>`, input validation, exception
handling, and the red-when-zero rule."
