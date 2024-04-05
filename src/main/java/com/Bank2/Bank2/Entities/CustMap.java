package com.Bank2.Bank2.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CustMap {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Cust_Id")
    Cust_Details custDetails;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="accId")
    AccBalance accBalance;
}