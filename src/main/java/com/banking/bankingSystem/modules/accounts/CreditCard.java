package com.banking.bankingSystem.modules.accounts;
import com.banking.bankingSystem.modules.users.User;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
public class CreditCard extends Account {
    @DecimalMin(value = "100.00")
    @DecimalMax(value = "100000.00")
    BigDecimal creditLimit = BigDecimal.valueOf(100);
    @DecimalMin(value = "0.1")
    BigDecimal interestRate = BigDecimal.valueOf(0.2);
    BigDecimal penaltyFee = BigDecimal.valueOf(40);
    LocalDate interestDate;

    public CreditCard() {    }

    public CreditCard(BigDecimal balance, User primaryOwner) {
        super(balance, null, primaryOwner);
    }

    public CreditCard(BigDecimal balance, String secretKey, User primaryOwner, BigDecimal creditLimit, BigDecimal interestRate) {
        super(balance, null, primaryOwner);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
        setInterestDate(LocalDate.now());
    }

    public CreditCard(BigDecimal interestRate) {
        setInterestRate(interestRate);
    }

    public CreditCard(BigDecimal balance, String secretKey, User primaryOwner, BigDecimal creditLimit) {
        super(balance, null, primaryOwner);
        setCreditLimit(creditLimit);
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public LocalDate getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(LocalDate interestDate) {
        this.interestDate = interestDate;
    }
}
