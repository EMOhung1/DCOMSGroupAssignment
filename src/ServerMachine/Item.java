package ServerMachine;

public class Item {
    private final int itemID;
    private int itemQuantity;
    private String itemName;

    public Item(int itemID, int itemQuantity, String itemName) {
        this.itemID = itemID; //change to generate unique id
        this.itemQuantity = itemQuantity;
        this.itemName = itemName;
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
}
