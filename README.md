SIMPLE INVENTORY MANAGEMENT SYSTEM

STUDENT DETAILS
Name: Abigael Amina  
Student Number: 100485  
Course: BBT 2202 – Advanced Object Oriented Programming  

PROJECT DESCRIPTION  
JavaFX + PostgreSQL + JDBC application that allows a user to:
- Add products (name, quantity, price)  
- Load and view all products in a TableView  
- Update stock quantity for an existing product  
- See a product name shown in red when its quantity is 0

Additionally
- CSS styling to the JavaFX scene.
- delete product functionality.
- search by product name.
- stronger validation, such as preventing negative values.

TECHNOLOGIES  
- Java (plain, no Maven / no Gradle)  
- JavaFX 21  
- PostgreSQL  
- JDBC (DriverManager, PreparedStatement, ResultSet, try-with-resources)  

PROJECT STRUCTURE  
src/com/inventory/  
    Item.java         - parent class (inheritance)  
    Product.java      - child class (extends Item, encapsulation, polymorphism)  
    Repository.java   - generic class Repository<T> (generics)  
    DBConnection.java - JDBC connection to PostgreSQL  
    ProductDAO.java   - insert / select / update using PreparedStatement  
    Main.java         - JavaFX GUI entry point  

sql/schema.sql        - database and table setup  
libs/                 - JavaFX and PostgreSQL JDBC jars  

CLASS EXPLANATIONS 
Item.java – Parent class with id and name. Defines getDisplayInfo() that child classes can override.  

Product.java – Extends Item. Adds quantity and price using JavaFX properties so the TableView can display them. Overrides getDisplayInfo() to demonstrate polymorphism.  

Repository.java – Repository<T> is a generic container. The GUI uses Repository<Product>, but the same class can store any type.  

DBConnection.java – Returns a JDBC Connection to inventory_db on localhost:5432.  

ProductDAO.java – Contains all SQL operations: insert a product, read all products, and update stock quantity for a given id. Uses PreparedStatement and try-with-resources.  

Main.java – JavaFX application with a form (GridPane), buttons (HBox), a message label, and a TableView<Product>. Displays product names in red when quantity is 0.  

PREREQUISITES  
- Java 17 or higher (tested on Java 25)  
- PostgreSQL running on localhost:5432  
- JavaFX 21 jars and PostgreSQL JDBC jar (included in libs/)  

DATABASE SETUP  
1. Start PostgreSQL  
2. Create the database and table using:  
   psql -U postgres -f sql/schema.sql  

   Or open psql and paste the contents of sql/schema.sql  

3. Default credentials in DBConnection.java:  
   URL: jdbc:postgresql://localhost:5432/inventory_db  
   User: postgres  
   Password: postgres  

   Edit DBConnection.java if your credentials are different  

BUILD AND RUN FROM TERMINAL  

Compile:  
mkdir -p out  
javac --module-path libs --add-modules javafx.controls -cp libs/postgresql-42.7.3.jar -d out src/com/inventory/*.java  

Run:  
java --module-path libs --add-modules javafx.controls -cp "out:libs/postgresql-42.7.3.jar" com.inventory.Main  

A helper script run.sh can perform both steps  


VALIDATION RULES  
- Product name cannot be empty  
- Quantity must be an integer greater than or equal to 0  
- Price must be a number greater than or equal to 0  
- Errors appear in red, success messages in green  

BUSINESS / DISPLAY RULE  
- If a product has quantity equal to 0, its name is displayed in red in the TableView  

DOCUMENTATION  
- Presentation guide: docs/PRESENTATION.md  
- SQL file: sql/schema.sql  
