package SupplierMachine;

import ClientMachine.ClientInterface;
import ServerMachine.Client;
import ServerMachine.Item;
import ServerMachine.Supplier;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SupplierMachine {
    private static SupplierInterface supplierInterface;
    private static final Scanner scanner = new Scanner(System.in);

    private static Supplier currentSupplier;

    private static boolean loggedIn = false;

    public static void main(String[] args) {
        try{
            supplierInterface = (SupplierInterface) Naming.lookup("rmi://localhost:5050/Connect");
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
                System.out.println("New Supplier Registration:\n");

                System.out.print("Username: ");
                String newUsername = scanner.nextLine();
                System.out.print("Password: ");
                String newPassword = scanner.nextLine();

                try {
                    supplierInterface.supplierInsert(newUsername, newPassword);
                    currentSupplier = supplierInterface.supplierLogin(newUsername,newPassword);
                    if(currentSupplier.getuserName() != null && currentSupplier.getPassword() != null){
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
