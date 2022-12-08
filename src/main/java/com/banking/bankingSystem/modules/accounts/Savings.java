package com.banking.bankingSystem.modules.accounts;
import com.banking.bankingSystem.modules.users.User;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Savings extends Account {
    BigDecimal penaltyFee= BigDecimal.valueOf(40);
    @DecimalMin(value = "100.00")
    @DecimalMax(value = "1000.00")
    BigDecimal minimumBalance = BigDecimal.valueOf(1000.00);
    @DecimalMax(value = "0.5")
    BigDecimal interestRate = BigDecimal.valueOf(0.0025);
    LocalDate interestDate;

    public Savings() {    }

    public Savings(BigDecimal balance, String secretKey, User primaryOwner) {
        super(balance, secretKey, primaryOwner);
        setInterestDate(LocalDate.now());
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

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(LocalDate interestDate) {
        this.interestDate = interestDate;
    }
}
