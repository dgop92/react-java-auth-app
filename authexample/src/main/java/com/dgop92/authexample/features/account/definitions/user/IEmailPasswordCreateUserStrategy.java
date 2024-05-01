package com.dgop92.authexample.features.account.definitions.user;

import com.dgop92.authexample.features.account.definitions.auth.schemas.EmailPasswordUserCreate;
import com.dgop92.authexample.features.account.entities.User;

public interface IEmailPasswordCreateUserStrategy {

    User create(EmailPasswordUserCreate input);

}
