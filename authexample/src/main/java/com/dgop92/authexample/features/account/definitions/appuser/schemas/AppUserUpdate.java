package com.dgop92.authexample.features.account.definitions.appuser.schemas;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class AppUserUpdate {

    @Size(max = 50, message = "the first name must have at most 50 characters")
    private String firstName;

    @Size(max = 80, message = "the last name must have at most 80 characters")
    private String lastName;

    @NotNull
    @Positive
    private long appUserId;
}
