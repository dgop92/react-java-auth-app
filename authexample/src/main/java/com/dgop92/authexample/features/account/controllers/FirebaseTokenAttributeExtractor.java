package com.dgop92.authexample.features.account.controllers;

import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.common.exceptions.PresentationException;
import com.dgop92.authexample.features.account.entities.IdpProfile;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;

public class FirebaseTokenAttributeExtractor {

    public static IdpProfile getIdpProfile(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            Map<String, Object> tokenAttrs = jwtAuthenticationToken.getTokenAttributes();
            String name = tokenAttrs.get("name").toString();
            // split by space
            String[] nameParts = name.split(" ");
            String firstName = nameParts[0];
            String lastName = "";
            if (nameParts.length > 1) {
                // use the remaining parts as the last name
                nameParts[0] = "";
                lastName = String.join(" ", nameParts).trim();
            }

            String email = tokenAttrs.get("email").toString();

            return IdpProfile.builder().firstName(firstName).lastName(lastName).email(email).build();
        } else {
            throw new PresentationException(
                    "invalid authentication token, expected JwtAuthenticationToken",
                    ExceptionCode.APPLICATION_UNEXPECTED_ERROR
            );
        }
    }


}
