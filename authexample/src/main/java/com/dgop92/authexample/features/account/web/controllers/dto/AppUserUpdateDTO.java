package com.dgop92.authexample.features.account.web.controllers.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserUpdateDTO {

    @Size(max = 50, message = "the first name must have at most 50 characters")
    private String firstName;

    @Size(max = 80, message = "the last name must have at most 80 characters")
    private String lastName;
}
