package com.dgop92.authexample.integration.user;

import com.dgop92.authexample.features.account.database.repositories.AppUserJPARepository;
import com.dgop92.authexample.features.account.definitions.auth.IEmailPasswordCreateAuthUserStrategy;
import com.dgop92.authexample.features.account.definitions.auth.schemas.EmailPasswordUserCreate;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.entities.IdpProfile;
import com.dgop92.authexample.features.account.entities.User;
import com.dgop92.authexample.features.account.usecases.user.UserFindUseCase;
import com.dgop92.authexample.features.account.usecases.user.UserIdpCreateUseCase;
import com.dgop92.authexample.utils.FirebaseTestDataUtil;
import com.dgop92.authexample.utils.UserTestDataUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserIdpPersistenceTests {

    @Autowired
    private UserIdpCreateUseCase userIdpCreateUseCase;

    @Autowired
    private AppUserJPARepository appUserJPARepository;

    @Autowired
    private IEmailPasswordCreateAuthUserStrategy emailPasswordCreateAuthUserStrategy;

    @Autowired
    private UserFindUseCase userFindUseCase;

    // Already created user trough "IDP"
    private AuthUser idpAuthUser;

    private String idpEmail;

    @BeforeEach
    public void setup() {
        FirebaseTestDataUtil.deleteAllFirebaseUsers();
        appUserJPARepository.deleteAll();

        EmailPasswordUserCreate input = UserTestDataUtil.getValidEmailPasswordUserCreateInput();
        idpAuthUser = emailPasswordCreateAuthUserStrategy.create(input);
        idpEmail = input.getEmail();
    }

    @Test
    public void Should_CreateUser_WhenAppUserDoesNotExit() {
        IdpProfile idpProfile = IdpProfile.builder()
                .email(idpEmail)
                .firstName("Pepe")
                .lastName("Jimenez")
                .build();

        User createdUser = userIdpCreateUseCase.create(idpAuthUser.getId(), idpProfile);
        String userId = createdUser.getAuthUser().getId();

        Optional<User> user = userFindUseCase.getOneByUserId(userId);

        Assertions.assertThat(user).isNotEmpty();
        AppUser appUser = user.get().getAppUser();

        Assertions.assertThat(appUser.getEmail()).isEqualTo(idpEmail);
        Assertions.assertThat(appUser.getFirstName()).isEqualTo("Pepe");
        Assertions.assertThat(appUser.getLastName()).isEqualTo("Jimenez");
    }

}
