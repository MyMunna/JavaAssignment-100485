# Simple Inventory Management System

## Student Details
- Name: Abigael Amina
- Student Number: 100485
- Course: BBT 2202 – Advanced Object Oriented Programming

## Project Description
A beginner-friendly JavaFX + PostgreSQL + JDBC application that lets a user:
- Add products (name, quantity, price)
- Load and view all products in a TableView
- Update stock quantity for an existing product
- See a product name shown in **red** when its quantity is `0`


## Technologies
- Java (plain, no Maven / no Gradle)
- JavaFX 21
- PostgreSQL
- JDBC (`DriverManager`, `PreparedStatement`, `ResultSet`, try-with-resources)

## Project Structure
```
src/com/inventory/
    Item.java         - parent class (inheritance)
    Product.java      - child class (extends Item, encapsulation, polymorphism)
    Repository.java   - generic class Repository<T> (generics)
    DBConnection.java - JDBC connection to PostgreSQL
    ProductDAO.java   - insert / select / update using PreparedStatement
    Main.java         - JavaFX GUI entry point
sql/schema.sql        - database and table setup
libs/                 - JavaFX and PostgreSQL JDBC jars
```

## Class Explanations (beginner)
- **Item.java** – parent class with `id` and `name`. Defines
  `getDisplayInfo()` that child classes can override.
- **Product.java** – extends `Item`. Adds `quantity` and `price` using
  JavaFX properties so the `TableView` can display them. Overrides
  `getDisplayInfo()` to show polymorphism.
- **Repository.java** – `Repository<T>` is a generic container. The
  GUI uses `Repository<Product>` but the same class could store any type.
- **DBConnection.java** – returns a JDBC `Connection` to
  `inventory_db` on `localhost:5432`.
- **ProductDAO.java** – all SQL: insert a product, read all products,
  update the stock quantity for a given id. Uses `PreparedStatement`
  and try-with-resources.
- **Main.java** – JavaFX app with a form (`GridPane`), buttons (`HBox`),
  a message label, and a `TableView<Product>`. Shows product name in
  red when quantity is 0.

## Prerequisites
- Java 17+ (project tested on Java 25)
- PostgreSQL running on `localhost:5432` (Postgres.app is fine)
- JavaFX 21 jars and PostgreSQL JDBC jar (already included in `libs/`)

## Database Setup
1. Start PostgreSQL.
2. Create the database and table:
   ```bash
   psql -U postgres -f sql/schema.sql
   ```
   Or open `psql` and paste the contents of `sql/schema.sql`.
3. Default credentials used in `DBConnection.java`:
   - URL: `jdbc:postgresql://localhost:5432/inventory_db`
   - User: `postgres`
   - Password: `postgres`

   Edit `DBConnection.java` if your PostgreSQL uses different credentials.

## Build and Run from Terminal
```bash
# compile
mkdir -p out
javac --module-path libs --add-modules javafx.controls \
      -cp libs/postgresql-42.7.3.jar \
      -d out src/com/inventory/*.java

# run
java  --module-path libs --add-modules javafx.controls \
      -cp "out:libs/postgresql-42.7.3.jar" \
      com.inventory.Main
```
A helper script `run.sh` does both steps.

## Run in IntelliJ IDEA
1. `File` → `Open` and choose this folder.
2. `File` → `Project Structure` → `Project` → set SDK to Java 17+.
3. `Project Structure` → `Libraries` → `+` → `Java` → select all jars
   inside the `libs/` folder.
4. `Project Structure` → `Modules` → mark `src` as **Sources Root**.
5. Open `Run` → `Edit Configurations` → `+` → `Application`:
   - Main class: `com.inventory.Main`
   - VM options:
     ```
     --module-path libs --add-modules javafx.controls
     ```
6. Click `Run`.

## Validation Rules
- Product name cannot be empty
- Quantity must be an integer (≥ 0)
- Price must be a number (≥ 0)
- Errors appear in **red**, success messages in **green**

## Business/Display Rule
- If a product has `quantity == 0`, its name is shown in **red** in the
  `TableView`.

## Documentation
- Presentation guide: `docs/PRESENTATION.md`
- SQL: `sql/schema.sql`
