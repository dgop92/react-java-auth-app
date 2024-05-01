package com.dgop92.authexample.features.account.definitions.user;

import com.dgop92.authexample.features.account.entities.IdpProfile;
import com.dgop92.authexample.features.account.entities.User;

public interface IdpCreateUserStrategy {

    User create(String authUserId, IdpProfile idpProfile);
}
