package ClientMachine;

import ServerMachine.Client;
import ServerMachine.Item;
import ServerMachine.Order;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ClientMachine {
    private static ClientInterface clientInterface;
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean loggedIn = false;

    private static Client currentClient;
    private static HashMap<Item, Integer> cart;

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
                System.out.print("\nWelcome, " + currentClient.getUserName());  //replace with username

                cart = new HashMap<>();
                while(loggedIn) {
                    System.out.println("\n\n1. Search items\n2. View all items\n3. View cart\n4. View orders\n5. Logout\n\n");
                    System.out.print("Option: ");

                    int option = scanner.nextInt();

                    switch (option) {
                        case 1:
                            //search items
                            try {
                                HashMap<Integer, Item> items = clientInterface.cViewItem();

                                System.out.print("\nSearch: ");
                                scanner.nextLine();
                                String search = scanner.nextLine();

                                for (Map.Entry<Integer, Item> e : items.entrySet()) {
                                    if(e.getValue().getItemName().contains(search)) {
                                        System.out.println(e.getKey() + ". " + e.getValue().getItemName());
                                    }
                                }
                                System.out.print("\nOption: ");
                                int itemID = scanner.nextInt();
                                if(items.containsKey(itemID)) {
                                    itemMenu(items.get(itemID));
                                } else {
                                    System.out.println("ItemID does not exist!");
                                }
                            } catch(Exception e) {
                                System.out.println(e + "\n");
                            }
                            break;
                        case 2:
                            //view all items
                            Item selectedItem;
                            try {
                                HashMap<Integer, Item> items = clientInterface.cViewItem();
                                if(items.isEmpty()) {
                                    System.out.println("No items found!");
                                    break;
                                }
                                for (Map.Entry<Integer, Item> e : items.entrySet()) {
                                    System.out.println(e.getKey() + ". " + e.getValue().getItemName());
                                }
                                System.out.print("\nOption: ");
                                int itemID = scanner.nextInt();
                                if(items.containsKey(itemID)) {
                                    itemMenu(items.get(itemID));
                                } else {
                                    System.out.println("ItemID does not exist!");
                                }

                            } catch (Exception e) {
                                System.out.println(e + "\n");
                                break;
                            }
                            break;
                        case 3:
                            //view cart
                            System.out.println("\nCart:\n");
                            if(!cart.isEmpty()) {
                                System.out.format("%-10s%-40s%-40s%-3s%n", "ItemID", "Item Name", "Supplier", "Quantity");
                                for (Map.Entry<Item, Integer> item : cart.entrySet()) {
                                    System.out.format("%-10s%-40s%-40s%-3s%n",
                                            item.getKey().getItemID(),
                                            item.getKey().getItemName(),
                                            item.getKey().getSupplierName(),
                                            item.getValue());
                                }

                                System.out.print("\nCheckout? (y/n): ");
                                String checkout = scanner.nextLine();
                                if (checkout.equals("y")) {
                                    try {
                                        clientInterface.purchaseItem(currentClient, cart);
                                        System.out.println("Order submitted successfully!");

                                    } catch (Exception e) {
                                        System.out.println(e + "\n");
                                    }
                                }
                            } else {
                                System.out.println("Cart is empty!");
                            }
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
                    +"\nSupplier: "+item.getSupplierName()
                    +"\nQuantity: "+item.getItemQuantity());
            System.out.println("\n1. Add to cart\n2. Back\n\n");
            System.out.print("Option: ");

            int option = scanner.nextInt();

            switch(option) {
                case 1:
                    //add to cart
                    System.out.print("Quantity: ");
                    int qty = scanner.nextInt();
                    cart.put(item, cart.getOrDefault(item, 0) + qty);
                    y = false;
                    break;
                case 2:
                    y = false;
                    break;
            }
        }
    }
}
