package com.dgop92.authexample.utils;

import com.dgop92.authexample.common.exceptions.FieldError;
import com.dgop92.authexample.common.exceptions.InvalidInputException;
import org.assertj.core.api.Assertions;

import java.util.List;

public class CustomAssertions {

    public static void assertOneInvalidInputException(InvalidInputException ex, String fieldName) {
        Assertions.assertThat(ex).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = ex.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.get(0).path()).isEqualTo(fieldName);
    }
}
