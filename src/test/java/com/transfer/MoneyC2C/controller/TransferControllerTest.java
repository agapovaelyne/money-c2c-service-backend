package com.transfer.MoneyC2C.controller;

import com.transfer.MoneyC2C.exception.InvalidData;
import com.transfer.MoneyC2C.exception.TransferError;
import com.transfer.MoneyC2C.model.ConfirmationEntity;
import com.transfer.MoneyC2C.model.Operation;
import com.transfer.MoneyC2C.model.TestModels;
import com.transfer.MoneyC2C.repository.TransferRepository;
import com.transfer.MoneyC2C.service.TransferService;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static java.util.Optional.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransferControllerTest {

    TestModels testModels = new TestModels();
    Operation operation = testModels.getOperation();
    ConfirmationEntity confirmation = testModels.getConfirmationEntity();

    @Test
    public void transferMoney_test_service_mocked() {
        String expected = "Service result";
        TransferService serviceMock = mock(TransferService.class);
        when(serviceMock.transferMoney(any())).thenReturn(expected);
        TransferController controller = new TransferController(serviceMock);
        String actual = controller.transferMoney(operation);
        assertEquals(expected, actual);
    }

    @Test
    public void transferMoney_test_repository_mocked() {
        String result = "Repository result";
        String expected = String.format("{\"operationId\" : \"%s\"}", result);
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);
        TransferController controller = new TransferController(service);
        when(repositoryMock.transferMoney(operation)).thenReturn(of(result));
        String actual = controller.transferMoney(operation);
        assertEquals(expected, actual);
    }

    @Test
    public void transferMoney_test_nothing_mocked_valid_request() {
        TransferRepository repository = new TransferRepository();
        TransferService service = new TransferService(repository);
        TransferController controller = new TransferController(service);
        String actual = controller.transferMoney(operation);
        String expected = String.format("{\"operationId\" : \"%s\"}", operation.getOperationId());
        assertEquals(expected, actual);
    }

    @Test
    public void transferMoney_test_invalid_request_card_expired() {
        Operation operation = testModels.getCardExpiredOperation();
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService serviceSpy = spy(new TransferService(repositoryMock));
        TransferController controller = new TransferController(serviceSpy);
        String expected = String.format("Card %s expired or invalid format of date provided", operation.getCardFromNumber());

        InvalidData result = assertThrows(InvalidData.class,
                () -> controller.transferMoney(operation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void transferMoney_test_invalid_request_null_operation() {
        Operation operation = mock(Operation.class);
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService serviceSpy = spy(new TransferService(repositoryMock));
        TransferController controller = new TransferController(serviceSpy);
        Class<InvalidData> expected = InvalidData.class;

        InvalidData result = assertThrows(InvalidData.class,
                () -> controller.transferMoney(operation)
        );
        assertEquals(expected, result.getClass());
    }

    @Test
    public void transferMoney_test_invalid_request_nullCardFrom_operation() {
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService serviceSpy = spy(new TransferService(repositoryMock));
        TransferController controller = new TransferController(serviceSpy);
        Class<TransferError> expected = TransferError.class;

        TransferError result = assertThrows(TransferError.class,
                () -> controller.transferMoney(testModels.getCardFromNullNumberOperation())
        );
        assertEquals(expected, result.getClass());
    }

    @Test
    public void transferMoney_test_invalid_request_same_card() {
        Operation operation = testModels.getSameCardOperation();
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService serviceSpy = spy(new TransferService(repositoryMock));
        TransferController controller = new TransferController(serviceSpy);

        String expected = String.format("Can't transfer money to the same card: %s -> %s", operation.getCardFromNumber(), operation.getCardToNumber());

        InvalidData result = assertThrows(InvalidData.class,
                () -> controller.transferMoney(operation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void transferMoney_test_invalid_request_null_amount() {
        Operation operation = testModels.getNullAmountOperation();
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService serviceSpy = spy(new TransferService(repositoryMock));
        TransferController controller = new TransferController(serviceSpy);
        String expected = String.format("Can't transfer money from card %s", operation.getCardFromNumber());

        TransferError result = assertThrows(TransferError.class,
                () -> controller.transferMoney(operation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void transferMoney_test_invalidAmount_request() {
        //if spring validation doesn't work
        Operation operation = testModels.getInvalidAmountOperation();
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService serviceSpy = spy(new TransferService(repositoryMock));
        TransferController controller = new TransferController(serviceSpy);
        String expected = String.format("Can't transfer money from card %s", operation.getCardFromNumber());

        TransferError result = assertThrows(TransferError.class,
                () -> controller.transferMoney(operation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void transferMoney_test_invalid_cardFromCVV_request() {
        //if spring validation doesn't work
        Operation operation = testModels.getCardFromInvalidCVVOperation();
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService serviceSpy = spy(new TransferService(repositoryMock));
        TransferController controller = new TransferController(serviceSpy);
        String expected = String.format("Can't transfer money from card %s", operation.getCardFromNumber());

        TransferError result = assertThrows(TransferError.class,
                () -> controller.transferMoney(operation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void transferMoney_test_invalid_request_invalid_cardTo() {
        //if spring validation doesn't work
        Operation operation = testModels.getCardFromInvalidNumberOperation();
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService serviceSpy = spy(new TransferService(repositoryMock));
        TransferController controller = new TransferController(serviceSpy);
        String expected = String.format("Can't transfer money from card %s", operation.getCardFromNumber());

        TransferError result = assertThrows(TransferError.class,
                () -> controller.transferMoney(operation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void transferMoney_test_invalid_request_blank_cardTo() {
        //if spring validation doesn't work
        Operation operation = testModels.getCardFromBlankNumberOperation();
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService serviceSpy = spy(new TransferService(repositoryMock));
        TransferController controller = new TransferController(serviceSpy);
        String expected = String.format("Can't transfer money from card %s", operation.getCardFromNumber());

        TransferError result = assertThrows(TransferError.class,
                () -> controller.transferMoney(operation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void confirmOperation_test_service_mocked() {
        String expected = "Service result";
        TransferService serviceMock = mock(TransferService.class);
        when(serviceMock.confirmOperation(any())).thenReturn(expected);
        TransferController controller = new TransferController(serviceMock);
        String actual = controller.confirmOperation(confirmation);
        assertEquals(expected, actual);
    }


    @Test
    public void confirmOperation_test_repository_mocked() {
        String result = "Repository result";
        String expected = String.format("{\"operationId\" : \"%s\"}", result);
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);
        TransferController controller = new TransferController(service);
        when(repositoryMock.confirmOperation(confirmation.getOperationId(), confirmation.getCode())).thenReturn(of(result));
        String actual = controller.confirmOperation(confirmation);
        assertEquals(expected, actual);
    }

    @Test
    public void confirmOperation_test_invalid_request_operationId_not_in_base() {
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);
        TransferController controller = new TransferController(service);

        String expected = String.format("Can't transfer money for operation %s", confirmation.getOperationId());

        TransferError result = assertThrows(TransferError.class,
                () -> controller.confirmOperation(confirmation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void confirmOperation_test_invalid_request_confirmation_is_null() {
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);
        TransferController controller = new TransferController(service);

        Class<InvalidData> expected = InvalidData.class;

        InvalidData result = assertThrows(InvalidData.class,
                () -> controller.confirmOperation(null)
        );

        assertEquals(expected, result.getClass());
    }
}