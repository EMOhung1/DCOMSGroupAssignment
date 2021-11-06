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
    private static boolean loggedIn = false;

    private static Client currentClient;

    public static void main(String[] args){
        try{
            clientInterface = (ClientInterface) Naming.lookup("rmi://localhost:5050/Connect");
        }catch (Exception e){
            System.out.println(e+"\n");
        }

        while(!loggedIn){
            System.out.println("\nWelcome to CKFC Delivery System!\nDo not have an account? Register now by typing 0 in both Username and Password.\nType -1 in both Username and Password to exit.\n");

            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            if(username.equals("0") && password.equals("0")){
                //Run the register method here
                System.out.println("New Customer Registration:\n");

                System.out.print("Username: ");
                String newUsername = scanner.nextLine();
                System.out.print("Password: ");
                String newPassword = scanner.nextLine();

                try {
                    clientInterface.clientInsert(newUsername, newPassword);
                    currentClient = clientInterface.clientLogin(newUsername,newPassword);

                    if(currentClient.getUserName() != null && currentClient.getPassword() != null){
                        loggedIn = true;
                    }
                    else{
                        System.out.println("\nInvalid Login Credentials\n");
                    }

                }catch(Exception ex){System.out.println(ex.getMessage());}

            } else if(username.equals("-1") && password.equals("-1")) {
                return;
            }else{
                //Check user credential here, if account exist the user account is returned and shown here
                try {
                    currentClient = clientInterface.clientLogin(username,password);
                    if(currentClient.getUserName() != null && currentClient.getPassword() != null){
                        loggedIn = true;
                    }
                    else{
                        System.out.println("\nInvalid Login Credentials\n");
                    }
                }catch(Exception ex){System.out.println(ex.getMessage());}
            }

            if(loggedIn) {
                System.out.println("Welcome, " + currentClient.getUserName());  //replace with username

                while(loggedIn) {
                    System.out.println("\n1. Search items\n2. View all items\n3. View cart\n4. View orders\n5. Logout\n\n");
                    System.out.print("Option: ");

                    int option = scanner.nextInt();

                    switch (option) {
                        case 1:
                            //search items
                            break;
                        case 2:
                            Item selectedItem;
                            try {
                                for (Map.Entry<Integer, Item> e : clientInterface.cViewItem().entrySet()) {  //replace with user id
                                    System.out.println(e.getKey() + ". " + e.getValue().getItemName());
                                }
                                System.out.print("\nOption: ");
                                selectedItem = clientInterface.cViewItem().get(scanner.nextInt());  //replace with user id
                            } catch (Exception e) {
                                System.out.println(e + "\n");
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
                            scanner.nextLine();
                            currentClient = null;
                            loggedIn = false;
                            break;
                    }
                }
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
