package com.dgop92.authexample.features.account.definitions.user;

import com.dgop92.authexample.features.account.entities.User;

import java.util.Optional;

public interface IUserFindUseCase {

    Optional<User> getOneByUserId(String userId);
}
