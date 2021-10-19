package com.transfer.MoneyC2C.repository;

import com.transfer.MoneyC2C.model.Card;
import com.transfer.MoneyC2C.model.ConfirmationEntity;
import com.transfer.MoneyC2C.model.Operation;
import com.transfer.MoneyC2C.model.TestModels;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TransferRepositoryTest {
    private final TestModels testModels = new TestModels();
    private final Operation operation = testModels.getOperation();
    private final ConfirmationEntity confirmation = testModels.getConfirmationEntity();

    @Test
    public void transferMoney_test() {
        TransferRepository repository = new TransferRepository();
        Optional<String> actual = repository.transferMoney(operation);
        assertEquals(of(operation.getOperationId()), actual);
    }

    @Test
    public void transferMoney_test_card_not_in_base() {
        TransferRepository repository = new TransferRepository();
        Optional<String> actual = repository.transferMoney(testModels.getUnknownCardFromNumberOperation());
        assertEquals(Optional.empty(), actual);
    }

    @Test
    public void transferMoney_confirmation_code_generated_test() {
        TransferRepository repository = new TransferRepository();
        Optional<String> actual = repository.transferMoney(operation);
        assertNotNull(operation.getCode());
    }

    @Test
    public void transferMoney_test_cardFrom_blank() {
        TransferRepository repository = new TransferRepository();
        Optional<String> actual = repository.transferMoney(testModels.getCardFromBlankNumberOperation());
        assertEquals(Optional.empty(), actual);
    }

    @Test
    public void transferMoney_test_cardFrom_incorrect_CVV() {
        TransferRepository repository = new TransferRepository();
        Optional<String> actual = repository.transferMoney(testModels.getCardFromIncorrectCVVOperation());
        assertEquals(Optional.empty(), actual);
    }

    @Test
    public void transferMoney_test_cardFrom_incorrect_expires() {
        TransferRepository repository = new TransferRepository();
        Optional<String> actual = repository.transferMoney(testModels.getCardIncorrectExpiresOperation());
        assertEquals(Optional.empty(), actual);
    }

    @Test
    public void transferMoney_test_cardFrom_invalid_amount() {
        TransferRepository repository = new TransferRepository();
        Optional<String> actual = repository.transferMoney(testModels.getInvalidAmountOperation());
        assertEquals(Optional.empty(), actual);
    }

    @Test
    public void transferMoney_test_cardFrom_balance_less_then_amount() {
        TransferRepository repository = new TransferRepository();
        Optional<String> actual = repository.transferMoney(testModels.getBiggerThenBalanceAmountOperation());
        assertEquals(Optional.empty(), actual);
    }

    @Test
    public void confirmOperation_test() {
        TransferRepository repository =  new TransferRepository();
        repository.transferMoney(operation);
        Optional<String> actual = repository.confirmOperation(operation.getOperationId(), confirmation.getCode());
        assertEquals(of(operation.getOperationId()), actual);
    }

    @Test
    public void confirmOperation_test_operation_not_in_base() {
        TransferRepository repository =  new TransferRepository();
        Optional<String> actual = repository.confirmOperation(operation.getOperationId(), confirmation.getCode());
        assertEquals(Optional.empty(), actual);
    }

    @Test
    public void confirmOperation_test_invalid_code() {
        ConfirmationEntity confirmation = testModels.getConfirmationInvalidCodeEntity();
        TransferRepository repository =  new TransferRepository();
        repository.transferMoney(operation);
        Optional<String> actual = repository.confirmOperation(operation.getOperationId(), confirmation.getCode());
        assertEquals(Optional.empty(), actual);
    }

    @Test
    public void confirmOperation_test_balance() {
        TransferRepository repository =  new TransferRepository();
        Card cardFrom = repository.getCardBaseStub().get(operation.getCardFromNumber());
        int cardFromExpectedBalance = cardFrom.getBalance() - (operation.getAmount().getValue() + (int) (operation.getAmount().getValue() * Operation.COMMISSION_RATE));
        repository.transferMoney(operation);

        Card cardTo = repository.getCardBaseStub().get(operation.getCardToNumber());
        int cardToExpectedBalance = cardTo.getBalance() + (operation.getAmount().getValue());
        repository.confirmOperation(operation.getOperationId(), confirmation.getCode());
        assertEquals(cardFrom.getBalance(), cardFromExpectedBalance);
        assertEquals(cardTo.getBalance(), cardToExpectedBalance);
    }

    @Test
    public void confirmOperation_test_CVV_updated() {
        TransferRepository repository =  new TransferRepository();
        repository.transferMoney(operation);
        repository.confirmOperation(operation.getOperationId(), confirmation.getCode());
        String CVVBefore = repository.getCardBaseStub().get(operation.getCardToNumber()).getCVV();
        Operation reversedOperation = testModels.getReversedOperation();
        repository.transferMoney(reversedOperation);
        repository.confirmOperation(reversedOperation.getOperationId(), confirmation.getCode());
        String CVVAfter = repository.getCardBaseStub().get(reversedOperation.getCardToNumber()).getCVV();
        assertEquals(CVVBefore, Card.DEFAULT_CVV);
        assertEquals(CVVAfter, reversedOperation.getCardFromCVV());
    }

    @Test
    public void confirmOperation_test_expires_updated() {
        TransferRepository repository =  new TransferRepository();
        repository.transferMoney(operation);
        repository.confirmOperation(operation.getOperationId(), confirmation.getCode());
        String expiresBefore = repository.getCardBaseStub().get(operation.getCardToNumber()).getExpires();
        Operation reversedOperation = testModels.getReversedOperation();
        repository.transferMoney(reversedOperation);
        repository.confirmOperation(reversedOperation.getOperationId(), confirmation.getCode());
        String expiresAfter = repository.getCardBaseStub().get(reversedOperation.getCardToNumber()).getExpires();
        assertEquals(expiresBefore, Card.DEFAULT_EXPIRES);
        assertEquals(expiresAfter, reversedOperation.getCardFromValidTill());
    }

}
