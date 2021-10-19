package com.transfer.MoneyC2C.model;
import java.util.Random;

public class TestModels {

    private final String cardFromStubNumber = "1111222233334444";
    private final String cardFromInvalidStubNumber = "111dsd1222233334444";

    private final int cardStubBalance = 1000000;

    private final String cardStubCVV = "555";
    private final String invalidCardStubCVV = "5555";
    private final String incorrectCardStubCVV = "345";

    private final String cardStubExpires = "05/25";
    private final String cardStubIsExpired = "08/21";
    private final String cardStubIncorrectExpires = "07/25";
    private final String cardStubCurrency = "RUR";

    private final String cardToStubNumber = "1111111111111111";

    private final String operationIdStub = "1";
    private final String operationInvalidIdStub = "invalid";

    private final String confirmationCodeStub = ConfirmationCodeSystemStub.CONFIRMATION_CODE_STUB;
    private final String confirmationInvalidCodeStub = "invalid";

    private Random rand = new Random();

    private int cardStubAmountValue = rand.nextInt(cardStubBalance) + 1;
    private final Amount amount = new Amount(cardStubAmountValue, cardStubCurrency);

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
