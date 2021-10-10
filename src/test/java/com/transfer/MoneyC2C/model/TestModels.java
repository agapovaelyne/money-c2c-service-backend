package com.transfer.MoneyC2C.model;
import java.util.Random;

public class TestModels {

    private String cardFromStubNumber = "1111222233334444";
    private String cardFromInvalidStubNumber = "111dsd1222233334444";

    private int cardStubBalance = 1000000;

    private String cardStubCVV = "555";
    private String invalidCardStubCVV = "5555";
    private String incorrectCardStubCVV = "345";

    private String cardStubExpires = "05/25";
    private String cardStubIsExpired = "08/21";
    private String cardStubIncorrectExpires = "07/25";
    private String cardStubCurrency = "RUR";

    private String cardToStubNumber = "1111111111111111";

    private String operationIdStub = "1";
    private String operationInvalidIdStub = "invalid";

    private String confirmationCodeStub = ConfirmationCodeSystemStub.CONFIRMATION_CODE_STUB;
    private String confirmationInvalidCodeStub = "invalid";

    private Random rand = new Random();

    private int cardStubAmountValue = rand.nextInt(cardStubBalance) + 1;
    private Amount amount = new Amount(cardStubAmountValue, cardStubCurrency);

    public Operation getOperation() {
        return new Operation(cardFromStubNumber, cardStubCVV, cardStubExpires, cardToStubNumber, amount);
    }

    public Operation getReversedOperation() {
        return new Operation(cardToStubNumber, cardStubCVV, cardStubExpires, cardFromStubNumber, amount);
    }

    public Operation getCardExpiredOperation() {
        return new Operation(cardFromStubNumber, cardStubCVV, cardStubIsExpired, cardToStubNumber, amount);
    }

    public Operation getCardIncorrectExpiresOperation() {
        return new Operation(cardFromStubNumber, cardStubCVV, cardStubIncorrectExpires, cardToStubNumber, amount);
    }

    public Operation getCardFromInvalidNumberOperation() {
        return new Operation(cardFromInvalidStubNumber, cardStubCVV, cardStubExpires, cardToStubNumber, amount);
    }

    public Operation getCardFromInvalidCVVOperation() {
        return new Operation(cardFromInvalidStubNumber, invalidCardStubCVV, cardStubExpires, cardToStubNumber, amount);
    }

    public Operation getCardFromIncorrectCVVOperation() {
        return new Operation(cardFromStubNumber, incorrectCardStubCVV, cardStubExpires, cardToStubNumber, amount);
    }

    public Operation getCardFromBlankNumberOperation() {
        return new Operation("", cardStubCVV, cardStubExpires, cardToStubNumber, amount);
    }

    public Operation getCardFromNullNumberOperation() {
        return new Operation(null, cardStubCVV, cardStubExpires, cardToStubNumber, amount);
    }

    public Operation getUnknownCardFromNumberOperation() {
        return new Operation("", cardStubCVV, cardStubExpires, cardToStubNumber, amount);
    }

    public Operation getNullAmountOperation() {
        return new Operation(cardFromInvalidStubNumber, cardStubCVV, cardStubExpires, cardToStubNumber, null);
    }

    public Operation getInvalidAmountOperation() {
        Amount amount = new Amount(-100, cardStubCurrency);
        return new Operation(cardFromInvalidStubNumber, cardStubCVV, cardStubExpires, cardToStubNumber, amount);
    }

    public Operation getBiggerThenBalanceAmountOperation() {
        Amount amount = new Amount(cardStubBalance + 1000, cardStubCurrency);
        return new Operation(cardFromInvalidStubNumber, cardStubCVV, cardStubExpires, cardToStubNumber, amount);
    }

    public Operation getSameCardOperation() {
        return new Operation(cardFromInvalidStubNumber, cardStubCVV, cardStubExpires, cardFromInvalidStubNumber, amount);
    }

    public ConfirmationEntity getConfirmationEntity() {
        return new ConfirmationEntity(operationIdStub, confirmationCodeStub);
    }

    public ConfirmationEntity getConfirmationInvalidIdEntity() {
        return new ConfirmationEntity(operationInvalidIdStub, confirmationCodeStub);
    }

    public ConfirmationEntity getConfirmationInvalidCodeEntity() {
        return new ConfirmationEntity(operationIdStub, confirmationInvalidCodeStub);
    }
}
