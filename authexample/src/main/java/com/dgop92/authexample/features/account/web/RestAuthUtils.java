package com.dgop92.authexample.features.account.web;

import com.dgop92.authexample.features.account.definitions.user.IdpCreateUserStrategy;
import com.dgop92.authexample.features.account.entities.IdpProfile;
import com.dgop92.authexample.features.account.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class RestAuthUtils {

    private final IdpCreateUserStrategy idpCreateUser;

    public RestAuthUtils(IdpCreateUserStrategy IdpCreateUser) {
        this.idpCreateUser = IdpCreateUser;
    }

    public User getUserFromAuthentication(Authentication authentication) {

        String authUserId = authentication.getName();
        IdpProfile idpProfile = FirebaseTokenAttributeExtractor.getIdpProfile(authentication);

        // This will get or create a user based on the authUserId.
        return idpCreateUser.create(authUserId, idpProfile);
    }
}
