package com.dgop92.authexample.features.account.web.controllers;

import com.dgop92.authexample.common.web.ApiError;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserUpdate;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.usecases.appuser.AppUserUpdateUseCase;
import com.dgop92.authexample.features.account.web.RestAuthUtils;
import com.dgop92.authexample.features.account.web.controllers.dto.AppUserUpdateDTO;
import com.dgop92.authexample.features.account.web.controllers.schemas.AppUserSchema;
import com.dgop92.authexample.features.account.web.controllers.dto.EmailPasswordCreateDTO;
import com.dgop92.authexample.features.account.definitions.auth.schemas.EmailPasswordUserCreate;
import com.dgop92.authexample.features.account.definitions.user.IEmailPasswordCreateUserStrategy;
import com.dgop92.authexample.features.account.definitions.user.IUserDeleteUseCase;
import com.dgop92.authexample.features.account.entities.User;
import com.dgop92.authexample.features.account.web.controllers.schemas.SchemaAdapters;
import com.dgop92.authexample.path.ControllerPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = ControllerPaths.API_V1_USERS)
@RestController
@Tag(name = "users")
public class UserController {

    private final IEmailPasswordCreateUserStrategy emailPasswordCreateUserService;

    private final IUserDeleteUseCase userDeleteUseCase;

    private final AppUserUpdateUseCase appUserUpdateUseCase;

    private final RestAuthUtils restAuthUtils;

    public UserController(
            IEmailPasswordCreateUserStrategy emailPasswordCreateUserService,
            IUserDeleteUseCase userDeleteUseCase,
            AppUserUpdateUseCase appUserUpdateUseCase,
            RestAuthUtils restAuthUtils
    ) {
        this.emailPasswordCreateUserService = emailPasswordCreateUserService;
        this.userDeleteUseCase = userDeleteUseCase;
        this.appUserUpdateUseCase = appUserUpdateUseCase;
        this.restAuthUtils = restAuthUtils;
    }


    @PostMapping("/create-email-password")
    @Operation(summary = "Create a user with email and password")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created a new user",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = AppUserSchema.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Email or password does not meet the specified requirements",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email is already in use",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            )
    })
    ResponseEntity<AppUserSchema> createUserWithEmailPassword(@RequestBody EmailPasswordCreateDTO emailPasswordCreateDTO) {
        EmailPasswordUserCreate emailPasswordUserCreate = EmailPasswordUserCreate.builder()
                .email(emailPasswordCreateDTO.getEmail())
                .password(emailPasswordCreateDTO.getPassword())
                .build();
        User user = emailPasswordCreateUserService.create(emailPasswordUserCreate);
        AppUserSchema appUserScheme = SchemaAdapters.appUserEntityToSchema(user.getAppUser());
        return new ResponseEntity<>(appUserScheme, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    @Operation(summary = "Get the current authenticated user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = AppUserSchema.class))
                    }
            ),
            @ApiResponse(responseCode = "401", content = {@Content(mediaType = "application/json")})
    })
    ResponseEntity<AppUserSchema> getMe(Authentication authentication) {
        User user = restAuthUtils.getUserFromAuthentication(authentication);
        AppUserSchema appUserSchema = SchemaAdapters.appUserEntityToSchema(user.getAppUser());
        return new ResponseEntity<>(appUserSchema, HttpStatus.OK);
    }

    @PatchMapping("/me")
    @Operation(summary = "Update the app user of the current authenticated user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = AppUserSchema.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
            @ApiResponse(responseCode = "401", content = {@Content(mediaType = "application/json")})
    })
    ResponseEntity<AppUserSchema> updateMe(Authentication authentication, @RequestBody AppUserUpdateDTO appUserUpdateDTO) {
        User user = restAuthUtils.getUserFromAuthentication(authentication);
        AppUserUpdate appUserUpdate = AppUserUpdate.builder()
                .appUserId(user.getAppUser().getId())
                .firstName(appUserUpdateDTO.getFirstName())
                .lastName(appUserUpdateDTO.getLastName())
                .build();
        AppUser appUserUpdated = appUserUpdateUseCase.update(appUserUpdate);
        AppUserSchema appUserSchema = SchemaAdapters.appUserEntityToSchema(appUserUpdated);
        return new ResponseEntity<>(appUserSchema, HttpStatus.OK);
    }

    @DeleteMapping("/me")
    @Operation(summary = "Delete the current authenticated user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = AppUserSchema.class))
                    }
            ),
            @ApiResponse(responseCode = "401", content = {@Content(mediaType = "application/json")})
    })
    ResponseEntity<Void> deleteMe(Authentication authentication) {
        User user = restAuthUtils.getUserFromAuthentication(authentication);
        userDeleteUseCase.deleteByUserId(user.getAuthUser().getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
