package com.dgop92.authexample.features.account.usecases.appuser;

import com.dgop92.authexample.common.Utils;
import com.dgop92.authexample.common.exceptions.ValidationUtils;
import com.dgop92.authexample.features.account.database.adapters.AppUserMapper;
import com.dgop92.authexample.features.account.database.entities.AppUserJPA;
import com.dgop92.authexample.features.account.database.repositories.AppUserJPARepository;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserCreateUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserCreate;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.AuthUser;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class AppUserCreateUseCase implements IAppUserCreateUseCase {

    private final Validator validator;
    private final AppUserJPARepository repository;

    public AppUserCreateUseCase(Validator validator, AppUserJPARepository repository) {
        this.validator = validator;
        this.repository = repository;
    }

    @Override
    public AppUser create(AppUserCreate input, AuthUser authUser) {
        ValidationUtils.validate(validator, input);

        String firstName = Utils.toBlankStringIfNull(input.getFirstName());
        String lastName = Utils.toBlankStringIfNull(input.getLastName());

        AppUserJPA appUserJPA = AppUserJPA.builder()
                .firstName(firstName)
                .lastName(lastName)
                .authUserId(authUser.getId())
                .email(input.getEmail())
                .build();

        AppUserJPA finalAppUserJPA = repository.save(appUserJPA);

        return AppUserMapper.jpaEntityToDomain(finalAppUserJPA);
    }
}
