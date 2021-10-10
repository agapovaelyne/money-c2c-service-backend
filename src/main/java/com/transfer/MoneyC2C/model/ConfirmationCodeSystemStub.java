package com.transfer.MoneyC2C.model;

public class ConfirmationCodeSystemStub {
    public static final String CONFIRMATION_CODE_STUB = "0000";

    public void generateCode(Operation operation) {
        operation.setCode(CONFIRMATION_CODE_STUB); //it should be random, but front uses the stub
    }
}
