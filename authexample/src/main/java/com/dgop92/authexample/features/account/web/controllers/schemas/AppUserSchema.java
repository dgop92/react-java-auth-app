package com.dgop92.authexample.features.account.web.controllers.schemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class AppUserSchema {

    private long id;

    private String email;

    private String firstName;

    private String lastName;
}
