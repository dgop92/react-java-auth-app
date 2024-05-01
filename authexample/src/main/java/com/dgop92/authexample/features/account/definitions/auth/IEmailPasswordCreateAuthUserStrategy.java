package com.dgop92.authexample.features.account.definitions.auth;

import com.dgop92.authexample.features.account.definitions.auth.schemas.EmailPasswordUserCreate;
import com.dgop92.authexample.features.account.entities.AuthUser;

public interface IEmailPasswordCreateAuthUserStrategy {

    AuthUser create(EmailPasswordUserCreate input);
}
