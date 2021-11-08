package SupplierMachine;

import ClientMachine.ClientInterface;
import ServerMachine.Client;
import ServerMachine.Item;
import ServerMachine.Order;
import ServerMachine.Supplier;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

public class SupplierMachine {
    private static SupplierInterface supplierInterface;
    private static final Scanner scanner = new Scanner(System.in);

    private static Supplier currentSupplier;
    private static Supplier checkDupe;

    private static boolean loggedIn = false;

    public static void main(String[] args) {
        try{
            supplierInterface = (SupplierInterface) Naming.lookup("rmi://localhost:5050/Connect");
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
                System.out.println("New Supplier Registration:\n");

                System.out.print("Username: ");
                String newUsername = scanner.nextLine();
                System.out.print("Password: ");
                String newPassword = scanner.nextLine();

                if(!newUsername.isBlank() && !newPassword.isBlank()){
                    try {
                        checkDupe = supplierInterface.checkSupplierDupe(newUsername);
                        if(checkDupe.getuserName() == null && checkDupe.getPassword() == null){
                            supplierInterface.supplierInsert(newUsername, newPassword);
                            currentSupplier = supplierInterface.supplierLogin(newUsername,newPassword);
                            if(currentSupplier.getuserName() != null && currentSupplier.getPassword() != null){
                                loggedIn = true;
                            }
                            else{
                                System.out.println("\nInvalid Login Credentials\n");
                            }
                        }
                        else{
                            System.out.println("\nDuplicate Username Detected, Please Provide A Unique Username\n");
                            checkDupe = null;
                        }
                    }catch(Exception ex){System.out.println(ex.getMessage());}
                }
                else
                {
                    System.out.println("\nPlease Do Not Leave The Username Or The Password As Blank\n");
                }

            } else if(username.equals("-1") && password.equals("-1")) {
                return;
            }else{
                //Check user credential here, if account exist the user account is returned and shown here
                try {
                    currentSupplier = supplierInterface.supplierLogin(username,password);
                    if(currentSupplier.getuserName() != null && currentSupplier.getPassword() != null){
                        loggedIn = true;
                    }
                    else{
                        System.out.println("\nInvalid Login Credentials\n");
                    }
                }catch(Exception ex){System.out.println(ex.getMessage());}
            }

            if(loggedIn) {
                System.out.print("\nWelcome, " + currentSupplier.getuserName());  //replace with username

                while (loggedIn) {
                    System.out.println("\n\n1. View items\n2. View orders\n3. Logout\n\n");
                    System.out.print("Option: ");

                    int option = scanner.nextInt();

                    switch (option) {
                        case 1:
                            Item selectedItem;
                            try {
                                HashMap<Integer, Item> items = supplierInterface.sViewItem(currentSupplier.getSupplierId());
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
                                    selectedItem = items.get(itemID);
                                } else {
                                    System.out.println("ItemID does not exist!");
                                    break;
                                }
                            } catch (Exception e) {
                                System.out.println(e + "\n");
                                break;
                            }

                            boolean y = true;
                            while (y) {
                                System.out.println("\n\nID: " + selectedItem.getItemID()
                                        + "\nName: " + selectedItem.getItemName()
                                        + "\nQuantity: " + selectedItem.getItemQuantity());
                                System.out.println("\n1. Edit name\n2. Edit quantity\n3. Delete item\n4. Back\n\n");
                                System.out.print("Option: ");

                                option = scanner.nextInt();

                                switch (option) {
                                    case 1:
                                        //edit name
                                        break;
                                    case 2:
                                        //edit quantity
                                        break;
                                    case 3:
                                        //delete item
                                        break;
                                    case 4:
                                        y = false;
                                        break;
                                }
                            }
                            break;
                        case 2:
                            //view orders
                            HashMap<Integer, Order> orders;
                            try {
                                orders = supplierInterface.sViewOrders(currentSupplier.getSupplierId());
                            } catch(Exception e) {
                                System.out.println(e.getMessage());break;}
                            if(orders.isEmpty()) {
                                System.out.println("No orders found!");
                                break;
                            }
                            System.out.format("%n%-11s%-25s%-30s%-30s%-10s%n", "OrderID", "Date", "Client", "Address", "No. of items");
                            for (Map.Entry<Integer, Order> o : orders.entrySet()) {
                                int sum = 0;
                                for(int i: o.getValue().getItemList().values()) {
                                    sum += i;
                                }
                                System.out.format("%1$-10s %2$tY-%2$tm-%2$td %2$tH:%2$tM:%2$tS %3$-4s %4$-29s %5$-29s %6$-10s%n",
                                        o.getKey(),
                                        o.getValue().getCreationDate(),
                                        "",
                                        o.getValue().getClientUserName(),
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
                            System.out.println("Client: " + order.getClientUserName());
                            System.out.println("Address: " + order.getAddress());

                            boolean confirmed = false;
                            System.out.println("\nItems:");
                            System.out.format("%-10s%-25s%-15s%-15s%n", "ItemID", "Item Name", "Quantity", "Confirmed");
                            List<Integer> itemIdList = new ArrayList<Integer>();
                            for (Map.Entry<Item, Integer> item : order.getItemList().entrySet()) {
                                System.out.format("%-10s%-25s%-15s%-15s%n",
                                        item.getKey().getItemID(),
                                        item.getKey().getItemName(),
                                        item.getValue(),
                                        item.getKey().getConfirm());

                                confirmed = item.getKey().getConfirm();
                                itemIdList.add(item.getKey().getItemID());
                            }

                            if(!confirmed) {
                                System.out.print("\nConfirm all items? (y/n): ");
                                scanner.nextLine();
                                String confirm = scanner.nextLine();
                                if(confirm.equals("y")) {
                                    try {
                                        for (int itemId : itemIdList){
                                            supplierInterface.confirmOrder(itemId, order.getOrderID());
                                        }
                                    }catch(Exception ex){System.out.println(ex.getMessage());}
                                }
                            }

                            break;
                        case 3:
                            scanner.nextLine();
                            currentSupplier = null;
                            loggedIn = false;
                            break;
                    }
                }
            }
        }
    }
}
