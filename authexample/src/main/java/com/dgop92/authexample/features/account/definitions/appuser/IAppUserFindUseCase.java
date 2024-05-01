package com.dgop92.authexample.features.account.definitions.appuser;

import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserSearch;

import java.util.Optional;

public interface IAppUserFindUseCase {

    Optional<AppUser> getOneBy(AppUserSearch input);

}
