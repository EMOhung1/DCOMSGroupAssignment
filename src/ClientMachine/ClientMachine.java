package ClientMachine;

import ServerMachine.Client;
import ServerMachine.Item;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

public class ClientMachine {
    private static ClientInterface clientInterface;
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean running = true;
    private static boolean loggedIn = false;

    private static Client currentClient;

    public static void main(String[] args){
        try{
            clientInterface = (ClientInterface) Naming.lookup("rmi://localhost:5050/Connect");
        }catch (Exception e){
            System.out.println(e+"\n");
        }
        while(running){
            while(!loggedIn){
                System.out.println("Welcome to CKFC Delivery System!\n\nUsername:\nPassword:\n\nDo not have an account? Register now by typing 0 in both Username and Password.");

                String username = scanner.nextLine();
                System.out.println("username: " + username);

                String password = scanner.nextLine();
                System.out.println("password: " + password);

                if(username.equals("0") && password.equals("0")){
                    //Run the register method here
                    System.out.println("New Customer Registration:\n\nUsername:\nPassword");
                    String newUsername = scanner.nextLine();
                    String newPassword = scanner.nextLine();

                    try {
                        clientInterface.clientInsert(newUsername, newPassword);
                        currentClient = clientInterface.clientLogin(newUsername,newPassword);

                        if(currentClient.getUserName() != null && currentClient.getPassword() != null){
                            loggedIn = true;
                        }
                        else{
                            System.out.println("Invalid Login Credentials");
                        }

                    }catch(Exception ex){System.out.println(ex.getMessage());};

                }else{
                    //Check user credential here, if account exist the user account is returned and shown here
                    try {
                        currentClient = clientInterface.clientLogin(username,password);
                        if(currentClient.getUserName() != null && currentClient.getPassword() != null){
                            loggedIn = true;
                        }
                        else{
                            System.out.println("Invalid Login Credentials");
                        }
                    }catch(Exception ex){System.out.println(ex.getMessage());};
                }
            }


            System.out.println("Welcome, "+ currentClient.getUserName());  //replace with username

            boolean x = true;
            while(x) {
                System.out.println("\n1. Search items\n2. View all items\n3. View cart\n4. View orders\n5. Logout\n6. Exit System\n");
                System.out.print("Option: ");

                int option = scanner.nextInt();
                scanner.nextLine(); //I ADDED THIS HERE, OTHERWISE SCANNER.NEXTLINE FOR LOGIN AND REGISTER WONT WORK - Yu Ze

                switch(option) {
                    case 1:
                        //search items
                        break;
                    case 2:
                        Item selectedItem;
                        try {
                            for(Map.Entry<Integer, Item> e: clientInterface.cViewItem().entrySet()) {  //replace with user id
                                System.out.println(e.getKey()+". "+e.getValue().getItemName());
                            }
                            System.out.print("\nOption: ");
                            selectedItem = clientInterface.cViewItem().get(scanner.nextInt());  //replace with user id
                        } catch(Exception e) {
                            System.out.println(e+"\n");
                            break;
                        }

                        itemMenu(selectedItem);
                        break;
                    case 3:
                        //view cart
                        break;
                    case 4:
                        //view orders
                        break;
                    case 5:
                        //logout code here
                        loggedIn = false;
                        break;
                    case 6:
                        //exit code here
                        running = false;
                        loggedIn = false;
                        break;
                }
                if(!loggedIn){
                    break;
                }
            }
            if(!running){
                break;
            }
        }
    }

    public static void itemMenu(Item item) {
        boolean y = true;
        while(y) {
            System.out.println("\n\nID: "+item.getItemID()
                    +"\nName: "+item.getItemName()
                    +"\nQuantity: "+item.getItemQuantity());
            System.out.println("\n1. Add to cart\n2. Buy now\n3. Back\n\n");
            System.out.print("Option: ");

            int option = scanner.nextInt();

            switch(option) {
                case 1:
                    //add to cart
                    break;
                case 2:
                    //buy now
                    break;
                case 3:
                    y = false;
                    break;
            }
        }
    }
}
