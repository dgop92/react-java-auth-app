package com.dgop92.authexample.unit.user;

import com.dgop92.authexample.common.exceptions.ApplicationException;
import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserDeleteUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserFindUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserSearch;
import com.dgop92.authexample.features.account.definitions.auth.IAuthUserService;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.entities.User;
import com.dgop92.authexample.features.account.usecases.user.UserDeleteUseCase;
import com.dgop92.authexample.features.account.usecases.user.UserFindUseCase;
import com.dgop92.authexample.utils.UserTestDataUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserDeleteUseCaseTests {

    @Mock
    private IAuthUserService authUserService;
    @Mock
    private IAppUserDeleteUseCase appUserDeleteUseCase;
    @Mock
    private UserFindUseCase userFindUseCase;

    private UserDeleteUseCase userDeleteUseCase;

    @BeforeEach
    public void setup() {
        userDeleteUseCase = new UserDeleteUseCase(userFindUseCase, authUserService, appUserDeleteUseCase);
    }

    @Test
    public void Should_DeleteUser_WhenBothUserExist() {
        String userId = UserTestDataUtil.validAuthUserId;
        AuthUser authUser = UserTestDataUtil.getValidAuthUser();
        AppUser appUser = UserTestDataUtil.getValidAppUser();
        User user = User.builder().authUser(authUser).appUser(appUser).build();

        when(userFindUseCase.getOneByUserId(userId)).thenReturn(Optional.of(user));

        userDeleteUseCase.deleteByUserId(userId);
    }

    @Test
    public void Should_ThrowApplicationException_WhenBothUserDoNotExist() {
        String userId = UserTestDataUtil.validAuthUserId;
        when(userFindUseCase.getOneByUserId(userId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userDeleteUseCase.deleteByUserId(userId))
                .isInstanceOf(ApplicationException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NOT_FOUND);
    }




}
