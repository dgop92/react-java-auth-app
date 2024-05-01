package com.dgop92.authexample.features.account.definitions.appuser;

import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserCreate;

public interface IAppUserCreateUseCase {

    AppUser create(AppUserCreate input, AuthUser authUser);

}
