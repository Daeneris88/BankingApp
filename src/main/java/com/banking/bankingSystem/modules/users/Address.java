package com.banking.bankingSystem.modules.users;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String streetAddress;
    private String city;
    private String postalCode;

}
