package com.transfer.MoneyC2C.dto;

import com.transfer.MoneyC2C.exception.MoneyTransferException;

public class MoneyTransferExceptionDTO {
    private String message;
    private int id;

    public MoneyTransferExceptionDTO(MoneyTransferException exception) {
        this.message = exception.getLocalizedMessage();
        this.id = exception.getId();
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(int id) {
        this.id = id;
    }
}
