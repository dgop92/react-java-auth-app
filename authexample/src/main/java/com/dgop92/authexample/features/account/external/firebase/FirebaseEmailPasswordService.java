package com.dgop92.authexample.features.account.external.firebase;

import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.common.exceptions.RepositoryException;
import com.dgop92.authexample.features.account.definitions.auth.IEmailPasswordCreateAuthUserStrategy;
import com.dgop92.authexample.features.account.definitions.auth.schemas.EmailPasswordUserCreate;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FirebaseEmailPasswordService implements IEmailPasswordCreateAuthUserStrategy {

    Logger logger = LoggerFactory.getLogger(FirebaseEmailPasswordService.class);

    @Override
    public AuthUser create(EmailPasswordUserCreate input) {

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(input.getEmail())
                .setEmailVerified(false)
                .setPassword(input.getPassword())
                .setDisabled(false);


        try {
            logger.info("Creating firebase user with email: {}", input.getEmail());
            UserRecord user = FirebaseAuth.getInstance().createUser(request);
            logger.info("Firebase user created with email: {}", input.getEmail());
            return new AuthUser(user.getUid());
        } catch (FirebaseAuthException e) {
            if (e.getAuthErrorCode().equals(AuthErrorCode.EMAIL_ALREADY_EXISTS)) {
                throw new RepositoryException("email already exists", ExceptionCode.DUPLICATED_RECORD, e);
            }
            // Data is valid at this point, so this is an unexpected error from the Firebase service
            throw new RepositoryException("error creating firebase user", ExceptionCode.APPLICATION_UNEXPECTED_ERROR, e);
        }
    }
}
