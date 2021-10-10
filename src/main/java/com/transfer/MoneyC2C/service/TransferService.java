package com.transfer.MoneyC2C.service;

import com.transfer.MoneyC2C.exception.InvalidData;
import com.transfer.MoneyC2C.exception.TransferError;
import com.transfer.MoneyC2C.model.Card;
import com.transfer.MoneyC2C.model.ConfirmationEntity;
import com.transfer.MoneyC2C.model.Operation;
import com.transfer.MoneyC2C.repository.TransferRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    TransferRepository repository;
    private final Logger logger = Logger.getLogger(TransferService.class);

    public TransferService(TransferRepository repository) {
        this.repository = repository;
    }

    public String transferMoney(Operation operation) {
        try {
            if (operation.getCardToNumber().equals(operation.getCardFromNumber())) {
                throw new InvalidData(String.format("Can't transfer money to the same card: %s -> %s", operation.getCardFromNumber(), operation.getCardToNumber()));
            }

            if (Card.isExpired(operation.getCardFromValidTill())) {
                throw new InvalidData(String.format("Card %s expired or invalid format of date provided", operation.getCardFromNumber()));
            }

            String operationId = repository.transferMoney(operation).orElseThrow(() -> new TransferError("Can't transfer money from card " + operation.getCardFromNumber()));
            logger.info("Operation " + operation);
            return String.format("{\"operationId\" : \"%s\"}", operationId);
        } catch (NullPointerException ex) {
            throw new InvalidData(ex.getLocalizedMessage());
        }
    }

    public String confirmOperation(ConfirmationEntity confirmation) {
        try {
            String operationId = confirmation.getOperationId();
            String code = confirmation.getCode();

            String operationResult = repository.confirmOperation(operationId, code).orElseThrow(() -> new TransferError(String.format("Can't transfer money for operation %s", operationId)));
            logger.info(String.format("Operation confirmed {operationId=%s}", operationResult));
            return String.format("{\"operationId\" : \"%s\"}", operationResult);
        } catch (NullPointerException ex) {
            throw new InvalidData(ex.getLocalizedMessage());
        }
    }
}
