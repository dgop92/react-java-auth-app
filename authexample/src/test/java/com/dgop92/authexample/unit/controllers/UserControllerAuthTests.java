package com.dgop92.authexample.unit.controllers;

import com.dgop92.authexample.common.exceptions.InvalidInputException;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserUpdateUseCase;
import com.dgop92.authexample.features.account.definitions.user.IEmailPasswordCreateUserStrategy;
import com.dgop92.authexample.features.account.definitions.user.IUserDeleteUseCase;
import com.dgop92.authexample.features.account.entities.User;
import com.dgop92.authexample.features.account.web.RestAuthUtils;
import com.dgop92.authexample.common.web.SecurityConfiguration;
import com.dgop92.authexample.features.account.web.controllers.UserController;
import com.dgop92.authexample.features.account.web.controllers.dto.AppUserUpdateDTO;
import com.dgop92.authexample.features.account.web.controllers.dto.EmailPasswordCreateDTO;
import com.dgop92.authexample.path.ControllerPaths;
import com.dgop92.authexample.utils.JWTSecurityTestConfig;
import com.dgop92.authexample.utils.UserTestDataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfiguration.class, JWTSecurityTestConfig.class})
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerAuthTests {

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

    private final String userMePath = ControllerPaths.API_V1_USERS + "/me";


    @ParameterizedTest
    @ValueSource(strings = {"get", "patch", "delete"})
    public void Should_ThrowUnauthorized_When_UserIsNotAuthenticated(String input) throws Exception {
        ResultActions response = switch (input) {
            case "get" -> mockMvc.perform(get(userMePath));
            case "patch" -> mockMvc.perform(patch(userMePath));
            case "delete" -> mockMvc.perform(delete(userMePath));
            default -> throw new InvalidInputException("Invalid input");
        };

        response.andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"get", "patch", "delete"})
    public void Should_GetSuccessfulResponse_When_UserIsAuthenticated(String input) throws Exception {
        User validUser = UserTestDataUtil.getValidUser();

        when(restAuthUtils.getUserFromAuthentication(Mockito.any())).thenReturn(validUser);
        when(appUserUpdateUseCase.update(Mockito.any())).thenReturn(validUser.getAppUser());

        ResultActions response = switch (input) {
            case "get" -> mockMvc.perform(get(userMePath)
                    .header("Authorization", "Bearer " + "validToken"));
            case "patch" -> mockMvc.perform(get(userMePath)
                    .header("Authorization", "Bearer " + "validToken")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new AppUserUpdateDTO("newName", "newSurname"))));
            case "delete" -> mockMvc.perform(delete(userMePath)
                    .header("Authorization", "Bearer " + "validToken"));
            default -> throw new InvalidInputException("Invalid input");
        };

        response.andExpect(status().is2xxSuccessful());

    }

    @Test
    public void Should_CreateUser_WhenAuthIsNotProvided() throws Exception {
        User validUser = UserTestDataUtil.getValidUser();
        EmailPasswordCreateDTO data = new EmailPasswordCreateDTO(
                validUser.getAppUser().getEmail(),
                "validPassword1234"
        );


        when(emailPasswordCreateUserService.create(Mockito.any())).thenReturn(UserTestDataUtil.getValidUser());
        ResultActions response = mockMvc.perform(post(String.format("%s/create-email-password", ControllerPaths.API_V1_USERS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data)));


        response.andExpect(status().isCreated());
    }


}
