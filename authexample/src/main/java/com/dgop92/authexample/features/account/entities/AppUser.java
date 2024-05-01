package com.dgop92.authexample.features.account.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class AppUser {

    private long id;

    @NonNull
    private String authUserId;

    @NonNull
    private String email;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;
}
