package ServerMachine;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ServerMachine.ServerRegistry.logfile;

public class Database {
    private static final Logger logger = Logger.getLogger(Database.class.getName());  //logger
    static {
        logger.addHandler(logfile);
    }
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
                + "	creationDate varchar,\n"
                + "	address varchar,\n"
                + "	clientID varchar,\n"
                + "	FOREIGN KEY(clientID) REFERENCES ClientTable(userID)\n"
                + ");";

        String orderItem = "CREATE TABLE IF NOT EXISTS OrderItemTable (\n"
                + "	orderID integer,\n"
                + "	itemID integer,\n"
                + "	quantity integer,\n"
                + "	confirm integer,\n"
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

    public static Client clientSearch(String userName, String password){
        String sql = "SELECT userID, userName, password FROM ClientTable WHERE userName ='" + userName + "' AND password ='" + password+"'";

        int id = 0;
        String name = null;
        String pswd = null;

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                    id = rs.getInt("userID");
                    name = rs.getString("userName");
                    pswd = rs.getString("password");

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return new Client(id, name, pswd);
    }

    public static Supplier supplierSearch(String userName, String password){
        String sql = "SELECT userID, userName, password FROM SupplierTable WHERE userName ='" + userName + "' AND password ='" + password+"'";

        int id = 0;
        String name = null;
        String pswd = null;

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                id = rs.getInt("userID");
                name = rs.getString("userName");
                pswd = rs.getString("password");

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return new Supplier(id, name, pswd);
    }

    public void supplierSelectAll(){
        String sql = "SELECT userID, userName, password FROM SupplierTable";

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

    public static HashMap<Integer, Item> getItems(int supplierID) {
        String sql = "SELECT * FROM ItemTable LEFT JOIN SupplierTable ON ItemTable.supplierID = SupplierTable.userID";
        HashMap<Integer, Item> items = new HashMap<>();

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                if(supplierID != -1 && rs.getInt("supplierID") != supplierID) {
                    continue;
                }
                Item item = new Item(rs.getInt("itemID"),
                        rs.getInt("itemQuantity"),
                        rs.getString("itemName"),
                        rs.getString("userName"));
                items.put(item.getItemID(), item);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return items;
    }

    public static HashMap<Integer, Item> getItems() {
        return getItems(-1);
    }

    public static void insertOrder(Client client, String address, HashMap<Item, Integer> cart) {
        String orderStmt = "INSERT INTO OrderTable(creationDate, address, clientID) VALUES(?,?,?)";
        String itemStmt = "INSERT INTO OrderItemTable(orderID, itemID, quantity, confirm) VALUES(?,?,?,?)";
        String qtyStmt = "UPDATE ItemTable SET itemQuantity = ? WHERE itemID = ?";

        try (Connection conn = connect();
             PreparedStatement orderPstmt = conn.prepareStatement(orderStmt);
             PreparedStatement itemPstmt = conn.prepareStatement(itemStmt);
             PreparedStatement qtyPstmt = conn.prepareStatement(qtyStmt)) {

            orderPstmt.setString(1, System.currentTimeMillis()+"");
            orderPstmt.setString(2, address);
            orderPstmt.setInt(3, client.getUserId());
            orderPstmt.executeUpdate();

            int orderID = orderPstmt.getGeneratedKeys().getInt(1);
            for (Map.Entry<Item, Integer> item : cart.entrySet()) {
                itemPstmt.setInt(1, orderID);
                itemPstmt.setInt(2, item.getKey().getItemID());
                itemPstmt.setInt(3, item.getValue());
                itemPstmt.setInt(4, 0);
                itemPstmt.executeUpdate();

                //update item quantity
                qtyPstmt.setInt(1, item.getKey().getItemQuantity() - item.getValue());
                qtyPstmt.setInt(2, item.getKey().getItemID());
                qtyPstmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static HashMap<Integer, Order> getOrders(String type, int userID) {
        String stmt = "SELECT oi.orderID, creationDate, it.itemID, oi.quantity, it.itemName, it.itemQuantity, ot.clientID, ct.userName AS clientName, ot.address, it.supplierID, st.userName AS supplierName, oi.confirm FROM OrderItemTable oi\n" +
                "LEFT JOIN OrderTable ot ON oi.orderID = ot.orderID\n" +
                "LEFT JOIN ItemTable it ON oi.itemID = it.itemID\n" +
                "LEFT JOIN SupplierTable st ON it.supplierID = st.userID\n" +
                "LEFT JOIN ClientTable ct ON ot.clientID = ct.userID\n";
        HashMap<Integer, Order> orderList = new HashMap<>();

        if(type.equals("client")) {
            stmt = stmt + "WHERE ot.clientID = ? ORDER BY oi.orderID ASC";
        } else if(type.equals("supplier")) {
            stmt = stmt + "WHERE it.supplierID = ? ORDER BY oi.orderID ASC";
        } else {
            return null;
        }

        try (Connection conn = connect();
             PreparedStatement pstmt  = conn.prepareStatement(stmt)){

            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();

            int currID = 0;
            Order order = null;
            HashMap<Item, Integer> itemList = null;
            while(rs.next()) {
                if(currID != rs.getInt("orderID")) {
                    if(itemList != null) {
                        order.setItemList(itemList);
                        orderList.put(order.getOrderID(), order);
                    }
                    currID = rs.getInt("orderID");
                    order = new Order(currID,
                            rs.getString("address"),
                            rs.getString("clientName"),
                            null,
                            new Date(Long.parseLong(rs.getString("creationDate"))));
                    itemList = new HashMap<>();
                }

                if(itemList != null) {
                    Item item = new Item(rs.getInt("itemID"),
                            rs.getInt("itemQuantity"),
                            rs.getString("itemName"),
                            rs.getString("supplierName"));
                    itemList.put(item, rs.getInt("quantity"));
                    if(rs.getInt("confirm") == 1) {
                        item.confirm();
                    }
                }

            }
            if(itemList != null) {
                order.setItemList(itemList);
                orderList.put(order.getOrderID(), order);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return orderList;
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
