package com.transfer.MoneyC2C.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class Amount {

    public Amount(int value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    @Positive
    @Min(value = 1, message = "Transfer amount should be greater than 1")
    private int value;

    @NotBlank(message = "Amount currency can't be blank")
    private String currency;

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("{\"currency\": \"%s\",\"value\": %d}", currency, value);
    }
}

