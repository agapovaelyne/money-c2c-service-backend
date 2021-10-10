package com.transfer.MoneyC2C.controller;

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
//@CrossOrigin(origins = "https://serp-ya.github.io,http://localhost:3000")
@CrossOrigin(origins = "*")
@RequestMapping()
@Validated
public class TransferController {
    TransferService service;
    private final Logger logger = Logger.getLogger(TransferController.class);

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping(value = "/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    public String transferMoney(@Valid @RequestBody Operation operation) {
        return service.transferMoney(operation);
    }

    @PostMapping(value = "/confirmOperation", produces = MediaType.APPLICATION_JSON_VALUE)
    public String confirmOperation(@Valid @RequestBody ConfirmationEntity confirmation) {
        return service.confirmOperation(confirmation);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<String> handleRunTimeExc(MethodArgumentNotValidException exc) {
        logger.error(exc.getLocalizedMessage());
        return handleRunTimeExc(new InvalidData(exc.getLocalizedMessage()));
    }

    @ExceptionHandler(InvalidData.class)
    ResponseEntity<String> handleRunTimeExc(InvalidData exc) {
        String responseText = String.format(
                "{\"message\" : \"Error input data: %s\"," +
                        "\"id\" : %d}",
                exc.getLocalizedMessage(), exc.getId()
        );
        logger.error("Error " + responseText);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(responseText);
    }

    @ExceptionHandler(TransferError.class)
    ResponseEntity<String> handleRunTimeExc(TransferError exc) {
        String responseText = String.format(
                "{\"message\" : \"Error input data: %s\"," +
                        "\"id\" : %d}",
                exc.getLocalizedMessage(), exc.getId()
        );
        logger.error("Error " + responseText);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(responseText);
    }
}
