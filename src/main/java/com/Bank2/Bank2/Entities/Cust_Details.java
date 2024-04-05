package com.Bank2.Bank2.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Cust_Details {
    @Id
    private Long Cust_Id;
    @Column(name = "name")
    private String name;
    @Column
    private Long phone;
    @Column
    private String email;
    @Column
    private Timestamp created;
    @Column
    private Timestamp lastUpdated;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    Cust_Address custAddress;
    @OneToOne(mappedBy = "custDetails")
    @JsonBackReference
    private CustMap custMap;
}
