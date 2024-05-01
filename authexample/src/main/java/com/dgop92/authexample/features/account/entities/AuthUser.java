package com.dgop92.authexample.features.account.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuthUser {

    private String id;
}
