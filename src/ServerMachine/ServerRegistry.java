package ServerMachine;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerRegistry {
    public static void main(String[] args)throws RemoteException

    {

        Registry reg = LocateRegistry.createRegistry(5050);

        reg.rebind("Connect",new Server());

    }
}
