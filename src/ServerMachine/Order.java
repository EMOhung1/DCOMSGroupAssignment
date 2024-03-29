package ServerMachine;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Order implements Serializable {
    private final int orderID;
    private String address, clientUserName;
    private HashMap<Item, Integer> itemList;
    private Date creationDate;

    public Order(int orderID, String address, String clientUserName, HashMap<Item, Integer> itemList, Date creationDate) {
        this.orderID = orderID; //change to generate unique id
        this.address = address;
        this.clientUserName = clientUserName;
        this.itemList = itemList;
        this.creationDate = creationDate;
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

    public HashMap<Item, Integer> getItemList() {
        return itemList;
    }

    public void setItemList(HashMap<Item, Integer> itemList) {
        this.itemList = itemList;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
