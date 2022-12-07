package com.banking.bankingSystem.modules.users;
import jakarta.persistence.Entity;

@Entity
public class Admin extends User {
    public Admin() {    }
    public Admin(String name, String password) {
        super(name, password);
    }
}
