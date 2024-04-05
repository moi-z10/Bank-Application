package com.Bank2.Bank2.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class AccBalance {
    @Id
    private long accId;
    private Double balance;
//    @OneToOne(mappedBy = "accBalance")
//    @JsonBackReference
//    private CustMap custMap;
//    @OneToMany(mappedBy = "accBalance")
//    @JsonBackReference
//    private List<AccTransacions> accTransacions;

}