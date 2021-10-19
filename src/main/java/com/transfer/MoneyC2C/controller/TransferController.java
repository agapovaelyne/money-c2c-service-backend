package com.transfer.MoneyC2C.controller;

import com.transfer.MoneyC2C.dto.MoneyTransferExceptionDTO;
import com.transfer.MoneyC2C.dto.MoneyTransferResponseDTO;
import com.transfer.MoneyC2C.exception.InvalidData;
import com.transfer.MoneyC2C.exception.TransferError;
import com.transfer.MoneyC2C.model.ConfirmationEntity;
import com.transfer.MoneyC2C.model.Operation;
import com.transfer.MoneyC2C.service.TransferService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping()
@Validated
public class TransferController {
    private final TransferService service;
    private final Logger logger = Logger.getLogger(TransferController.class);

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping(value = "/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MoneyTransferResponseDTO> transferMoney(@Valid @RequestBody Operation operation) {
        return new ResponseEntity<>(service.transferMoney(operation),HttpStatus.OK);
    }

    @PostMapping(value = "/confirmOperation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MoneyTransferResponseDTO> confirmOperation(@Valid @RequestBody ConfirmationEntity confirmation) {
        return new ResponseEntity<>(service.confirmOperation(confirmation),HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<MoneyTransferExceptionDTO> handleRunTimeExc(MethodArgumentNotValidException exc) {
        return handleRunTimeExc(new InvalidData(exc.getLocalizedMessage()));
    }

    @ExceptionHandler(InvalidData.class)
    ResponseEntity<MoneyTransferExceptionDTO> handleRunTimeExc(InvalidData exc) {
        logger.error("Error " + exc.toString());
        return new ResponseEntity<>(new MoneyTransferExceptionDTO(exc), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransferError.class)
    ResponseEntity<MoneyTransferExceptionDTO> handleRunTimeExc(TransferError exc) {
        logger.error("Error " + exc.toString());
        return new ResponseEntity<>(new MoneyTransferExceptionDTO(exc), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
