package com.Bank2.Bank2.Entities;

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
public class Cust_Address {
    @Id
    private Long address_Id;
    @Column
    private String country;
    @Column
    private String city;
    @Column
    private String address_Lane;
    @Column
    private Long pin;
    @Column
    private Timestamp lastUpdate;

//    @OneToOne(mappedBy = "custAddress")
//    private Cust_Details custDetails;
}

