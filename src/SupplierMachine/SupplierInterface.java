package SupplierMachine;

import ServerMachine.Client;
import ServerMachine.Item;
import ServerMachine.Order;
import ServerMachine.Supplier;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface SupplierInterface extends Remote {
    Supplier supplierLogin(String x, String y)throws RemoteException;
    Supplier checkSupplierDupe(String x) throws RemoteException;
    void confirmOrder(int x, int y) throws RemoteException;
    void supplierInsert(String x,String y)throws RemoteException;
    HashMap<Integer, Item> sViewItem(int userID) throws RemoteException;
    HashMap<Integer, Order> sViewOrders(int userID) throws RemoteException;
    void sRegisterItem(int x, String y, int z)throws RemoteException;
    void sUpdateItemName(String x, int y)throws RemoteException;
    void sUpdateItemQuantity(int x, int y)throws RemoteException;
    void sDeleteItem(int x)throws RemoteException;
}
