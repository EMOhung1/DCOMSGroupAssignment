package ServerMachine;

import java.io.Serializable;

public class Item implements Serializable {
    private final int itemID;
    private int itemQuantity;
    private String itemName, supplierName;

    public Item(int itemID, int itemQuantity, String itemName, String supplierName) {
        this.itemID = itemID; //change to generate unique id
        this.itemQuantity = itemQuantity;
        this.itemName = itemName;
        this.supplierName = supplierName;
    }

    public int getItemID() {
        return itemID;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSupplierName() {
        return supplierName;
    }
}
