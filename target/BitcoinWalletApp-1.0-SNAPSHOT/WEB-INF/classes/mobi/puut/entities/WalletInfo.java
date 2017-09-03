package mobi.puut.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Chaklader on 6/24/17.
 */
@Entity
@Table(name = "wallet_info")
public class WalletInfo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "address")
    private String address;

    @NotNull
    @Column(name = "currency")
    private String currency;

    @NotNull
    @Column(name = "code")
    private String code;

    public WalletInfo() {
    }

    public WalletInfo(String address, String code, String currency) {
        this.currency = currency;
        this.code = code;
        this.address = address;
    }

    public WalletInfo(String currency, String address) {
        this.currency = currency;
        this.address = address;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
