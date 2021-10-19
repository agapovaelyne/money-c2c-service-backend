package com.transfer.MoneyC2C.service;

import com.transfer.MoneyC2C.dto.MoneyTransferResponseDTO;
import com.transfer.MoneyC2C.exception.InvalidData;
import com.transfer.MoneyC2C.exception.TransferError;
import com.transfer.MoneyC2C.model.ConfirmationEntity;
import com.transfer.MoneyC2C.model.Operation;
import com.transfer.MoneyC2C.model.TestModels;
import com.transfer.MoneyC2C.repository.TransferRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TransferServiceTest {

    private final TestModels testModels = new TestModels();
    private final Operation operation = testModels.getOperation();
    private final ConfirmationEntity confirmation = testModels.getConfirmationEntity();

    @Test
    public void transferMoney_test_repository_mocked() {
        String resultText = "Repository result";
        MoneyTransferResponseDTO result = new MoneyTransferResponseDTO(resultText);
        ResponseEntity<MoneyTransferResponseDTO> expected = new ResponseEntity<>(result, HttpStatus.OK);
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);
        when(repositoryMock.transferMoney(operation)).thenReturn(of(resultText));
        MoneyTransferResponseDTO actual = service.transferMoney(operation);
        assertEquals(expected.getBody().getOperationId(), actual.getOperationId());
    }

    @Test
    public void transferMoney_test_nothing_mocked_valid_request() {
        TransferRepository repository = new TransferRepository();
        TransferService service = new TransferService(repository);
        MoneyTransferResponseDTO actual = service.transferMoney(operation);
        ResponseEntity<MoneyTransferResponseDTO> expected = new ResponseEntity<>(new MoneyTransferResponseDTO(operation.getOperationId()),HttpStatus.OK);
        assertEquals(expected.getBody().getOperationId(), actual.getOperationId());
    }

    @Test
    public void transferMoney_test_invalid_request_card_expired() {
        Operation operation = testModels.getCardExpiredOperation();
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);
        String expected = String.format("Card %s expired or invalid format of date provided", operation.getCardFromNumber());

        InvalidData result = assertThrows(InvalidData.class,
                () -> service.transferMoney(operation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void transferMoney_test_invalid_request_null_operation() {
        Operation operation = mock(Operation.class);
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);
        Class<InvalidData> expected = InvalidData.class;

        InvalidData result = assertThrows(InvalidData.class,
                () -> service.transferMoney(operation)
        );
        assertEquals(expected, result.getClass());
    }

    @Test
    public void transferMoney_test_invalid_request_nullCardFrom_operation() {
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService serviceSpy = spy(new TransferService(repositoryMock));
        Class<TransferError> expected = TransferError.class;

        TransferError result = assertThrows(TransferError.class,
                () -> serviceSpy.transferMoney(testModels.getCardFromNullNumberOperation())
        );
        assertEquals(expected, result.getClass());
    }

    @Test
    public void transferMoney_test_invalid_request_same_card() {
        Operation operation = testModels.getSameCardOperation();
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);

        String expected = String.format("Can't transfer money to the same card: %s -> %s", operation.getCardFromNumber(), operation.getCardToNumber());

        InvalidData result = assertThrows(InvalidData.class,
                () -> service.transferMoney(operation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void transferMoney_test_invalid_request_null_amount() {
        Operation operation = testModels.getNullAmountOperation();
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);
        String expected = String.format("Can't transfer money from card %s", operation.getCardFromNumber());

        TransferError result = assertThrows(TransferError.class,
                () -> service.transferMoney(operation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void transferMoney_test_invalidAmount_request() {
        //if spring validation doesn't work
        Operation operation = testModels.getInvalidAmountOperation();
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);
        String expected = String.format("Can't transfer money from card %s", operation.getCardFromNumber());

        TransferError result = assertThrows(TransferError.class,
                () -> service.transferMoney(operation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void transferMoney_test_invalid_cardFromCVV_request() {
        //if spring validation doesn't work
        Operation operation = testModels.getCardFromInvalidCVVOperation();
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);

        String expected = String.format("Can't transfer money from card %s", operation.getCardFromNumber());

        TransferError result = assertThrows(TransferError.class,
                () -> service.transferMoney(operation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void transferMoney_test_invalid_request_invalid_cardTo() {
        //if spring validation doesn't work
        Operation operation = testModels.getCardFromInvalidNumberOperation();
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);
        String expected = String.format("Can't transfer money from card %s", operation.getCardFromNumber());

        TransferError result = assertThrows(TransferError.class,
                () -> service.transferMoney(operation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void transferMoney_test_invalid_request_blank_cardTo() {
        //if spring validation doesn't work
        Operation operation = testModels.getCardFromBlankNumberOperation();
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);
        String expected = String.format("Can't transfer money from card %s", operation.getCardFromNumber());

        TransferError result = assertThrows(TransferError.class,
                () -> service.transferMoney(operation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void confirmOperation_test_repository_mocked() {
        String resultText = "Repository result";
        MoneyTransferResponseDTO result = new MoneyTransferResponseDTO(resultText);
        ResponseEntity<MoneyTransferResponseDTO> expected = new ResponseEntity<>(result, HttpStatus.OK);
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);
        when(repositoryMock.confirmOperation(confirmation.getOperationId(), confirmation.getCode())).thenReturn(of(resultText));
        MoneyTransferResponseDTO actual = service.confirmOperation(confirmation);
        assertEquals(expected.getBody().getOperationId(), actual.getOperationId());
    }

    @Test
    public void confirmOperation_test_invalid_request_operationId_not_in_base() {
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);

        String expected = String.format("Can't transfer money for operation with id %s", confirmation.getOperationId());

        TransferError result = assertThrows(TransferError.class,
                () -> service.confirmOperation(confirmation)
        );
        assertEquals(expected, result.getMessage());
    }

    @Test
    public void confirmOperation_test_invalid_request_confirmation_is_null() {
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);

        Class<InvalidData> expected = InvalidData.class;

        InvalidData result = assertThrows(InvalidData.class,
                () -> service.confirmOperation(null)
        );

        assertEquals(expected, result.getClass());
    }

    @Test
    public void confirmOperation_test_invalid_request_confirmation_invalid_code() {
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);

        Class<TransferError> expected = TransferError.class;

        TransferError result = assertThrows(TransferError.class,
                () -> service.confirmOperation(testModels.getConfirmationInvalidCodeEntity())
        );

        assertEquals(expected, result.getClass());
    }

    @Test
    public void confirmOperation_test_invalid_request_confirmation_invalid_OperationId() {
        //if spring validation doesn't work
        TransferRepository repositoryMock = mock(TransferRepository.class);
        TransferService service = new TransferService(repositoryMock);

        Class<TransferError> expected = TransferError.class;

        TransferError result = assertThrows(TransferError.class,
                () -> service.confirmOperation(testModels.getConfirmationInvalidIdEntity())
        );

        assertEquals(expected, result.getClass());
    }
}
