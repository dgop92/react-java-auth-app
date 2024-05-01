package com.dgop92.authexample.unit.appuser;

import com.dgop92.authexample.common.exceptions.ApplicationException;
import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.features.account.database.adapters.AppUserMapper;
import com.dgop92.authexample.features.account.database.repositories.AppUserJPARepository;
import com.dgop92.authexample.features.account.usecases.appuser.AppUserDeleteUseCase;
import com.dgop92.authexample.utils.UserTestDataUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppUserUseCaseDeleteTests {

    @Mock
    private AppUserJPARepository appUserRepository;

    @InjectMocks
    private AppUserDeleteUseCase appUserUseCase;


    @Test
    public void Should_DeleteAppUser_WhenDataIsValid() {
        long appUserId = UserTestDataUtil.validAppUserId;
        var appUser = UserTestDataUtil.getValidAppUser();
        var appUserJPA = AppUserMapper.domainToJpaEntity(appUser);

        when(appUserRepository.findById(appUserId)).thenReturn(Optional.of(appUserJPA));

        // assert there is no exception
        appUserUseCase.deleteOneById(appUserId);
    }

    @Test
    public void Should_ThrowNotFoundException_WhenAppUserIsNotFound() {
        long appUserId = UserTestDataUtil.validAppUserId;

        when(appUserRepository.findById(appUserId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> appUserUseCase.deleteOneById(appUserId))
                .isInstanceOf(ApplicationException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NOT_FOUND);
    }




}
