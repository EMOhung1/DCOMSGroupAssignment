package ServerMachine;

import java.io.Serializable;

public class Supplier implements Serializable {
    private int supplierId;
    private String userName;
    private String password;

    public Supplier(int supplierId, String userName, String password){
        this.supplierId = supplierId;
        this.userName = userName;
        this.password = password;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public String getuserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

}
