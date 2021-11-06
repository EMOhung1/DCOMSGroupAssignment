package ServerMachine;

import ClientMachine.ClientInterface;
import SupplierMachine.SupplierInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends UnicastRemoteObject implements ClientInterface, SupplierInterface {
    private static final Logger logger = Logger.getLogger(Server.class.getName());  //logger

    public Server()throws RemoteException{
        super();
    }

    //Client methods:

    public Client clientLogin(String userName, String password){
        Database database = new Database();
        Client currentClient = database.clientSearch(userName,password);
        return currentClient;
        //You need to return the user object saved, so when James login it returns James' account and not someone else, this is an example.
        //After creating a method, create an empty version of the method at ServerInterface.
    }

    public Supplier supplierLogin(String supplierName, String password){
        Database database = new Database();
        Supplier currentSupplier = database.supplierSearch(supplierName,password);
        return currentSupplier;
        //You need to return the user object saved, so when James login it returns James' account and not someone else, this is an example.
        //After creating a method, create an empty version of the method at ServerInterface.
    }

    public void clientInsert(String userName, String password){
        Database database = new Database();
        database.insertClient(userName,password);
    }

    public void supplierInsert(String userName, String password){
        Database database = new Database();
        database.insertSupplier(userName,password);
    }

    public HashMap<Integer, Item> cViewItem() {
        return null;
    }

    public Order purchaseItem() {
        return null;
    }


    //Supplier methods:

    public HashMap<Integer, Item> sViewItem(int userID) {
        return null;
    }

}
