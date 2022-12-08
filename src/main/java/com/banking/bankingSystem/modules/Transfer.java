package com.banking.bankingSystem.modules;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private Long sendingId;
    private Long receivingId;
    private String recipientName;
    private String secretKey;

    public Transfer() {    }
    public Transfer(BigDecimal amount, Long sendingId, Long receivingId, String recipientName) {
        setAmount(amount);
        setSendingId(sendingId);
        setReceivingId(receivingId);
        setRecipientName(recipientName);
    }
    public Transfer(BigDecimal amount, Long sendingId, String secretKey) {
        setAmount(amount);
        setSecretKey(secretKey);
        setSendingId(sendingId);
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getSendingId() {
        return sendingId;
    }

    public void setSendingId(Long sendingId) {
        this.sendingId = sendingId;
    }

    public Long getReceivingId() {
        return receivingId;
    }

    public void setReceivingId(Long receivingId) {
        this.receivingId = receivingId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}
