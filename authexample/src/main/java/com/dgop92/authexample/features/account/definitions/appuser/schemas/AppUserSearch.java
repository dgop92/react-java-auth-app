package com.dgop92.authexample.features.account.definitions.appuser.schemas;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class AppUserSearch {

    @Positive
    private Long id;

    @Size(min = 1, message = "authUserId must not be empty")
    private String authUserId;

    @Email(message = "the provided email is not valid")
    private String email;
}
