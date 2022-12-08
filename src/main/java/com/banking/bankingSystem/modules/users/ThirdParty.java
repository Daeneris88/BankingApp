package com.banking.bankingSystem.modules.users;
import jakarta.persistence.Entity;
import java.util.Hashtable;

@Entity
public class ThirdParty extends User {
    private Hashtable<Integer, String> hashKey = new Hashtable<>();

    public ThirdParty() {    }

}
