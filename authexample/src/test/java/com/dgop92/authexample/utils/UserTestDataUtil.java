package com.dgop92.authexample.utils;


import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserCreate;
import com.dgop92.authexample.features.account.definitions.auth.schemas.EmailPasswordUserCreate;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.AuthUser;

public class UserTestDataUtil {

    public static long validAppUserId = 1L;
    public static String validAuthUserId = "pls8df-123fl";

    public static String validEmail = "pepe@gmail.com";

    public static AppUserCreate getValidAppUserCreateInput() {
        return AppUserCreate.builder()
                .firstName("Pepe")
                .lastName("Dull")
                .email(validEmail)
                .build();
    }

    public static EmailPasswordUserCreate getValidEmailPasswordUserCreateInput() {
        return EmailPasswordUserCreate.builder()
                .email(validEmail)
                .password("123PAsaac456")
                .build();
    }


    public static AuthUser getValidAuthUser() {
        return new AuthUser(validAuthUserId);
    }


    public static AppUser getValidAppUser() {
        return AppUser.builder()
                .id(validAppUserId)
                .authUserId(validAuthUserId)
                .firstName("Pepe")
                .lastName("Dull")
                .email(validEmail)
                .build();
    }
}
