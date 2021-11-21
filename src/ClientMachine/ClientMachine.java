package ClientMachine;

import ServerMachine.Client;
import ServerMachine.Item;
import ServerMachine.Order;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ClientMachine {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean loggedIn = false;
    private static Client currentClient;
    private static HashMap<Item, Integer> cart;

    public static void main(String[] args){
        ClientInterface clientInterface;
        Client checkDupe;

        try{
            clientInterface = (ClientInterface) Naming.lookup("rmi://localhost:5000/Connect");
        } catch(Exception e) {
            System.out.println("Failed to connect to the CKFC Delivery System!");
            return;
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
                if(!newUsername.isBlank() && !newPassword.isBlank()){
                    try {
                        checkDupe = clientInterface.checkClientDupe(newUsername);
                        if(checkDupe.getUserName() == null && checkDupe.getPassword() == null){
                            clientInterface.clientInsert(newUsername, newPassword);
                            currentClient = clientInterface.clientLogin(newUsername,newPassword);

                            if(currentClient.getUserName() != null && currentClient.getPassword() != null){
                                loggedIn = true;
                            }
                            else{
                                System.out.println("\nInvalid Login Credentials\n");
                            }
                        }
                        else{
                            System.out.println("\nDuplicate Username Detected, Please Provide A Unique Username\n");

                        }
                    }catch(Exception ex){System.out.println(ex.getMessage());}
                }else
                {
                    System.out.println("\nPlease Do Not Leave The Username Or The Password As Blank\n");
                }


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

                    ArrayList<Item> ItemList = new ArrayList<>();
                    Item currentItem;
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
                                        //System.out.println(e.getKey() + ". " + e.getValue().getItemName());
                                        ItemList.add(items.get(e.getKey()));
                                    }
                                }

                                for(int i = 0; i < ItemList.size(); i++){
                                    currentItem = ItemList.get(i);
                                    System.out.println(i + 1 + ".\t" + currentItem.getItemName());
                                }

                                System.out.print("\nOption: ");
                                int itemID = scanner.nextInt();

                                if(itemID-1 > ItemList.size() || itemID-1 < 0){
                                    System.out.println("Invalid Item!");
                                }
                                else {
                                    itemMenu(ItemList.get(itemID-1));
                                }

                            } catch(Exception e) {
                                System.out.println(e + "\n");
                            }
                            break;
                        case 2:
                            //view all items

                            try {
                                HashMap<Integer, Item> items = clientInterface.cViewItem();
                                if(items.isEmpty()) {
                                    System.out.println("No items found!");
                                    break;
                                }
                                for (Map.Entry<Integer, Item> e : items.entrySet()) {
                                    //System.out.println(e.getKey() + ". " + e.getValue().getItemName());
                                    ItemList.add(items.get(e.getKey()));
                                }

                                for(int i = 0; i < ItemList.size(); i++){
                                    currentItem = ItemList.get(i);
                                    System.out.println(i + 1 + ".\t" + currentItem.getItemName());
                                }

                                System.out.print("\nOption: ");
                                int itemID = scanner.nextInt();

                                if(itemID-1 > ItemList.size() || itemID-1 < 0){
                                    System.out.println("Invalid Item!");
                                }
                                else {
                                    itemMenu(ItemList.get(itemID-1));
                                }

                            } catch (Exception e) {
                                System.out.println(e + "\n");
                                break;
                            }
                            break;
                        case 3:
                            //view cart
                            if(!cart.isEmpty()) {
                                System.out.format("%n%-25s%-30s%-3s%n", "Item Name", "Supplier", "Quantity");
                                for (Map.Entry<Item, Integer> item : cart.entrySet()) {
                                    System.out.format("%-25s%-30s%-3s%n",
                                            //item.getKey().getItemID(),
                                            item.getKey().getItemName(),
                                            item.getKey().getSupplierName(),
                                            item.getValue());
                                }

                                System.out.print("\nCheckout? (y/n): ");
                                scanner.nextLine();
                                String checkout = scanner.nextLine();
                                if (checkout.equals("y")) {
                                    System.out.print("\nAddress: ");
                                    String address = scanner.nextLine();
                                    try {
                                        clientInterface.purchaseItem(currentClient, address, cart);
                                        System.out.println("Order submitted successfully!");

                                    } catch (Exception e) {
                                        System.out.println(e + "\n");
                                    }
                                } else {
                                    System.out.print("\nClear cart? (y/n): ");
                                    String clear = scanner.nextLine();
                                    if(clear.equals("y")) {
                                        cart = new HashMap<>();
                                    }
                                }
                            } else {
                                System.out.println("Cart is empty!");
                            }
                            break;
                        case 4:
                            //view orders
                            HashMap<Integer, Order> orders;
                            try {
                                orders = clientInterface.cViewOrders(currentClient.getUserId());
                            } catch(Exception e) {
                                System.out.println(e.getMessage());break;}
                            if(orders.isEmpty()) {
                                System.out.println("No orders found!");
                                break;
                            }
                            System.out.format("%n%-11s%-25s%-30s%-10s%n", "OrderID", "Date", "Address", "No. of items");
                            for (Map.Entry<Integer, Order> o : orders.entrySet()) {
                                int sum = 0;
                                for(int i: o.getValue().getItemList().values()) {
                                    sum += i;
                                }
                                System.out.format("%1$-10s %2$tY-%2$tm-%2$td %2$tH:%2$tM:%2$tS %3$-4s %4$-29s %5$-10s%n",
                                        o.getKey(),
                                        o.getValue().getCreationDate(),
                                        "",
                                        o.getValue().getAddress(),
                                        sum);
                            }

                            System.out.print("\nOption: ");
                            int orderID = scanner.nextInt();
                            Order order;
                            if(orders.containsKey(orderID)) {
                                order = orders.get(orderID);
                            } else {
                                System.out.println("OrderID does not exist!");
                                break;
                            }

                            System.out.println("\nOrderID: " + order.getOrderID());
                            System.out.format("%1$s %2$tY-%2$tm-%2$td %2$tH:%2$tM:%2$tS%n", "Order date: ", order.getCreationDate());
                            System.out.println("Address: " + order.getAddress());

                            System.out.println("\nItems:");
                            System.out.format("%-10s%-25s%-30s%-15s%-15s%n", "ItemID", "Item Name", "Supplier", "Quantity", "Confirmed");
                            for (Map.Entry<Item, Integer> item : order.getItemList().entrySet()) {
                                System.out.format("%-10s%-25s%-30s%-15s%-15s%n",
                                        item.getKey().getItemID(),
                                        item.getKey().getItemName(),
                                        item.getKey().getSupplierName(),
                                        item.getValue(),
                                        item.getKey().getConfirm());
                            }

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
            System.out.println("\n\nName: "+item.getItemName()
                    +"\nSupplier: "+item.getSupplierName()
                    +"\nQuantity: "+item.getItemQuantity());
            System.out.println("\n1. Add to cart\n2. Back\n\n");
            System.out.print("Option: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1 -> {
                    //add to cart
                    System.out.print("Quantity: ");
                    int qty = scanner.nextInt();
                    if (qty > item.getItemQuantity() || (cart.containsKey(item) && qty + cart.get(item) > item.getItemQuantity())) {
                        System.out.println("Quantity exceeds available item quantity!");
                    } else if (qty < 1) {
                        System.out.println("Invalid quantity!");
                    } else {
                        cart.put(item, cart.getOrDefault(item, 0) + qty);
                    }
                    y = false;
                }
                case 2 -> y = false;
            }
        }
    }
}
