package com.transfer.MoneyC2C.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ConfirmationEntity {

    private static final String OPERATION_ID_FORMAT_REGEXP = "^[0-9]+$";
    private static final String CONFIRMATION_CODE_FORMAT_REGEXP = "^[0-9]+$";


    @NotBlank(message = "Operation Id can't be blank")
    @Pattern(regexp = OPERATION_ID_FORMAT_REGEXP, message = "Invalid operation Id")
    private String operationId;

    @NotBlank(message = "Confirmation code can't be blank")
    @Pattern(regexp = CONFIRMATION_CODE_FORMAT_REGEXP, message = "Invalid Confirmation code")
    private String code;


    public ConfirmationEntity(String operationId, String code) {
        this.operationId = operationId;
        this.code = code;
    }

    public String getOperationId() {
        return operationId;
    }

    public String getCode() {
        return code;
    }
}
