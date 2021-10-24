package ServerMachine;

import java.util.ArrayList;

public class Order {
    private final int orderID;
    private String address, clientUserName;
    private ArrayList<Item> itemList;

    public Order(int orderID, String address, String clientUserName, ArrayList<Item> itemList) {
        this.orderID = orderID; //change to generate unique id
        this.address = address;
        this.clientUserName = clientUserName;
        this.itemList = itemList;
    }

    public int getOrderID() {
        return orderID;
    }

    public String getAddress() {
        return address;
    }

    public String getClientUserName() {
        return clientUserName;
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }
}
