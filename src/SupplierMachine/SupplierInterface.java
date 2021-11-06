package SupplierMachine;

import ServerMachine.Client;
import ServerMachine.Item;
import ServerMachine.Supplier;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface SupplierInterface extends Remote {
    Supplier supplierLogin(String x, String y)throws RemoteException;
    void supplierInsert(String x,String y)throws RemoteException;
    HashMap<Integer, Item> sViewItem(int userID) throws RemoteException;
}
