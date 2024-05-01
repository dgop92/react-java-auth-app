package com.dgop92.authexample.unit.appuser;

import com.dgop92.authexample.common.exceptions.ApplicationException;
import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.common.exceptions.FieldError;
import com.dgop92.authexample.common.exceptions.InvalidInputException;
import com.dgop92.authexample.features.account.database.adapters.AppUserMapper;
import com.dgop92.authexample.features.account.database.repositories.AppUserJPARepository;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserUpdate;
import com.dgop92.authexample.features.account.usecases.appuser.AppUserUpdateUseCase;
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
public class AppUserUseCaseUpdateTests {

    @Mock
    private AppUserJPARepository appUserRepository;

    private AppUserUpdateUseCase appUserUseCase;

    @BeforeEach
    public void setup() {
        appUserUseCase = new AppUserUpdateUseCase(Validation.buildDefaultValidatorFactory().getValidator(), appUserRepository);
    }

    @Test
    public void Should_UpdateAppUser_WhenDataIsValid() {
        long appUserId = UserTestDataUtil.validAppUserId;
        var appUserUpdateInput = AppUserUpdate.builder()
                .appUserId(appUserId)
                .firstName("Updated John")
                .build();
        var appUser = UserTestDataUtil.getValidAppUser();
        var appUserJPA = AppUserMapper.domainToJpaEntity(appUser);
        var finalAppUserJPA = appUserJPA.toBuilder()
                .firstName("Updated John")
                .build();

        when(appUserRepository.findById(appUserId)).thenReturn(Optional.of(appUserJPA));
        when(appUserRepository.save(Mockito.eq(appUserJPA))).thenReturn(finalAppUserJPA);

        var appUserUpdatedResult = appUserUseCase.update(appUserUpdateInput);

        Assertions.assertThat(appUserUpdatedResult.getFirstName()).isEqualTo("Updated John");
    }

    @Test
    public void Should_ThrowNotFoundException_WhenAppUserIsNotFound() {
        long appUserId = 12049L;
        var appUserUpdateInput = AppUserUpdate.builder()
                .appUserId(appUserId)
                .firstName("Updated John")
                .build();

        when(appUserRepository.findById(appUserId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> appUserUseCase.update(appUserUpdateInput))
                .isInstanceOf(ApplicationException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NOT_FOUND);
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenAppUserIdIsNotProvided() {
        var appUserUpdateInput = AppUserUpdate.builder()
                .firstName("Updated John")
                .build();

        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> appUserUseCase.update(appUserUpdateInput),
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.get(0).path()).isEqualTo("appUserId");
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenAppUserIdIsNonPositive() {
        var appUserUpdateInput = AppUserUpdate.builder()
                .appUserId(-1L)
                .firstName("Updated John")
                .build();

        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> appUserUseCase.update(appUserUpdateInput),
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.get(0).path()).isEqualTo("appUserId");
    }

    private void commonInvalidInputTestForTooLongFields(AppUserUpdate data, String fieldName) {
        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> appUserUseCase.update(data),
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
                AppUserUpdate.builder()
                        .appUserId(UserTestDataUtil.validAppUserId)
                        .firstName("a".repeat(51))
                        .build(),
                "firstName"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenLastNameIsTooLong() {
        commonInvalidInputTestForTooLongFields(
                AppUserUpdate.builder()
                        .appUserId(UserTestDataUtil.validAppUserId)
                        .lastName("a".repeat(81))
                        .build(),
                "lastName"
        );
    }

}
