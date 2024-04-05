package com.Bank2.Bank2.Exception;

import lombok.Data;

@Data
public class IdNotFoundException extends Exception{
    private String reason;

    public IdNotFoundException(String msg, String reason){
        super(msg);
        this.reason=reason;
    }
}

