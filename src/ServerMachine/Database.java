package ServerMachine;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static final Logger logger = Logger.getLogger(Database.class.getName());  //logger
    static String url = "jdbc:sqlite:dcoms.db";

    public static void init() {
        // SQL statement for creating a new table
        String client = "CREATE TABLE IF NOT EXISTS ClientTable (\n"
                + "	userID integer PRIMARY KEY,\n"
                + "	userName varchar,\n"
                + "	password varchar\n"
                + ");";

        String supplier = "CREATE TABLE IF NOT EXISTS SupplierTable (\n"
                + "	userID integer PRIMARY KEY,\n"
                + "	userName varchar,\n"
                + "	password varchar\n"
                + ");";

        String item = "CREATE TABLE IF NOT EXISTS ItemTable (\n"
                + "	itemID integer PRIMARY KEY,\n"
                + "	itemQuantity integer,\n"
                + "	itemName varchar,\n"
                + "	supplierID integer,\n"
                + "	FOREIGN KEY(supplierID) REFERENCES SupplierTable(userID)\n"
                + ");";

        String order = "CREATE TABLE IF NOT EXISTS OrderTable (\n"
                + "	orderID integer PRIMARY KEY,\n"
                + "	address varchar,\n"
                + "	clientID varchar,\n"
                + "	FOREIGN KEY(clientID) REFERENCES ClientTable(userID)\n"
                + ");";

        String orderItem = "CREATE TABLE IF NOT EXISTS OrderItemTable (\n"
                + "	orderID integer,\n"
                + "	itemID integer,\n"
                + "	quantity integer,\n"
                + "	FOREIGN KEY(orderID) REFERENCES OrderTable(orderID),\n"
                + "	FOREIGN KEY(itemID) REFERENCES ItemTable(itemID)\n"
                + ");";

        try (Connection conn = connect()) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                //System.out.println("The driver name is " + meta.getDriverName());
                //System.out.println("A new database has been created.");

                Statement stmt = conn.createStatement();
                stmt.execute(client);
                stmt.execute(supplier);
                stmt.execute(item);
                stmt.execute(order);
                stmt.execute(orderItem);
            }
            logger.log(Level.INFO, "Database initialized successfully");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database initialization failed!", e);
            //System.out.println(e.getMessage());
        }
    }

    public static void insertClient(String name, String password) {
        String sql = "INSERT INTO ClientTable(userName,password) VALUES(?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertSupplier(String name, String password) {
        String sql = "INSERT INTO SupplierTable(userName,password) VALUES(?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void clientSelectAll(){
        String sql = "SELECT userID, userName, password FROM ClientTable";

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("userID") +  "\t" +
                        rs.getString("userName") + "\t" +
                        rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
