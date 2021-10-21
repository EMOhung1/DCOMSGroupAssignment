package ClientMachine;

import ServerMachine.Item;
import ServerMachine.Order;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientInterface extends Remote {
    ArrayList<Item> cViewItem() throws RemoteException;
    Order purchaseItem() throws RemoteException;
}
