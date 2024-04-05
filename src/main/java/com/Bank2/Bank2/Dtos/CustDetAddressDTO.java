package com.Bank2.Bank2.Dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustDetAddressDTO {
    private String name;
    private long phone;
    private String email;
    private String country;

    private String city;
    private String addressLane;
    private long pin;

    public CustDetAddressDTO(String name, long phone, String email, String country, String city, String addressLane, long pin) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.country = country;
        this.city = city;
        this.addressLane = addressLane;
        this.pin = pin;
    }

    public CustDetAddressDTO() {
    }
}
