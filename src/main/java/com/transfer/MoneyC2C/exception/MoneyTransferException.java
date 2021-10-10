package com.transfer.MoneyC2C.exception;

public class MoneyTransferException extends RuntimeException {
    private static int errorCounter = 0;
    private int id;

    public MoneyTransferException(String msg) {
        super(msg);
        id = ++errorCounter;
    }

    public int getId() {
        return id;
    }
}
