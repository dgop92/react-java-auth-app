package com.dgop92.authexample.features.account.usecases.user;

import com.dgop92.authexample.common.exceptions.ApplicationException;
import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.common.exceptions.ValidationUtils;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserCreateUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserFindUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserCreate;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserSearch;
import com.dgop92.authexample.features.account.definitions.auth.IAuthUserService;
import com.dgop92.authexample.features.account.definitions.auth.IEmailPasswordCreateAuthUserStrategy;
import com.dgop92.authexample.features.account.definitions.auth.schemas.EmailPasswordUserCreate;
import com.dgop92.authexample.features.account.definitions.user.IEmailPasswordCreateUserStrategy;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.entities.User;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserEmailPasswordCreateUseCase implements IEmailPasswordCreateUserStrategy {

    private final IAuthUserService authUserService;

    private final IAppUserFindUseCase appUserFindUseCase;
    private final IAppUserCreateUseCase appUserCreateUseCase;

    private final IEmailPasswordCreateAuthUserStrategy emailPasswordCreateAuthUserStrategy;

    private final Validator validator;

    public UserEmailPasswordCreateUseCase(
            Validator validator,
            IAuthUserService authUserService,
            IAppUserFindUseCase appUserFindUseCase,
            IAppUserCreateUseCase appUserCreateUseCase,
            IEmailPasswordCreateAuthUserStrategy emailPasswordCreateAuthUserStrategy
    ) {
        this.validator = validator;
        this.authUserService = authUserService;
        this.appUserFindUseCase = appUserFindUseCase;
        this.appUserCreateUseCase = appUserCreateUseCase;
        this.emailPasswordCreateAuthUserStrategy = emailPasswordCreateAuthUserStrategy;
    }

    @Override
    public User create(EmailPasswordUserCreate input) {
        ValidationUtils.validate(validator, input);

        // Get or create the auth user
        Optional<AuthUser> authUser = authUserService.getOneByEmail(input.getEmail());
        AuthUser finalAuthUser = authUser.orElseGet(
                () -> emailPasswordCreateAuthUserStrategy.create(input)
        );
        String userId = finalAuthUser.getId();

        Optional<AppUser> appUser = appUserFindUseCase.getOneBy(AppUserSearch.builder().authUserId(userId).build());

        if (appUser.isPresent()) {
            throw new ApplicationException(
                    "the provided email is already in use by an existing auth user",
                    ExceptionCode.DUPLICATED_RECORD
            );
        }

        AppUserCreate appUserCreate = AppUserCreate.builder()
                .email(input.getEmail())
                .build();
        AppUser finalAppUser = appUserCreateUseCase.create(appUserCreate, finalAuthUser);

        return User.builder()
                .authUser(finalAuthUser)
                .appUser(finalAppUser)
                .build();
    }
}
