package com.dgop92.authexample.unit.controllers;

import com.dgop92.authexample.common.exceptions.ApplicationException;
import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.common.exceptions.InvalidInputException;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserUpdateUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserUpdate;
import com.dgop92.authexample.features.account.definitions.auth.schemas.EmailPasswordUserCreate;
import com.dgop92.authexample.features.account.definitions.user.IEmailPasswordCreateUserStrategy;
import com.dgop92.authexample.features.account.definitions.user.IUserDeleteUseCase;
import com.dgop92.authexample.features.account.entities.User;
import com.dgop92.authexample.features.account.web.RestAuthUtils;
import com.dgop92.authexample.features.account.web.controllers.UserController;
import com.dgop92.authexample.features.account.web.controllers.dto.AppUserUpdateDTO;
import com.dgop92.authexample.features.account.web.controllers.dto.EmailPasswordCreateDTO;
import com.dgop92.authexample.utils.UserTestDataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false) // disable all spring security filters
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IEmailPasswordCreateUserStrategy emailPasswordCreateUserService;

    @MockBean
    private IUserDeleteUseCase userDeleteUseCase;

    @MockBean
    private IAppUserUpdateUseCase appUserUpdateUseCase;

    @MockBean
    private RestAuthUtils restAuthUtils;

    @Test
    public void Should_CreateUser_WhenDataIsValid() throws Exception {
        User validUser = UserTestDataUtil.getValidUser();
        EmailPasswordCreateDTO data = new EmailPasswordCreateDTO(
                validUser.getAppUser().getEmail(),
                "validPassword1234"
        );
        EmailPasswordUserCreate emailPasswordUserCreate = EmailPasswordUserCreate.builder()
                .email(validUser.getAppUser().getEmail())
                .password("validPassword1234")
                .build();


        when(emailPasswordCreateUserService.create(emailPasswordUserCreate)).thenReturn(validUser);
        ResultActions response = mockMvc.perform(post("/api/v1/users/create-email-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data)));


        response.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(validUser.getAppUser().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(validUser.getAppUser().getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(validUser.getAppUser().getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(validUser.getAppUser().getLastName()));
    }

    @Test
    public void Should_ThrowBadRequest_WhenCreatingUserWithInvalidData() throws Exception {
        EmailPasswordCreateDTO data = new EmailPasswordCreateDTO(
                "invalid-email",
                "validPassword1234"
        );
        InvalidInputException ex = new InvalidInputException();
        ex.addError("email", "error message 1");
        ex.addError("password", "error message 2");


        when(emailPasswordCreateUserService.create(Mockito.any(EmailPasswordUserCreate.class))).thenThrow(ex);
        ResultActions response = mockMvc.perform(post("/api/v1/users/create-email-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data)));


        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[*].path", hasSize(2)))
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$.fieldErrors[*].path",
                                containsInAnyOrder("email", "password"))
                );

    }

    @Test
    public void Should_ThrowConflict_WhenCreatingUserWithDuplicateEmail() throws Exception {
        EmailPasswordCreateDTO data = new EmailPasswordCreateDTO(
                UserTestDataUtil.validEmail,
                "validPassword1234"
        );
        EmailPasswordUserCreate emailPasswordUserCreate = EmailPasswordUserCreate.builder()
                .email(UserTestDataUtil.validEmail)
                .password("validPassword1234")
                .build();
        ApplicationException ex = new ApplicationException(
                "the provided email is already in use by an existing auth user",
                ExceptionCode.DUPLICATED_RECORD
        );

        when(emailPasswordCreateUserService.create(emailPasswordUserCreate)).thenThrow(ex);
        ResultActions response = mockMvc.perform(post("/api/v1/users/create-email-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data)));


        response.andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[*].path", hasSize(0)));
    }

    @Test
    public void Should_UpdateUser_WhenDataIsValid() throws Exception {
        User validUser = UserTestDataUtil.getValidUser();
        AppUserUpdateDTO data = new AppUserUpdateDTO(
                validUser.getAppUser().getFirstName(),
                validUser.getAppUser().getLastName()
        );
        AppUserUpdate appUserUpdate = AppUserUpdate.builder()
                .appUserId(validUser.getAppUser().getId())
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .build();


        when(restAuthUtils.getUserFromAuthentication(Mockito.any())).thenReturn(validUser);
        when(appUserUpdateUseCase.update(appUserUpdate)).thenReturn(validUser.getAppUser());
        ResultActions response = mockMvc.perform(patch("/api/v1/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data)));


        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(validUser.getAppUser().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(validUser.getAppUser().getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(validUser.getAppUser().getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(validUser.getAppUser().getLastName()));
    }

    @Test
    public void Should_ThrowBadRequest_WhenUpdatingUserWithInvalidData() throws Exception {
        User validUser = UserTestDataUtil.getValidUser();
        AppUserUpdateDTO data = new AppUserUpdateDTO(
                validUser.getAppUser().getFirstName(),
                "a".repeat(81)
        );
        AppUserUpdate appUserUpdate = AppUserUpdate.builder()
                .appUserId(validUser.getAppUser().getId())
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .build();
        InvalidInputException ex = new InvalidInputException();
        ex.addError("firstName", "error message 1");

        when(restAuthUtils.getUserFromAuthentication(Mockito.any())).thenReturn(validUser);
        when(appUserUpdateUseCase.update(appUserUpdate)).thenThrow(ex);
        ResultActions response = mockMvc.perform(patch("/api/v1/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data)));

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[*].path", hasSize(1)))
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$.fieldErrors[*].path",
                                containsInAnyOrder("firstName"))
                );
    }

    @Test
    public void Should_GetAuthenticatedUser() throws Exception {
        User validUser = UserTestDataUtil.getValidUser();


        when(restAuthUtils.getUserFromAuthentication(Mockito.any())).thenReturn(validUser);
        ResultActions response = mockMvc.perform(get("/api/v1/users/me"));


        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(validUser.getAppUser().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(validUser.getAppUser().getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(validUser.getAppUser().getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(validUser.getAppUser().getLastName()));
    }

    @Test
    public void Should_DeleteAuthenticateUser() throws Exception {
        User validUser = UserTestDataUtil.getValidUser();

        when(restAuthUtils.getUserFromAuthentication(Mockito.any())).thenReturn(validUser);
        ResultActions response = mockMvc.perform(delete("/api/v1/users/me"));
        verify(userDeleteUseCase).deleteByUserId(validUser.getAuthUser().getId());

        response.andExpect(status().isNoContent());
    }
}
