package com.dgop92.authexample.features.account.web.controllers.schemas;

import com.dgop92.authexample.features.account.entities.AppUser;

public class SchemaAdapters {

    public static AppUserSchema appUserEntityToSchema(AppUser appUser) {
        return AppUserSchema.builder()
                .id(appUser.getId())
                .email(appUser.getEmail())
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .build();
    }
}
