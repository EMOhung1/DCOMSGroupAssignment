package ServerMachine;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.*;

public class ServerRegistry {
    private static final Logger logger = Logger.getLogger(ServerRegistry.class.getName());  //logger
    public static FileHandler logfile;

    public static void main(String[] args)throws RemoteException {
        //logger
        try {
            System.setProperty("java.util.logging.SimpleFormatter.format",
                    "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
            logfile = new FileHandler("./log.txt", true);
            //LogManager.getLogManager().reset();
            logger.addHandler(logfile);
            logfile.setFormatter(new SimpleFormatter());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        //initialize database
        Database.init();

        //create registry
        Registry reg = LocateRegistry.createRegistry(5050);
        reg.rebind("Connect",new Server());
        logger.log(Level.INFO, "Server registry started successfully");
    }
}
