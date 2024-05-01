package com.dgop92.authexample.features.account.definitions.auth;


import com.dgop92.authexample.features.account.entities.AuthUser;

import java.util.Optional;

public interface IAuthUserService {
    void delete(AuthUser user);
    Optional<AuthUser> getOneById(String id);
    Optional<AuthUser> getOneByEmail(String email);
}
