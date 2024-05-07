package com.dgop92.authexample.features.account.controllers.user;

import com.dgop92.authexample.features.account.controllers.RestAuthUtils;
import com.dgop92.authexample.features.account.controllers.user.dto.EmailPasswordCreateDTO;
import com.dgop92.authexample.features.account.definitions.auth.schemas.EmailPasswordUserCreate;
import com.dgop92.authexample.features.account.definitions.user.IEmailPasswordCreateUserStrategy;
import com.dgop92.authexample.features.account.definitions.user.IUserDeleteUseCase;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.User;
import com.dgop92.authexample.path.ControllerPaths;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path=ControllerPaths.API_V1_USERS)
@RestController
public class UserController {

    private final IEmailPasswordCreateUserStrategy emailPasswordCreateUserService;

    private final IUserDeleteUseCase userDeleteUseCase;

    private final RestAuthUtils restAuthUtils;

    public UserController(
            IEmailPasswordCreateUserStrategy emailPasswordCreateUserService,
            IUserDeleteUseCase userDeleteUseCase,
            RestAuthUtils restAuthUtils
    ) {
        this.emailPasswordCreateUserService = emailPasswordCreateUserService;
        this.userDeleteUseCase = userDeleteUseCase;
        this.restAuthUtils = restAuthUtils;
    }


    @PostMapping("/create-email-password")
    ResponseEntity<AppUser> createUserWithEmailPassword(@RequestBody EmailPasswordCreateDTO emailPasswordCreateDTO) {
        EmailPasswordUserCreate emailPasswordUserCreate = EmailPasswordUserCreate.builder()
                .email(emailPasswordCreateDTO.getEmail())
                .password(emailPasswordCreateDTO.getPassword())
                .build();
        User user = emailPasswordCreateUserService.create(emailPasswordUserCreate);
        return new ResponseEntity<>(user.getAppUser(), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    AppUser getMe(Authentication authentication) {
        User user = restAuthUtils.getUserFromAuthentication(authentication);
        return user.getAppUser();
    }

    @DeleteMapping("/me")
    void deleteMe(Authentication authentication) {
        User user = restAuthUtils.getUserFromAuthentication(authentication);
        userDeleteUseCase.deleteByUserId(user.getAuthUser().getId());
    }
}
