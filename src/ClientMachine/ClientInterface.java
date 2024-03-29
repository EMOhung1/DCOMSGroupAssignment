package ClientMachine;

import ServerMachine.Item;
import ServerMachine.Order;
import ServerMachine.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface ClientInterface extends Remote {
    Client clientLogin(String x,String y)throws RemoteException;
    Client checkClientDupe(String x) throws RemoteException;
    void clientInsert(String x,String y)throws RemoteException;
    HashMap<Integer, Item> cViewItem() throws RemoteException;
    void purchaseItem(Client client, String address, HashMap<Item, Integer> cart) throws RemoteException;
    HashMap<Integer, Order> cViewOrders(int userID) throws RemoteException;
}
