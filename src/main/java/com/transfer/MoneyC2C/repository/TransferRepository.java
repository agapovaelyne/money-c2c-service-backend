package com.transfer.MoneyC2C.repository;

import com.transfer.MoneyC2C.model.Card;
import com.transfer.MoneyC2C.model.ConfirmationCodeSystemStub;
import com.transfer.MoneyC2C.model.Operation;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransferRepository {

    ConfirmationCodeSystemStub confirmationSystem = new ConfirmationCodeSystemStub();

    private Map<String, Card> cardBaseStub = new ConcurrentHashMap<>();
    private Map<String, Operation> operationBaseStub = new ConcurrentHashMap<>();

    //stub card parameters for testing without db
    private String cardFromStubNumber = "1111222233334444";
    private int cardStubBalance = 1000000;
    private String cardStubCVV = "555";
    private String cardStubExpires = "05/25";
    private String cardStubCurrency = "RUR";
    private final Card stubCard = new Card(cardFromStubNumber, cardStubBalance, cardStubCVV, cardStubExpires);

    public TransferRepository() {
        //add stub card for testing without db
        cardBaseStub.put(cardFromStubNumber, stubCard);
    }


    public Optional<String> transferMoney(Operation operation) {
        Card cardFrom = cardBaseStub.get(operation.getCardFromNumber());
        if (cardFrom == null) {
            //cardBaseStub.put(operation.getCardFromNumber(), (new Card(operation.getCardToNumber(), cardStubBalance)));
            //or if we want to make operation only for cardFrom that is already in base (means exists):
            return Optional.empty();
        }

        if ((!cardFrom.getCVV().equals(operation.getCardFromCVV()) && cardFrom.getCVV() != Card.DEFAULT_CVV) ||
                (!cardFrom.getExpires().equals(operation.getCardFromValidTill()) && cardFrom.getExpires() != Card.DEFAULT_EXPIRES) ||
                cardFrom.getBalance() < operation.getAmount().getValue()
        ) {
            return Optional.empty();
        }

        if (cardFrom.getCVV().equals(Card.DEFAULT_CVV)) {
            cardFrom.setCVV(operation.getCardFromCVV());
        }

        if (cardFrom.getExpires().equals(Card.DEFAULT_EXPIRES)) {
            cardFrom.setExpires(operation.getCardFromValidTill());
        }

        if (!cardBaseStub.containsKey(operation.getCardToNumber())) {
            cardBaseStub.put(operation.getCardToNumber(), new Card(operation.getCardToNumber(), 0));
        }

        operationBaseStub.put(operation.getOperationId(), operation);
        confirmationSystem.generateCode(operation);
        return Optional.of(operation.getOperationId());
    }


    public Optional<String> confirmOperation(String operationId, String code) {

        Operation operation = operationBaseStub.get(operationId);

        if (operation == null || !code.equals(operation.getCode())) {
            return Optional.empty();
        }

//        System.out.println(String.format(
//                "card %s\nbalance %d\n" +
//                        "card %s\nbalance %d\n"
//                , operation.getCardFromNumber(), cardBaseStub.get(operation.getCardFromNumber()).getBalance(),
//                operation.getCardToNumber(), cardBaseStub.get(operation.getCardToNumber()).getBalance()));

        cardBaseStub.get(operation.getCardFromNumber()).changeBalance((operation.getAmount().getValue() + (int) (operation.getAmount().getValue() * Operation.COMMISSION_RATE)) * (-1));
        cardBaseStub.get(operation.getCardToNumber()).changeBalance(operation.getAmount().getValue());

//        System.out.println(String.format(
//                "transfer money from card %s\nto card %s \n", operation.getCardFromNumber(), operation.getCardToNumber()));
//
//        System.out.println(String.format(
//                "card %s\nbalance %d\n" +
//                        "card %s\nbalance %d\n"
//                , operation.getCardFromNumber(), cardBaseStub.get(operation.getCardFromNumber()).getBalance(),
//                operation.getCardToNumber(), cardBaseStub.get(operation.getCardToNumber()).getBalance()));
        return Optional.of(operationId);
    }

    public Map<String, Card> getCardBaseStub() {
        return cardBaseStub;
    }
}