package com.transfer.MoneyC2C.dto;

public class MoneyTransferResponseDTO {
    private String operationId;

    public MoneyTransferResponseDTO(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
