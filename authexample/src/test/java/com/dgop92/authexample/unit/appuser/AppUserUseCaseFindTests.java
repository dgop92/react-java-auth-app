package com.dgop92.authexample.unit.appuser;

import com.dgop92.authexample.common.exceptions.ApplicationException;
import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.common.exceptions.FieldError;
import com.dgop92.authexample.common.exceptions.InvalidInputException;
import com.dgop92.authexample.features.account.database.adapters.AppUserMapper;
import com.dgop92.authexample.features.account.database.repositories.AppUserJPARepository;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserSearch;
import com.dgop92.authexample.features.account.usecases.appuser.AppUserFindUseCase;
import com.dgop92.authexample.utils.UserTestDataUtil;
import jakarta.validation.Validation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppUserUseCaseFindTests {

    @Mock
    private AppUserJPARepository appUserRepository;

    private AppUserFindUseCase appUserUseCase;

    @BeforeEach
    public void setup() {
        appUserUseCase = new AppUserFindUseCase(Validation.buildDefaultValidatorFactory().getValidator(), appUserRepository);
    }

    @Test
    public void Should_FindAppUser_WhenGivenAppUserId() {
        long appUserId = UserTestDataUtil.validAppUserId;
        var appUserSearchInput = AppUserSearch.builder()
                .id(appUserId)
                .build();
        var appUser = UserTestDataUtil.getValidAppUser();
        var appUserJPA = AppUserMapper.domainToJpaEntity(appUser);

        when(appUserRepository.findById(appUserId)).thenReturn(Optional.of(appUserJPA));

        var appUserFound = appUserUseCase.getOneBy(appUserSearchInput);

        Assertions.assertThat(appUserFound).isNotEmpty();
        Assertions.assertThat(appUserFound.get()).isEqualTo(appUser);
    }

    @Test
    public void Should_FindAppUser_WhenGivenAuthUserId() {
        var authUser = UserTestDataUtil.getValidAuthUser();
        var appUserSearchInput = AppUserSearch.builder()
                .authUserId(authUser.getId())
                .build();
        var appUser = UserTestDataUtil.getValidAppUser();
        var appUserJPA = AppUserMapper.domainToJpaEntity(appUser);

        when(appUserRepository.findByAuthUserId(authUser.getId())).thenReturn(Optional.of(appUserJPA));
        var appUserFound = appUserUseCase.getOneBy(appUserSearchInput);

        Assertions.assertThat(appUserFound).isNotEmpty();
        Assertions.assertThat(appUserFound.get()).isEqualTo(appUser);
    }

    @Test
    public void Should_FindAppUser_WhenGivenEmail() {
        var appUser = UserTestDataUtil.getValidAppUser();
        var appUserJPA = AppUserMapper.domainToJpaEntity(appUser);
        var appUserSearchInput = AppUserSearch.builder()
                .email(appUser.getEmail())
                .build();

        when(appUserRepository.findByEmail(appUser.getEmail())).thenReturn(Optional.of(appUserJPA));
        var appUserFound = appUserUseCase.getOneBy(appUserSearchInput);

        Assertions.assertThat(appUserFound).isNotEmpty();
        Assertions.assertThat(appUserFound.get()).isEqualTo(appUser);
    }

    @Test
    public void Should_ThrowAnException_WhenNoSearchCriteriaIsGiven() {
        var appUserSearchInput = AppUserSearch.builder()
                .build();

        Assertions.assertThatThrownBy(() -> appUserUseCase.getOneBy(appUserSearchInput))
                .isInstanceOf(ApplicationException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.ID_NOT_PROVIDED);
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenAppUserIdIsNonPositive() {
        var appUserSearchInput = AppUserSearch.builder()
                .id(-10L)
                .build();

        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> appUserUseCase.getOneBy(appUserSearchInput),
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.get(0).path()).isEqualTo("id");
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenAuthUserIdIsEmpty() {
        var appUserSearchInput = AppUserSearch.builder()
                .authUserId("")
                .build();

        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> appUserUseCase.getOneBy(appUserSearchInput),
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.get(0).path()).isEqualTo("authUserId");
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenEmailIsInvalid() {
        var invalidAppUserSearchInput = AppUserSearch.builder()
                .email("invalid-email")
                .build();

        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> this.appUserUseCase.getOneBy(invalidAppUserSearchInput),
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.get(0).path()).isEqualTo("email");
    }

}
