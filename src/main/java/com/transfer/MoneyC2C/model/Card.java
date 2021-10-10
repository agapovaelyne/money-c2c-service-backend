package com.transfer.MoneyC2C.model;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Card {
    private String number;
    private AtomicInteger balance = new AtomicInteger(0);
    private String currency;

    private String CVV;
    private String expires;

    public static final String DEFAULT_CURRENCY = "RUR";
    public static final String DEFAULT_CVV = "000";
    public static final String DEFAULT_EXPIRES = "11/11";

    public Card(String number, int balance, String CVV, String expires) {
        this.number = number;
        this.balance.set(balance);
        this.CVV = CVV;
        this.expires = expires;
        this.currency = DEFAULT_CURRENCY;
    }

    public Card(String number, int balance) {
        this.number = number;
        this.balance.set(balance);
        this.CVV = DEFAULT_CVV;
        this.expires = DEFAULT_EXPIRES;
    }

    public int getBalance() {
        return balance.get();
    }

    public void changeBalance(int value) {
        balance.getAndAdd(value);
    }

    public String getCVV() {
        return CVV;
    }

    public String getExpires() {
        return expires;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public static boolean isExpired(String expires) {
        LocalDate currentDate = LocalDate.now();
        int expMonth = Integer.parseInt(expires.substring(0, 2));
        int expYear = Integer.parseInt(expires.substring(3));
        int curYear = currentDate.getYear() % 1000;

        if (expMonth < 0 || expMonth > 12 || expYear < curYear) {
            return true;
        }

        if (expYear == curYear && expMonth < currentDate.getMonthValue()) {
            return true;
        }
        return false;
    }

}
