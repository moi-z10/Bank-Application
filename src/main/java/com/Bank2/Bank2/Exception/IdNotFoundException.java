package com.Bank2.Bank2.Exception;

import lombok.Data;

public class IdNotFoundException extends Exception {
    private final String errorMessage;
    private final String reasonCode;

    public IdNotFoundException(String errorMessage, String reasonCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.reasonCode = reasonCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getReasonCode() {
        return reasonCode;
    }
}


