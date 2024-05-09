package com.dgop92.authexample.features.account.usecases.user;

import com.dgop92.authexample.common.exceptions.ApplicationException;
import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserFindUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserSearch;
import com.dgop92.authexample.features.account.definitions.auth.IAuthUserService;
import com.dgop92.authexample.features.account.definitions.user.IUserFindUseCase;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserFindUseCase implements IUserFindUseCase {

    Logger logger = LoggerFactory.getLogger(UserFindUseCase.class);

    private final IAuthUserService authUserService;

    private final IAppUserFindUseCase appUserFindUseCase;

    public UserFindUseCase(IAuthUserService authUserService, IAppUserFindUseCase appUserFindUseCase) {
        this.authUserService = authUserService;
        this.appUserFindUseCase = appUserFindUseCase;
    }

    @Override
    public Optional<User> getOneByUserId(String userId) {
        logger.info("Finding user with id: {}", userId);

        Optional<AuthUser> authUser = authUserService.getOneById(userId);
        Optional<AppUser> appUser = appUserFindUseCase.getOneBy(AppUserSearch.builder().authUserId(userId).build());

        if (authUser.isEmpty() && appUser.isEmpty()) {
            logger.info("Could not find user with id: {}", userId);
            return Optional.empty();
        }

        if (authUser.isPresent() && appUser.isPresent()) {
            logger.info("Found user with id: {}", userId);
            return Optional.of(
                    User.builder()
                            .authUser(authUser.get())
                            .appUser(appUser.get())
                            .build()
            );
        }

        logger.warn("Could not find both auth and app user for id: {}", userId);

        if (authUser.isPresent()) {
            throw new ApplicationException(
                    "auth user exists, but app user does not",
                    ExceptionCode.APPLICATION_INTEGRITY_ERROR
            );
        }

        throw new ApplicationException(
                "app user exists, but auth user does not",
                ExceptionCode.APPLICATION_INTEGRITY_ERROR
        );
    }
}
