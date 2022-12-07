package com.banking.bankingSystem.modules.accounts;
import com.banking.bankingSystem.modules.users.User;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class StudentChecking extends Account {
    public StudentChecking() {    }

    public StudentChecking(BigDecimal balance, String secretKey, User primaryOwner) {
        super(balance, secretKey, primaryOwner);
    }

}
