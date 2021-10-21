package SupplierMachine;

import ServerMachine.Item;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SupplierInterface extends Remote {
    ArrayList<Item> sViewItem(int userID) throws RemoteException;
}
