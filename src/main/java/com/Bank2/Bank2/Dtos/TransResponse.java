package com.Bank2.Bank2.Dtos;

import lombok.Data;

@Data

public class TransResponse {
    private  String message;
    private String statusCode;
    private long transRefId;
    public TransResponse(String message,String statusCode,long transRefId){
        this.message=message;
        this.statusCode=statusCode;
        this.transRefId=transRefId;
    }
}
