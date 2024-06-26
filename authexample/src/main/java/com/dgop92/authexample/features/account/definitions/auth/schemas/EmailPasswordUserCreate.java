package com.dgop92.authexample.features.account.definitions.auth.schemas;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class EmailPasswordUserCreate {

    @NotNull
    @Email(message = "the provided email is not valid")
    private String email;
    // Password must have at least 8 characters and at least an uppercase letter,a lowercase letter and a number
    @NotNull
    @Pattern(
            regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$",
            message = "the password must have at least 8 characters, an uppercase letter, " +
                    "a lowercase letter and a number"
    )
    private String password;

    @Override
    public String toString() {
        // We cannot log the password due to security reasons
        return String.format("AuthUserCreate{email='%s'}", email);
    }
}
