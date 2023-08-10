package com.doc.mgt.system.docmgt.general.enums;

public enum ResponseCodeAndMessage {

    SUCCESSFUL_0("0", "Successful"),
    TIMEOUT_ERROR_87("87", "Timeout error"),
    RECORD_NOT_FOUND_88("88", "Record not found"),
    BILLER_NOT_FOUND_89("89", "Biller not found"),
    INVALID_JSON_REQUEST_DATA_90("90", "Invalid JSON request data"),
    INCOMPLETE_PARAMETERS_91("91", "Incomplete parameters"),
    REMOTE_REQUEST_FAILED_92("92", "Remote request failed"),
    OPERATION_NOT_SUPPORTED_93("93", "Operation not supported"),
    AUTHENTICATION_FAILED_95("95", "Authentication failed"),
    CLIENT_NOT_ALLOWED_97("97", "Client not allowed"),
    AN_ERROR_OCCURRED_96("96", "An error occurred"),
    UNAUTHORIZED_97("97", "Invalid or missing JWT token"),
    SYSTEM_ERROR_99("99", "System error"),
    ALREADY_EXIST_86("86", "Already exist");

    public String responseCode;
    public String responseMessage;

    ResponseCodeAndMessage(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
}
