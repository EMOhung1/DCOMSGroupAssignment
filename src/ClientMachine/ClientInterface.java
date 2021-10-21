package ClientMachine;

import ServerMachine.Item;
import ServerMachine.Order;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientInterface extends Remote {
    boolean clientLogin(String x,String y)throws RemoteException;
    ArrayList<Item> cViewItem() throws RemoteException;
    Order purchaseItem() throws RemoteException;
}
