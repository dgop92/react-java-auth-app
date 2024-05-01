package com.dgop92.authexample.common.exceptions;

public enum ExceptionCode {

    /**
     * Exception code for invalid input.
     */
    INVALID_INPUT,
    /**
     * Exception code for invalid id.
     */
    INVALID_ID,
    /**
     * Exception code for invalid operation.
     */
    INVALID_OPERATION,
    /**
     * Exception code for duplicated record.
     */
    DUPLICATED_RECORD,
    /**
     * Exception code for id not provided.
     */
    ID_NOT_PROVIDED,
    /**
     * Exception code for not found record.
     */
    NOT_FOUND,
    /**
     * Exception code for unauthorized access.
     */
    UNAUTHORIZED,
    /**
     * Exception code for forbidden access or permission denied.
     */
    FORBIDDEN,
    /**
     * Exception code for application integrity error. Data inconsistency.
     */
    APPLICATION_INTEGRITY_ERROR,
    /**
     * Exception code for application unexpected error. External services errors.
     */
    APPLICATION_UNEXPECTED_ERROR,

}
