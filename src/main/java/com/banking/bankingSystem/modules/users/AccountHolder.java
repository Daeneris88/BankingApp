package com.banking.bankingSystem.modules.users;
import com.banking.bankingSystem.modules.accounts.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AccountHolder extends User {
    @Embedded
    private Address primaryAddress;
    private String mailAddress;
    private LocalDate dateOfBirth;
    @OneToMany (mappedBy = "primaryOwner")
    @JsonIgnore
    private List<Account> primaryAccountList = new ArrayList<>();

    @OneToMany (mappedBy = "secondaryOwner")
    @JsonIgnore
    private List<Account> secondaryAccountList = new ArrayList<>();

    public AccountHolder() {    }

    public AccountHolder(String name, String password, Address primaryAddress, String mailAddress, LocalDate dateOfBirth) {
        super(name, password);
        setPrimaryAddress(primaryAddress);
        setMailAddress(mailAddress);
        setDateOfBirth(dateOfBirth);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
       this.dateOfBirth = dateOfBirth;
    }

    public List<Account> getPrimaryAccountList() {
        return primaryAccountList;
    }

    public void setPrimaryAccountList(List<Account> primaryAccountList) {
        this.primaryAccountList = primaryAccountList;
    }

    public List<Account> getSecondaryAccountList() {
        return secondaryAccountList;
    }

    public void setSecondaryAccountList(List<Account> secondaryAccountList) {
        this.secondaryAccountList = secondaryAccountList;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }
}
