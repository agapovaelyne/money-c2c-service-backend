package com.transfer.MoneyC2C.logger;

import com.transfer.MoneyC2C.model.ConfirmationEntity;
import com.transfer.MoneyC2C.model.Operation;
import com.transfer.MoneyC2C.model.TestModels;
import com.transfer.MoneyC2C.repository.TransferRepository;
import com.transfer.MoneyC2C.service.TransferService;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.File;
import java.io.IOException;

import static java.util.Optional.of;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class LoggerTest {
    private final String logFile = "log/money-backend.log";
    private ReversedLinesFileReader reader;

    TestModels testModels = new TestModels();
    Operation operation = testModels.getOperation();
    ConfirmationEntity confirmation = testModels.getConfirmationEntity();

    public LoggerTest() throws IOException {
    }

    private String getLastLine() throws IOException {
        reader = new ReversedLinesFileReader(new File(logFile));
        return reader.readLine();
    }

    @Test
    public void new_operation_log_test() throws IOException {
        String expectedLogMessage = "Operation " + operation.toString();
        String expectedLogLevel = "INFO";
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);
        when(repositoryMock.transferMoney(operation)).thenReturn(of(operation.getOperationId()));
        service.transferMoney(operation);
        String logLastLine = this.getLastLine();
        assertTrue(logLastLine.startsWith(expectedLogLevel));
        assertTrue(logLastLine.contains(expectedLogMessage));
    }

    @Test
    public void confirmation_log_test() throws IOException {
        String expectedLogMessage = String.format("Operation confirmed {operationId=%s}", operation.getOperationId());
        String expectedLogLevel = "INFO";
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);
        when(repositoryMock.confirmOperation(confirmation.getOperationId(), confirmation.getCode())).thenReturn(of(operation.getOperationId()));
        service.confirmOperation(confirmation);
        String logLastLine = this.getLastLine();
        assertTrue(logLastLine.startsWith(expectedLogLevel));
        assertTrue(logLastLine.contains(expectedLogMessage));
    }
}
