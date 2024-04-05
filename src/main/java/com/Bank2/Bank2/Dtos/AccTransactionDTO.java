package com.Bank2.Bank2.Dtos;

import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;

@Data
public class AccTransactionDTO {
    private Long transactionId;
    private Long transRefId;
    private Double credit;
    private Double debit;
    private Double availBalance;
    private Timestamp lastUpdate;
    private Long accId;
}
