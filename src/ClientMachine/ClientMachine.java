package ClientMachine;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientMachine {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        ClientInterface clientInterface = (ClientInterface) Naming.lookup("rmi://localhost:5050/Server");
    }
}
