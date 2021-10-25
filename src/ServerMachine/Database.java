package ServerMachine;

import java.sql.*;

public class Database {
    static String url = "jdbc:sqlite:dcoms.db";

    public static void main(String[] args) {
        Database database = new Database();
        //createNewDatabase(); Not needed
        //databaseConnectCheck(); Not needed
        //createNewTable();
        //database.insertClient("June","321");
        //database.clientSelectAll();
        database.supplierSelectAll();
    }

    public static void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void databaseConnectCheck() {
        Connection conn = null;
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public static void createNewTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS ClientTable (\n"
                + "	userID integer PRIMARY KEY,\n"
                + "	userName varchar,\n"
                + "	password varchar\n"
                + ");";

        String sql2 = "CREATE TABLE IF NOT EXISTS SupplierTable (\n"
                + "	userID integer PRIMARY KEY,\n"
                + "	userName varchar,\n"
                + "	password varchar\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            stmt.execute(sql2);
            System.out.println("Table Created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertClient(String name, String password) {
        String sql = "INSERT INTO ClientTable(userName,password) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertSupplier(String name, String password) {
        String sql = "INSERT INTO SupplierTable(userName,password) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void clientSelectAll(){
        String sql = "SELECT userID, userName, password FROM ClientTable";

        try (Connection conn = this.connect();
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

    public void supplierSelectAll(){
        String sql = "SELECT userID, userName, password FROM SupplierTable";

        try (Connection conn = this.connect();
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

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
