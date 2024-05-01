package com.dgop92.authexample.features.account.usecases.user;

import com.dgop92.authexample.common.exceptions.ApplicationException;
import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserDeleteUseCase;
import com.dgop92.authexample.features.account.definitions.auth.IAuthUserService;
import com.dgop92.authexample.features.account.definitions.user.IUserDeleteUseCase;
import com.dgop92.authexample.features.account.definitions.user.IUserFindUseCase;
import com.dgop92.authexample.features.account.entities.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDeleteUseCase implements IUserDeleteUseCase {

    private final IUserFindUseCase userFindUseCase;

    private final IAuthUserService authUserService;

    private final IAppUserDeleteUseCase appUserDeleteUseCase;

    public UserDeleteUseCase(
            IUserFindUseCase userFindUseCase,
            IAuthUserService authUserService,
            IAppUserDeleteUseCase appUserDeleteUseCase
    ) {
        this.userFindUseCase = userFindUseCase;
        this.authUserService = authUserService;
        this.appUserDeleteUseCase = appUserDeleteUseCase;
    }

    @Override
    public void deleteByUserId(String userId) {
        Optional<User> user = userFindUseCase.getOneByUserId(userId);

        if (user.isEmpty()) {
            throw new ApplicationException(
                    "user was not found",
                    ExceptionCode.NOT_FOUND
            );
        }

        authUserService.delete(user.get().getAuthUser());
        appUserDeleteUseCase.deleteOneById(user.get().getAppUser().getId());
    }
}
