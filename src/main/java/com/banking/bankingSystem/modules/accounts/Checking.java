package com.banking.bankingSystem.modules.accounts;
import com.banking.bankingSystem.modules.users.User;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Checking extends Account {
    BigDecimal penaltyFee = BigDecimal.valueOf(40);
    BigDecimal minimumBalance = BigDecimal.valueOf(250);
    BigDecimal monthlyMaintenanceFee = BigDecimal.valueOf(12.00);

    public Checking() {    }

    public Checking(BigDecimal balance, String secretKey, User primaryOwner) {
        super(balance, secretKey, primaryOwner);
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public void setMonthlyMaintenanceFee(BigDecimal monthlyMaintenanceFee) {
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }
}
