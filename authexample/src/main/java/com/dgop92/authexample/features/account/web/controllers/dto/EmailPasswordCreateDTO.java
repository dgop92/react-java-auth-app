package com.dgop92.authexample.features.account.web.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailPasswordCreateDTO {


    @Schema(description = "A valid email address")
    @NotNull
    @Email(message = "the provided email is not valid")
    private String email;


    @Schema(description = "The password must have at least 8 characters, an uppercase letter, a lowercase letter and a number")
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
        return String.format("EmailPasswordCreateDTO{email='%s'}", email);
    }
}
