package ServerMachine;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    boolean clientLogin(String x,String y)throws RemoteException;
}
