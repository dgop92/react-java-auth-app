package com.dgop92.authexample.common.exceptions;

import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ValidationUtils {

    static Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

    public static <T> void validate(Validator validator, T input){
        logger.debug("Validating input");
        var valResult = validator.validate(input);
        if (!valResult.isEmpty()) {
            logger.debug("Validation failed");
            InvalidInputException ex = new InvalidInputException();
            valResult.forEach(vr -> ex.addError(vr.getPropertyPath().toString(), vr.getMessage()));
            throw ex;
        }else {
            logger.debug("Validation passed");
        }
    }
}
