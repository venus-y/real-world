package com.example.realworld.common;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    private String street;
    private String city;
    private String zipcode;

    public static Address toAddress(String street, String city, String zipcode) {
        Address address = new Address();
        address.street = street;
        address.city = city;
        address.zipcode = zipcode;
        return address;
    }

    @Override
    public String toString() {
        return city + " " + street;
    }


}
