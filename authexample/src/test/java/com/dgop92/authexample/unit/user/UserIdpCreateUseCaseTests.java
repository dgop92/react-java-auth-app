package com.dgop92.authexample.unit.user;

import com.dgop92.authexample.common.exceptions.ApplicationException;
import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserCreateUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserFindUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserCreate;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserSearch;
import com.dgop92.authexample.features.account.definitions.auth.IAuthUserService;
import com.dgop92.authexample.features.account.definitions.auth.IEmailPasswordCreateAuthUserStrategy;
import com.dgop92.authexample.features.account.definitions.auth.schemas.EmailPasswordUserCreate;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.entities.IdpProfile;
import com.dgop92.authexample.features.account.entities.User;
import com.dgop92.authexample.features.account.usecases.user.UserEmailPasswordCreateUseCase;
import com.dgop92.authexample.features.account.usecases.user.UserIdpCreateUseCase;
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
public class UserIdpCreateUseCaseTests {

    @Mock
    private IAppUserFindUseCase appUserFindUseCase;

    @Mock
    private IAppUserCreateUseCase appUserCreateUseCase;

    private UserIdpCreateUseCase userIdpCreateUseCase;

    @BeforeEach
    public void setup() {
        userIdpCreateUseCase = new UserIdpCreateUseCase(appUserFindUseCase, appUserCreateUseCase);
    }


    @Test
    public void Should_CreateUser_WhenAppUserDoesNotExist() {
        String userId = UserTestDataUtil.validAuthUserId;
        AppUserCreate appUserCreate = UserTestDataUtil.getValidAppUserCreateInput();
        AppUser appUser = UserTestDataUtil.getValidAppUser();
        IdpProfile idpProfile = IdpProfile.builder()
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .email(UserTestDataUtil.validEmail)
                .build();
        AppUserSearch appUserSearchInput = AppUserSearch.builder().authUserId(userId).build();

        when(appUserFindUseCase.getOneBy(Mockito.eq(appUserSearchInput))).thenReturn(Optional.empty());
        when(appUserCreateUseCase.create(Mockito.eq(appUserCreate), Mockito.any(AuthUser.class))).thenReturn(appUser);


        User user = userIdpCreateUseCase.create(userId, idpProfile);

        Assertions.assertThat(user.getAppUser()).isEqualTo(appUser);
        Assertions.assertThat(user.getAuthUser().getId()).isEqualTo(userId);
    }


    @Test
    public void Should_CreateUser_WhenAppUserExists() {
        String userId = UserTestDataUtil.validAuthUserId;
        AppUser appUser = UserTestDataUtil.getValidAppUser();
        IdpProfile idpProfile = IdpProfile.builder()
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .email(UserTestDataUtil.validEmail)
                .build();
        AppUserSearch appUserSearchInput = AppUserSearch.builder().authUserId(userId).build();

        when(appUserFindUseCase.getOneBy(Mockito.eq(appUserSearchInput))).thenReturn(Optional.of(appUser));

        User user = userIdpCreateUseCase.create(userId, idpProfile);

        Assertions.assertThat(user.getAppUser()).isEqualTo(appUser);
        Assertions.assertThat(user.getAuthUser().getId()).isEqualTo(userId);
    }

}
