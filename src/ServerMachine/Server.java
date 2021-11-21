package ServerMachine;

import ClientMachine.ClientInterface;
import SupplierMachine.SupplierInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ServerMachine.ServerRegistry.logfile;

public class Server extends UnicastRemoteObject implements ClientInterface, SupplierInterface {
    private static final Logger logger = Logger.getLogger(Server.class.getName());  //logger
    static {
        logger.addHandler(logfile);
    }

    public Server()throws RemoteException{
        super();
    }

    //Client methods:

    public Client clientLogin(String userName, String password) {
        Client client = Database.clientSearch(userName,password);
        try {
            if(client.getUserName() != null && client.getPassword() != null){
                logger.log(Level.INFO, "Client " + getClientHost() + " logged in as user " + userName);
            }
        } catch(Exception e) {
            e.getStackTrace();
        }

        return client;
    }

    public void clientInsert(String userName, String password){
        try {
            logger.log(Level.INFO, "Client " + getClientHost() + " registered a new user " + userName);
        } catch(Exception e) {
            e.getStackTrace();
        }

        Database.insertClient(userName,password);
    }

    public Client checkClientDupe(String userName){
        try {
            logger.log(Level.INFO, "Client " + getClientHost() + " checking for duplicated Username " + userName);
        } catch(Exception e) {
            e.getStackTrace();
        }

        return Database.clientCheckDupe(userName);
    }

    public HashMap<Integer, Item> cViewItem() {
        HashMap<Integer, Item> items = Database.getItems();
        try {
            logger.log(Level.INFO, "Client " + getClientHost() + " requested a list of all items (" + items.size() + ")");
        } catch(Exception e) {
            e.getStackTrace();
        }

        return items;
    }

    public void purchaseItem(Client client, String address, HashMap<Item, Integer> cart) {
        int sum = 0;
        for(int i: cart.values()) {
            sum += i;
        }
        try {
            logger.log(Level.INFO, "Client " + getClientHost() + " (" + client.getUserName() + ") purchased " + sum + " items");
        } catch(Exception e) {
            e.getStackTrace();
        }

        Database.insertOrder(client, address, cart);
    }

    public HashMap<Integer, Order> cViewOrders(int userID) {
        HashMap<Integer, Order> orders = Database.getOrders("client", userID);
        try {
            if(orders != null){
                logger.log(Level.INFO, "Client " + getClientHost() + " requested a list of all orders (" + orders.size() + ") made by userID " + userID);
            }

        } catch(Exception e) {
            e.getStackTrace();
        }

        return orders;
    }


    //Supplier methods:

    public Supplier supplierLogin(String supplierName, String password){
        Supplier supplier = Database.supplierSearch(supplierName,password);
        try {
            if(supplier.getuserName() != null && supplier.getPassword() != null){
                logger.log(Level.INFO, "Supplier " + getClientHost() + " logged in as user " + supplierName);
            }
        } catch(Exception e) {
            e.getStackTrace();
        }

        return supplier;
        //You need to return the user object saved, so when James login it returns James' account and not someone else, this is an example.
        //After creating a method, create an empty version of the method at ServerInterface.
    }

    public void supplierInsert(String userName, String password){
        try {
            logger.log(Level.INFO, "Supplier " + getClientHost() + " registered a new user " + userName);
        } catch(Exception e) {
            e.getStackTrace();
        }

        Database.insertSupplier(userName,password);
    }

    public Supplier checkSupplierDupe(String userName){
        try {
            logger.log(Level.INFO, "Supplier " + getClientHost() + " checking for duplicated Username " + userName);
        } catch(Exception e) {
            e.getStackTrace();
        }

        return Database.supplierCheckDupe(userName);
    }

    public HashMap<Integer, Item> sViewItem(int userID) {
        HashMap<Integer, Item> items = Database.getItems(userID);
        try {
            logger.log(Level.INFO, "Supplier " + getClientHost() + " requested a list of all items (" + items.size() + ") for userID " + userID);
        } catch(Exception e) {
            e.getStackTrace();
        }

        return items;
    }

    public HashMap<Integer, Order> sViewOrders(int userID) {
        HashMap<Integer, Order> orders = Database.getOrders("supplier", userID);
        try {
            if(orders != null){
                logger.log(Level.INFO, "Supplier " + getClientHost() + " requested a list of all orders (" + orders.size() + ") for userID " + userID);
            }
        } catch(Exception e) {
            e.getStackTrace();
        }

        return orders;
    }

    public void confirmOrder(int itemID, int orderID){
        try {
            logger.log(Level.INFO, "Supplier " + getClientHost() + " confirmed an order " + orderID);
        } catch(Exception e) {
            e.getStackTrace();
        }
        Database.orderConfirm(itemID, orderID);
    }

    public void sRegisterItem(int itemQuantity, String itemName, int supplierID){
        Database.registerItem(itemQuantity, itemName, supplierID);
        try {
            logger.log(Level.INFO, "Supplier " + getClientHost() + " registered an item " + itemName);
        } catch(Exception e) {
            e.getStackTrace();
        }
    }

    public void sUpdateItemName(String updateItemName, int itemID){
        Database.updateItemName(updateItemName, itemID);
        try {
            logger.log(Level.INFO, "Supplier " + getClientHost() + " updated an item name " + itemID);
        } catch(Exception e) {
            e.getStackTrace();
        }
    }

    public void sUpdateItemQuantity(int updateItemQuantity, int itemID){
        Database.updateQuantity(updateItemQuantity, itemID);
        try {
            logger.log(Level.INFO, "Supplier " + getClientHost() + " updated an item quantity " + itemID);
        } catch(Exception e) {
            e.getStackTrace();
        }
    }

    public void sDeleteItem(int itemID){
        Database.deleteItem(itemID);
        try {
            logger.log(Level.INFO, "Supplier " + getClientHost() + " deleted an item " + itemID);
        } catch(Exception e) {
            e.getStackTrace();
        }
    }

}
