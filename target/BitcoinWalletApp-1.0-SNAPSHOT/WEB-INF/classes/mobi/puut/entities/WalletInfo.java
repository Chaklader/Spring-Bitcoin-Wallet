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
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "address")
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
