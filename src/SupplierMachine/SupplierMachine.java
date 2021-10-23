package SupplierMachine;

import ClientMachine.ClientInterface;
import ServerMachine.Item;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Scanner;

public class SupplierMachine {
    static SupplierInterface supplierInterface;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try{
            supplierInterface = (SupplierInterface) Naming.lookup("rmi://localhost:5050/Connect");
        }catch (Exception e){
            System.out.println(e+"\n");
        }

        System.out.println("Welcome to CKFC Delivery System!\n\nUsername:\nPassword:\n\nDo not have an account? Register now by typing 0 in both Username and Password.");

        String username = scanner.nextLine();
        String password = scanner.nextLine();

        if(username.equals("0") && password.equals("0")){
            //Run the register method here
        }else{
            //Check user credential here, if account exist the user account is returned and shown here
        }

        System.out.println("Welcome, "+"user");

        boolean x = true;
        while(x) {
            System.out.println("\n1. View items\n2. View orders\n3.Logout\n\n");
            System.out.print("Option: ");

            int option = scanner.nextInt();

            switch(option) {
                case 1:
                    Item selectedItem;
                    try {
                        for(Map.Entry<Integer, Item> e: supplierInterface.sViewItem(0).entrySet()) {  //replace with user id
                            System.out.println(e.getKey()+". "+e.getValue().getItemName());
                        }
                        System.out.print("\nOption: ");
                        selectedItem = supplierInterface.sViewItem(0).get(scanner.nextInt());  //replace with user id
                    } catch(Exception e) {
                        System.out.println(e+"\n");
                        break;
                    }

                    boolean y = true;
                    while(y) {
                        System.out.println("\n\nID: "+selectedItem.getItemID()
                                +"\nName: "+selectedItem.getItemName()
                                +"\nQuantity: "+selectedItem.getItemQuantity());
                        System.out.println("\n1. Edit name\n2. Edit quantity\n3. Delete item\n4. Back\n");
                        System.out.print("Option: ");

                        option = scanner.nextInt();

                        switch(option) {
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
                    //logout code here
                    x = false;
                    break;
            }
        }
    }
}
