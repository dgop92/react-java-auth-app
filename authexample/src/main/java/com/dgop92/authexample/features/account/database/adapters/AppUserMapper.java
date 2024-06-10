package com.dgop92.authexample.features.account.database.adapters;


import com.dgop92.authexample.features.account.database.entities.AppUserJPA;
import com.dgop92.authexample.features.account.entities.AppUser;

public class AppUserMapper {

    public static AppUser jpaEntityToDomain(AppUserJPA appUserJPA) {
        return AppUser.builder()
                .id(appUserJPA.getId())
                .authUserId(appUserJPA.getAuthUserId())
                .firstName(appUserJPA.getFirstName())
                .lastName(appUserJPA.getLastName())
                .email(appUserJPA.getEmail())
                .createdAt(appUserJPA.getCreatedAt())
                .updatedAt(appUserJPA.getUpdatedAt())
                .deletedAt(appUserJPA.getDeletedAt())
                .build();
    }

    public static AppUserJPA domainToJpaEntity(AppUser appUser) {
        return AppUserJPA.builder()
                .id(appUser.getId())
                .authUserId(appUser.getAuthUserId())
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .email(appUser.getEmail())
                .createdAt(appUser.getCreatedAt())
                .updatedAt(appUser.getUpdatedAt())
                .deletedAt(appUser.getDeletedAt())
                .build();
    }
}
