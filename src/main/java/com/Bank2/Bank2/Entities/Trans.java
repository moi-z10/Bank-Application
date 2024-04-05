package com.Bank2.Bank2.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trans {
    private Long accid;
    private Long toAccid;
    private String type;
    private double amount;
}
