package SupplierMachine;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class SupplierMachine {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        SupplierInterface supplierInterface = (SupplierInterface) Naming.lookup("rmi://localhost:5050/Server");
    }
}
