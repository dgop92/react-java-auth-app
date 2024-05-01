package com.dgop92.authexample.features.account.usecases.appuser;

import com.dgop92.authexample.common.exceptions.ApplicationException;
import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.common.exceptions.ValidationUtils;
import com.dgop92.authexample.features.account.database.adapters.AppUserMapper;
import com.dgop92.authexample.features.account.database.entities.AppUserJPA;
import com.dgop92.authexample.features.account.database.repositories.AppUserJPARepository;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserUpdateUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserUpdate;
import com.dgop92.authexample.features.account.entities.AppUser;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppUserUpdateUseCase implements IAppUserUpdateUseCase {

    private final Validator validator;
    private final AppUserJPARepository repository;


    public AppUserUpdateUseCase(Validator validator, AppUserJPARepository repository) {
        this.validator = validator;
        this.repository = repository;
    }

    @Override
    public AppUser update(AppUserUpdate input) {
        ValidationUtils.validate(validator, input);

        Optional<AppUserJPA> appUserJPAContainer = repository.findById(input.getAppUserId());

        if (appUserJPAContainer.isEmpty()) {
            throw new ApplicationException("app user not found", ExceptionCode.NOT_FOUND);
        }

        AppUserJPA appUserJPA = appUserJPAContainer.get();

        if (input.getFirstName() != null) {
            appUserJPA.setFirstName(input.getFirstName());
        }
        if (input.getLastName() != null) {
            appUserJPA.setLastName(input.getLastName());
        }

        repository.save(appUserJPA);

        return AppUserMapper.jpaEntityToDomain(appUserJPA);
    }
}
