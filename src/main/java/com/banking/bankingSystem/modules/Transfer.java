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
    private Long recevingId;
    private String recipientName;


    public Transfer() {
    }

    public Transfer(BigDecimal amount, Long sendingId, Long recevingId, String recipientName) {
        this.amount = amount;
        this.sendingId = sendingId;
        this.recevingId = recevingId;
        this.recipientName = recipientName;
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

    public Long getRecevingId() {
        return recevingId;
    }

    public void setRecevingId(Long recevingId) {
        this.recevingId = recevingId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}
