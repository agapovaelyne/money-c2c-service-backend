package com.transfer.MoneyC2C.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Operation {

    private static int operationsCount = 0;

    private static final String CARD_NUMBER_FORMAT_REGEXP = "^[0-9]{16}$";
    private static final String CARD_CVV_NUMBER_FORMAT_REGEXP = "^[0-9]{3}$";
    private static final String CARD_EXPIRES_FORMAT_REGEXP = "^[0-9]{2}/[0-9]{2}$";

    public static final double COMMISSION_RATE = 0.01;

    @NotBlank(message = "Card number can't be blank")
    @NotNull(message = "Card number can't be null")
    @Pattern(regexp = CARD_NUMBER_FORMAT_REGEXP, message = "Invalid card number")
    private String cardFromNumber;

    @NotBlank
    @Pattern(regexp = CARD_CVV_NUMBER_FORMAT_REGEXP, message = "Invalid CVV number")
    private String cardFromCVV;

    @NotBlank(message = "Card number can't be blank")
    @Pattern(regexp = CARD_EXPIRES_FORMAT_REGEXP, message = "Invalid valid-till-date")
    private String cardFromValidTill;

    @NotBlank(message = "Card number can't be blank")
    @Pattern(regexp = CARD_NUMBER_FORMAT_REGEXP, message = "Invalid card number")
    private String cardToNumber;

    @NotNull(message = "Card number can't be null")
    private Amount amount;

    private String operationId;
    private String code;


    public Operation(String cardFromNumber, String cardFromCVV, String cardFromValidTill, String cardToNumber, Amount amount) {
        this.cardFromNumber = cardFromNumber;
        this.cardFromCVV = cardFromCVV;
        this.cardFromValidTill = cardFromValidTill;
        this.cardToNumber = cardToNumber;
        this.amount = amount;
        this.operationId = String.valueOf(++operationsCount);
    }

    public String getOperationId() {
        return operationId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCardFromNumber() {
        return cardFromNumber;
    }

    public String getCardFromCVV() {
        return cardFromCVV;
    }

    public String getCardFromValidTill() {
        return cardFromValidTill;
    }

    public String getCardToNumber() {
        return cardToNumber;
    }

    public Amount getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("{\"cardFromNumber\": \"%s\",\"cardToNumber\": \"%s\",\"cardFromCVV\": \"%s\",\"cardFromValidTill\": \"%s\",\"amount\": %s}", cardFromNumber, cardToNumber, cardFromCVV, cardFromValidTill, amount != null ? amount.toString() : "null");
    }
}

