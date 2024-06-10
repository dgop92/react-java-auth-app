package com.dgop92.authexample.unit.appuser;

import com.dgop92.authexample.common.exceptions.FieldError;
import com.dgop92.authexample.common.exceptions.InvalidInputException;
import com.dgop92.authexample.features.account.database.entities.AppUserJPA;
import com.dgop92.authexample.features.account.database.repositories.AppUserJPARepository;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserCreate;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.usecases.appuser.AppUserCreateUseCase;
import com.dgop92.authexample.utils.UserTestDataUtil;
import jakarta.validation.Validation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.Function;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppUserUseCaseCreateTests {

    @Mock
    private AppUserJPARepository appUserRepository;

    private AppUserCreateUseCase appUserUseCase;

    @BeforeEach
    public void setup() {
        appUserUseCase = new AppUserCreateUseCase(Validation.buildDefaultValidatorFactory().getValidator(), appUserRepository);
    }


    @Test
    public void Should_CreateAppUser_WhenDataIsValid() {
        String authUserId = UserTestDataUtil.validAuthUserId;
        var authUser = new AuthUser(authUserId);

        var appUserCreateInput = UserTestDataUtil.getValidAppUserCreateInput();
        var finalAppUser = UserTestDataUtil.getValidAppUser();

        var appUserJPA = AppUserJPA.builder()
                .firstName(finalAppUser.getFirstName())
                .lastName(finalAppUser.getLastName())
                .authUserId(authUserId)
                .email(finalAppUser.getEmail())
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        var finalAppUserJPA = appUserJPA.toBuilder().id(finalAppUser.getId()).build();

        // Cannot use exact match for appUserJPA because of the createdAt and updatedAt fields
        when(appUserRepository.save(Mockito.any(AppUserJPA.class))).thenReturn(finalAppUserJPA);

        var appUserCreated = appUserUseCase.create(appUserCreateInput, authUser);

        Assertions.assertThat(appUserCreated).isEqualTo(finalAppUser);
    }

    private void commonInvalidInputTestForTooLongFields(Function<AppUserCreate, AppUserCreate> fieldSetter, String fieldName) {
        var authUser = UserTestDataUtil.getValidAuthUser();
        var appUserCreateValidInput = UserTestDataUtil.getValidAppUserCreateInput();
        var appUserCreateInvalidInput = fieldSetter.apply(appUserCreateValidInput);
        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> appUserUseCase.create(appUserCreateInvalidInput, authUser),
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.get(0).path()).isEqualTo(fieldName);
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenFirstNameIsTooLong() {
        commonInvalidInputTestForTooLongFields(
                appUserCreate -> appUserCreate.toBuilder().firstName("a".repeat(51)).build(),
                "firstName"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenLastNameIsTooLong() {
        commonInvalidInputTestForTooLongFields(
                appUserCreate -> appUserCreate.toBuilder().lastName("a".repeat(81)).build(),
                "lastName"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenEmailIsInvalid() {
        String authUserId = UserTestDataUtil.validAuthUserId;
        var authUser = new AuthUser(authUserId);
        var invalidAppUserCreateInput = UserTestDataUtil
                .getValidAppUserCreateInput().toBuilder().email("invalid-email").build();

        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> this.appUserUseCase.create(invalidAppUserCreateInput, authUser),
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.get(0).path()).isEqualTo("email");
    }
}
