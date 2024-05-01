package com.dgop92.authexample.features.account.usecases.appuser;

import com.dgop92.authexample.common.exceptions.ApplicationException;
import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.features.account.database.entities.AppUserJPA;
import com.dgop92.authexample.features.account.database.repositories.AppUserJPARepository;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserDeleteUseCase;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppUserDeleteUseCase implements IAppUserDeleteUseCase {

    private final AppUserJPARepository repository;


    public AppUserDeleteUseCase(AppUserJPARepository repository) {
        this.repository = repository;
    }

    @Override
    public void deleteOneById(long id) {
        Optional<AppUserJPA> appUserJPAContainer = repository.findById(id);

        if (appUserJPAContainer.isEmpty()) {
            throw new ApplicationException("app user not found", ExceptionCode.NOT_FOUND);
        }

        repository.deleteById(id);
    }
}
