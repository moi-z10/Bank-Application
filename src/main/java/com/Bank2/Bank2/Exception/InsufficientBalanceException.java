package com.Bank2.Bank2.Exception;

public class InsufficientBalanceException extends Exception {
    private final String errorMessage;
    private final String reasonCode;

    public InsufficientBalanceException(String errorMessage, String reasonCode) {
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
