package com.dgop92.authexample.features.account.usecases.user;

import com.dgop92.authexample.features.account.definitions.appuser.IAppUserCreateUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserFindUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserCreate;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserSearch;
import com.dgop92.authexample.features.account.definitions.user.IdpCreateUserStrategy;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.entities.IdpProfile;
import com.dgop92.authexample.features.account.entities.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserIdpCreateUseCase implements IdpCreateUserStrategy {

    private final IAppUserFindUseCase appUserFindUseCase;
    private final IAppUserCreateUseCase appUserCreateUseCase;


    public UserIdpCreateUseCase(
            IAppUserFindUseCase appUserFindUseCase,
            IAppUserCreateUseCase appUserCreateUseCase
    ) {
        this.appUserFindUseCase = appUserFindUseCase;
        this.appUserCreateUseCase = appUserCreateUseCase;
    }

    @Override
    public User create(String authUserId, IdpProfile idpProfile) {
        AuthUser finalAuthUser = new AuthUser(authUserId);

        Optional<AppUser> appUser = appUserFindUseCase.getOneBy(AppUserSearch.builder().authUserId(authUserId).build());
        // TODO: validate email
        AppUserCreate.AppUserCreateBuilder appUserCreateBuilder = AppUserCreate.builder()
                .email(idpProfile.getEmail());

        if (!idpProfile.getFirstName().isEmpty()) {
            appUserCreateBuilder.firstName(idpProfile.getFirstName());
        }

        if (!idpProfile.getLastName().isEmpty()) {
            appUserCreateBuilder.lastName(idpProfile.getLastName());
        }

        AppUserCreate appUserCreate = appUserCreateBuilder.build();

        AppUser finalAppUser = appUser.orElseGet(() -> appUserCreateUseCase.create(appUserCreate, finalAuthUser));

        return User.builder()
                .authUser(finalAuthUser)
                .appUser(finalAppUser)
                .build();
    }
}
