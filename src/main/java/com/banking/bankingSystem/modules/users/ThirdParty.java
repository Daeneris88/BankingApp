package com.banking.bankingSystem.modules.users;
import com.banking.bankingSystem.modules.Transfer;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class ThirdParty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hashedKey;

    public ThirdParty() {    }
    public ThirdParty(String hashedKey) {
        setHashedKey(hashedKey);
    }

    public String getHashedKey() {
        return hashedKey;
    }
    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


}
