package mobi.puut.entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Chaklader on 6/13/17.
 */
@Entity
@Table(name = "status")
public class Status {

    @Id
    @Column
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "user_id")
    private int user_id;

    @NotNull
    @Column(name = "balance")
    private float balance;

    @NotNull
    @Column(name = "address")
    @Size(min = 5, max = 90, message = "Address must be between 5 and 90 characters.")
    private String address;

    @NotNull
    @Column(name = "transaction")
    @Size(min = 5, max = 90, message = "Transaction history must be between 5 and 90 characters.")
    private String transaction;

    @NotNull
    @Column(name = "wallet_id")
    private long wallet_id;

    public Status() {

    }

    public Status(float balance, String transaction, String address, int user_id) {
        this.user_id = user_id;
        this.balance = balance;
        this.address = address;
        this.transaction = transaction;
    }

    public Status(int id, int user_id, float balance, String address, String transaction) {
        super();
        this.id = id;
        this.user_id = user_id;
        this.balance = balance;
        this.address = address;
        this.transaction = transaction;
    }

    public int getId() {
        return id;
    }


    public int getUser_id() {
        return user_id;
    }

    public float getBalance() {
        return balance;
    }

    public String getAddress() {
        return address;
    }

    public String getTransaction() {
        return transaction;
    }

    public long getWallet_id() {
        return wallet_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public void setWallet_id(long wallet_id) {
        this.wallet_id = wallet_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Status)) return false;

        Status status = (Status) o;

        if (id != status.id) return false;
        if (user_id != status.user_id) return false;
        if (Float.compare(status.balance, balance) != 0) return false;
        if (!address.equals(status.address)) return false;
        return transaction.equals(status.transaction);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + user_id;
        result = 31 * result + (balance != +0.0f ? Float.floatToIntBits(balance) : 0);
        result = 31 * result + address.hashCode();
        result = 31 * result + transaction.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", balance=" + balance +
                ", address='" + address + '\'' +
                ", transaction='" + transaction + '\'' +
                '}';
    }
}
