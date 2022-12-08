package com.banking.bankingSystem.modules.DTO;
import java.math.BigDecimal;

public class AccountDTO {
    private Long primaryOwnerId;
    private Long secondaryOwnerId;
    private BigDecimal balance;
    private String secretKey;

    public AccountDTO(Long primaryOwnerId, Long secondaryOwnerId, BigDecimal balance, String secretKey) {
        setPrimaryOwnerId(primaryOwnerId);
        setSecondaryOwnerId(secondaryOwnerId);
        setBalance(balance);
        setSecretKey(secretKey);
    }

    public Long getPrimaryOwnerId() {
        return primaryOwnerId;
    }

    public void setPrimaryOwnerId(Long primaryOwnerId) {
        this.primaryOwnerId = primaryOwnerId;
    }

    public Long getSecondaryOwnerId() {
        return secondaryOwnerId;
    }

    public void setSecondaryOwnerId(Long secondaryOwnerId) {
        this.secondaryOwnerId = secondaryOwnerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
