package com.dgop92.authexample.features.account.definitions.appuser;

import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserUpdate;

public interface IAppUserUpdateUseCase {

    AppUser update(AppUserUpdate input);

}
