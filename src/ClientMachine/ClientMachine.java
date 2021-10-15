package ClientMachine;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientMachine {


    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        try{
            ClientInterface clientInterface = (ClientInterface) Naming.lookup("rmi://localhost:5050/Connect");
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
    }
}
