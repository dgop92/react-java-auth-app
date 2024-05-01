package com.dgop92.authexample.features.account.usecases.appuser;

import com.dgop92.authexample.common.exceptions.ApplicationException;
import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.common.exceptions.ValidationUtils;
import com.dgop92.authexample.features.account.database.adapters.AppUserMapper;
import com.dgop92.authexample.features.account.database.entities.AppUserJPA;
import com.dgop92.authexample.features.account.database.repositories.AppUserJPARepository;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserFindUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserSearch;
import com.dgop92.authexample.features.account.entities.AppUser;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppUserFindUseCase implements IAppUserFindUseCase {

    private final Validator validator;
    private final AppUserJPARepository repository;

    public AppUserFindUseCase(Validator validator, AppUserJPARepository repository) {
        this.validator = validator;
        this.repository = repository;
    }
    @Override
    public Optional<AppUser> getOneBy(AppUserSearch input) {
        ValidationUtils.validate(validator, input);

        if (input.getId() != null) {
            Optional<AppUserJPA> appUserJPA = repository.findById(input.getId());
            return appUserJPA.map(AppUserMapper::jpaEntityToDomain);
        }

        if (input.getAuthUserId() != null) {
            Optional<AppUserJPA> appUserJPA = repository.findByAuthUserId(input.getAuthUserId());
            return appUserJPA.map(AppUserMapper::jpaEntityToDomain);
        }

        if (input.getEmail() != null) {
            Optional<AppUserJPA> appUserJPA = repository.findByEmail(input.getEmail());
            return appUserJPA.map(AppUserMapper::jpaEntityToDomain);
        }

        throw new ApplicationException("invalid search criteria", ExceptionCode.ID_NOT_PROVIDED);
    }
}
