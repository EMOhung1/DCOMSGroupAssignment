package ServerMachine;

import ClientMachine.ClientInterface;
import SupplierMachine.SupplierInterface;

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

    public boolean clientLogin(String userName, String password){
        return true;
        //You need to return the user object saved, so when James login it returns James' account and not someone else, this is an example.
        //After creating a method, create an empty version of the method at ServerInterface.
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
