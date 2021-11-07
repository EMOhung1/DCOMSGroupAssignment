package ServerMachine;

import ClientMachine.ClientInterface;
import SupplierMachine.SupplierInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
        try {
            logger.log(Level.INFO, "Client " + getClientHost() + " logged in as user " + userName);
        } catch(Exception e) {}

        return Database.clientSearch(userName,password);
        //You need to return the user object saved, so when James login it returns James' account and not someone else, this is an example.
        //After creating a method, create an empty version of the method at ServerInterface.
    }

    public void clientInsert(String userName, String password){
        try {
            logger.log(Level.INFO, "Client " + getClientHost() + " registered a new user " + userName);
        } catch(Exception e) {}

        Database.insertClient(userName,password);
    }

    public HashMap<Integer, Item> cViewItem() {
        HashMap<Integer, Item> items = Database.getItems();
        try {
            logger.log(Level.INFO, "Client " + getClientHost() + " requested a list of all items (" + items.size() + ")");
        } catch(Exception e) {}

        return items;
    }

    public void purchaseItem(Client client, String address, HashMap<Item, Integer> cart) {
        int sum = 0;
        for(int i: cart.values()) {
            sum += i;
        }
        try {
            logger.log(Level.INFO, "Client " + getClientHost() + " (" + client.getUserName() + ") purchased " + sum + " items");
        } catch(Exception e) {}

        Database.insertOrder(client, address, cart);
    }

    public HashMap<Integer, Order> cViewOrders(int userID) {
        HashMap<Integer, Order> orders = Database.getOrders("client", userID);
        try {
            logger.log(Level.INFO, "Client " + getClientHost() + " requested a list of all orders (" + orders.size() + ") made by userID " + userID);
        } catch(Exception e) {}

        return orders;
    }


    //Supplier methods:

    public Supplier supplierLogin(String supplierName, String password){
        try {
            logger.log(Level.INFO, "Supplier " + getClientHost() + " logged in as user " + supplierName);
        } catch(Exception e) {}

        return Database.supplierSearch(supplierName,password);
        //You need to return the user object saved, so when James login it returns James' account and not someone else, this is an example.
        //After creating a method, create an empty version of the method at ServerInterface.
    }

    public void supplierInsert(String userName, String password){
        try {
            logger.log(Level.INFO, "Supplier " + getClientHost() + " registered a new user " + userName);
        } catch(Exception e) {}

        Database.insertSupplier(userName,password);
    }

    public HashMap<Integer, Item> sViewItem(int userID) {
        HashMap<Integer, Item> items = Database.getItems(userID);
        try {
            logger.log(Level.INFO, "Supplier " + getClientHost() + " requested a list of all items (" + items.size() + ") for userID " + userID);
        } catch(Exception e) {}

        return items;
    }

    public HashMap<Integer, Order> sViewOrders(int userID) {
        HashMap<Integer, Order> orders = Database.getOrders("supplier", userID);
        try {
            logger.log(Level.INFO, "Supplier " + getClientHost() + " requested a list of all orders (" + orders.size() + ") for userID " + userID);
        } catch(Exception e) {}

        return orders;
    }

}
