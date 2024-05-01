package com.dgop92.authexample.unit.user;

import com.dgop92.authexample.common.exceptions.ApplicationException;
import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.common.exceptions.FieldError;
import com.dgop92.authexample.common.exceptions.InvalidInputException;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserCreateUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserFindUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserCreate;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserSearch;
import com.dgop92.authexample.features.account.definitions.auth.IAuthUserService;
import com.dgop92.authexample.features.account.definitions.auth.IEmailPasswordCreateAuthUserStrategy;
import com.dgop92.authexample.features.account.definitions.auth.schemas.EmailPasswordUserCreate;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.entities.User;
import com.dgop92.authexample.features.account.usecases.user.UserEmailPasswordCreateUseCase;
import com.dgop92.authexample.utils.CustomAssertions;
import com.dgop92.authexample.utils.UserTestDataUtil;
import jakarta.validation.Validation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserEmailPasswordCreateUseCaseTests {

    @Mock
    private IAuthUserService authUserService;
    @Mock
    private IAppUserFindUseCase appUserFindUseCase;

    @Mock
    private IAppUserCreateUseCase appUserCreateUseCase;

    @Mock
    private IEmailPasswordCreateAuthUserStrategy emailPasswordCreateAuthUserStrategy;

    private UserEmailPasswordCreateUseCase userEmailPasswordCreateUseCase;

    @BeforeEach
    public void setup() {
        userEmailPasswordCreateUseCase = new UserEmailPasswordCreateUseCase(
                Validation.buildDefaultValidatorFactory().getValidator(),
                authUserService,
                appUserFindUseCase,
                appUserCreateUseCase,
                emailPasswordCreateAuthUserStrategy
        );
    }

    /*
    * Normal case. This is your first time registering with the app.
    * */
    @Test
    public void Should_CreateUser_WhenAppUserAndAuthUserDoNotExists() {
        EmailPasswordUserCreate input = UserTestDataUtil.getValidEmailPasswordUserCreateInput();

        when(authUserService.getOneByEmail(input.getEmail())).thenReturn(Optional.empty());
        AuthUser authUser = UserTestDataUtil.getValidAuthUser();
        when(emailPasswordCreateAuthUserStrategy.create(input)).thenReturn(authUser);

        AppUserSearch appUserSearchInput = AppUserSearch.builder().authUserId(UserTestDataUtil.validAuthUserId).build();
        when(appUserFindUseCase.getOneBy(Mockito.eq(appUserSearchInput))).thenReturn(Optional.empty());


        AppUserCreate appUserCreate = AppUserCreate.builder()
                .email(input.getEmail())
                .build();
        AppUser appUser = UserTestDataUtil.getValidAppUser();
        when(appUserCreateUseCase.create(Mockito.eq(appUserCreate), Mockito.eq(authUser))).thenReturn(appUser);

        User user = userEmailPasswordCreateUseCase.create(input);

        Assertions.assertThat(user.getAuthUser()).isEqualTo(authUser);
        Assertions.assertThat(user.getAppUser()).isEqualTo(appUser);
    }

    /*
    * Possible error case. You tried to register in the app, your auth user was created in the
    * authentication service but the app user was not created due to an unexpected error. You tried to register again,
    * so it is necessary to get the already created auth user
    * */
    @Test
    public void Should_CreateUser_WhenAppUserDoesNotExistAndAuthUserDoes() {
        EmailPasswordUserCreate input = UserTestDataUtil.getValidEmailPasswordUserCreateInput();

        AuthUser authUser = UserTestDataUtil.getValidAuthUser();
        when(authUserService.getOneByEmail(input.getEmail())).thenReturn(Optional.of(authUser));

        AppUserSearch appUserSearchInput = AppUserSearch.builder().authUserId(UserTestDataUtil.validAuthUserId).build();
        when(appUserFindUseCase.getOneBy(Mockito.eq(appUserSearchInput))).thenReturn(Optional.empty());


        AppUserCreate appUserCreate = AppUserCreate.builder()
                .email(input.getEmail())
                .build();
        AppUser appUser = UserTestDataUtil.getValidAppUser();
        when(appUserCreateUseCase.create(Mockito.eq(appUserCreate), Mockito.eq(authUser))).thenReturn(appUser);

        User user = userEmailPasswordCreateUseCase.create(input);

        Assertions.assertThat(user.getAuthUser()).isEqualTo(authUser);
        Assertions.assertThat(user.getAppUser()).isEqualTo(appUser);
    }


    /*
    * Duplicate record error case.
    *
    * Scenario 1: You tried to register in the app twice using email and password strategy.
    * Scenario 2: you are already register in app using an identity provider (In this app just Google)
    * and you tried to register again using email and password strategy.
    *
    * For the firebase implementation and merging users check:
    * https://firebase.google.com/docs/auth/users?hl=en&authuser=0#verified_email_addresses
    *
    * For the moment we do not consider the possibility of not throwing and error if your email is verified
    * */
    @Test
    public void Should_ThrowApplicationException_WhenAppUserAlreadyExists() {
        EmailPasswordUserCreate input = UserTestDataUtil.getValidEmailPasswordUserCreateInput();

        AuthUser authUser = UserTestDataUtil.getValidAuthUser();
        when(authUserService.getOneByEmail(input.getEmail())).thenReturn(Optional.of(authUser));

        AppUserSearch appUserSearchInput = AppUserSearch.builder().authUserId(UserTestDataUtil.validAuthUserId).build();
        AppUser appUser = UserTestDataUtil.getValidAppUser();
        when(appUserFindUseCase.getOneBy(Mockito.eq(appUserSearchInput))).thenReturn(Optional.of(appUser));

        Assertions.assertThatThrownBy(() -> userEmailPasswordCreateUseCase.create(input))
                .isInstanceOf(ApplicationException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.DUPLICATED_RECORD);
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenEmailIsInvalid() {
        var invalidEmailPasswordUserCreateInput = EmailPasswordUserCreate.builder()
                .email("invalid-email")
                .password("password1234PLK")
                .build();

        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> userEmailPasswordCreateUseCase.create(invalidEmailPasswordUserCreateInput),
                InvalidInputException.class
        );
        CustomAssertions.assertOneInvalidInputException(throwable, "email");
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenPasswordIsTooShort() {
        var invalidEmailPasswordUserCreateInput = EmailPasswordUserCreate.builder()
                .email("a@e.com")
                .password("1A3b")
                .build();

        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> userEmailPasswordCreateUseCase.create(invalidEmailPasswordUserCreateInput),
                InvalidInputException.class
        );
        CustomAssertions.assertOneInvalidInputException(throwable, "password");
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenPasswordDoesNotContainUppercaseLetter() {
        var invalidEmailPasswordUserCreateInput = EmailPasswordUserCreate.builder()
                .email("a@e.com")
                .password("abcdfghyu1234")
                .build();

        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> userEmailPasswordCreateUseCase.create(invalidEmailPasswordUserCreateInput),
                InvalidInputException.class
        );
        CustomAssertions.assertOneInvalidInputException(throwable, "password");
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenPasswordDoesNotContainLowercaseLetter() {
        var invalidEmailPasswordUserCreateInput = EmailPasswordUserCreate.builder()
                .email("a@e.com")
                .password("ABCDFEASD1234")
                .build();

        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> userEmailPasswordCreateUseCase.create(invalidEmailPasswordUserCreateInput),
                InvalidInputException.class
        );
        CustomAssertions.assertOneInvalidInputException(throwable, "password");
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenPasswordDoesNotContainNumber() {
        var invalidEmailPasswordUserCreateInput = EmailPasswordUserCreate.builder()
                .email("a@e.com")
                .password("ABCDFEASDabchj")
                .build();

        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> userEmailPasswordCreateUseCase.create(invalidEmailPasswordUserCreateInput),
                InvalidInputException.class
        );
        CustomAssertions.assertOneInvalidInputException(throwable, "password");
    }
}
