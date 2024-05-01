package com.dgop92.authexample.features.account.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    @NonNull
    AuthUser authUser;
    @NonNull
    AppUser appUser;
}
