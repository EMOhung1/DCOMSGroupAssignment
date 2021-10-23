package SupplierMachine;

import ServerMachine.Item;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface SupplierInterface extends Remote {
    HashMap<Integer, Item> sViewItem(int userID) throws RemoteException;
}
