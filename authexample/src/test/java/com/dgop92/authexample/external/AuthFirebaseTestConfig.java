package com.dgop92.authexample.external;

import com.dgop92.authexample.features.account.external.firebase.FirebaseAuthUserService;
import com.dgop92.authexample.features.account.external.firebase.FirebaseEmailPasswordService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AuthFirebaseTestConfig {

    @Bean
    public FirebaseAuthUserService firebaseAuthUserService() {
        return new FirebaseAuthUserService();
    }

    @Bean
    public FirebaseEmailPasswordService firebaseEmailPasswordService() {
        return new FirebaseEmailPasswordService();
    }
}
