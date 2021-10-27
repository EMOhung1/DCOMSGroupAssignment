package ServerMachine;

import java.io.Serializable;

public class Supplier implements Serializable {
    public int supplierId;
    public String userName;
    public String password;

    public Supplier(int supplierId, String userName, String password){
        this.supplierId = supplierId;
        this.userName = userName;
        this.password = password;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getuserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
