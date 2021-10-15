package ServerMachine;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements ServerInterface {
    public Server()throws RemoteException{
        super();
    }

    //Insert methods here, example:

    public boolean clientLogin(String userName, String password){
        return true;
        //You need to return the user object saved, so when James login it returns James' account and not someone else, this is an example.
        //After creating a method, create an empty version of the method at ServerInterface.
    }
}
