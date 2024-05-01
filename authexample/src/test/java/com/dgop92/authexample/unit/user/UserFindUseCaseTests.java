package com.dgop92.authexample.unit.user;

import com.dgop92.authexample.common.exceptions.ApplicationException;
import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserFindUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserSearch;
import com.dgop92.authexample.features.account.definitions.auth.IAuthUserService;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.entities.User;
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
public class UserFindUseCaseTests {

    @Mock
    private IAuthUserService authUserService;
    @Mock
    private IAppUserFindUseCase appUserFindUseCase;

    private UserFindUseCase userFindUseCase;

    @BeforeEach
    public void setup() {
        userFindUseCase = new UserFindUseCase(authUserService, appUserFindUseCase);
    }

    @Test
    public void Should_FindUser_WhenAppUserAndAuthUserExist() {
        String userId = UserTestDataUtil.validAuthUserId;
        AuthUser authUser = UserTestDataUtil.getValidAuthUser();
        AppUser appUser = UserTestDataUtil.getValidAppUser();
        AppUserSearch appUserSearchInput = AppUserSearch.builder().authUserId(userId).build();

        when(authUserService.getOneById(userId)).thenReturn(Optional.of(authUser));
        when(appUserFindUseCase.getOneBy(Mockito.eq(appUserSearchInput))).thenReturn(Optional.of(appUser));

        Optional<User> result = userFindUseCase.getOneByUserId(userId);

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getAuthUser()).isEqualTo(authUser);
        Assertions.assertThat(result.get().getAppUser()).isEqualTo(appUser);
    }

    @Test
    public void Should_NotFindUser_WhenAppUserAndAuthUserDoNotExist() {
        String userId = "abc-dfg-123-456";
        AppUserSearch appUserSearchInput = AppUserSearch.builder().authUserId(userId).build();

        when(authUserService.getOneById(userId)).thenReturn(Optional.empty());
        when(appUserFindUseCase.getOneBy(Mockito.eq(appUserSearchInput))).thenReturn(Optional.empty());

        Optional<User> result = userFindUseCase.getOneByUserId(userId);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void Should_ThrowApplicationException_WhenAppUserExistsButAuthUserNot() {
        String userId = "abc-dfg-123-456";
        AppUser appUser = UserTestDataUtil.getValidAppUser();
        appUser.setAuthUserId(userId);
        AppUserSearch appUserSearchInput = AppUserSearch.builder().authUserId(userId).build();

        when(authUserService.getOneById(userId)).thenReturn(Optional.empty());
        when(appUserFindUseCase.getOneBy(Mockito.eq(appUserSearchInput))).thenReturn(Optional.of(appUser));

        Assertions.assertThatThrownBy(() -> userFindUseCase.getOneByUserId(userId))
                .isInstanceOf(ApplicationException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.APPLICATION_INTEGRITY_ERROR);
    }

    @Test
    public void Should_ThrowApplicationException_WhenAuthUserExistsButAppUserNot() {
        String userId = UserTestDataUtil.validAuthUserId;
        AuthUser authUser = UserTestDataUtil.getValidAuthUser();
        AppUserSearch appUserSearchInput = AppUserSearch.builder().authUserId(userId).build();

        when(authUserService.getOneById(userId)).thenReturn(Optional.of(authUser));
        when(appUserFindUseCase.getOneBy(Mockito.eq(appUserSearchInput))).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userFindUseCase.getOneByUserId(userId))
                .isInstanceOf(ApplicationException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.APPLICATION_INTEGRITY_ERROR);
    }


}
