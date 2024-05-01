package com.dgop92.authexample.integration.user;

import com.dgop92.authexample.features.account.database.repositories.AppUserJPARepository;
import com.dgop92.authexample.features.account.definitions.appuser.IAppUserFindUseCase;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserSearch;
import com.dgop92.authexample.features.account.definitions.auth.IAuthUserService;
import com.dgop92.authexample.features.account.definitions.auth.IEmailPasswordCreateAuthUserStrategy;
import com.dgop92.authexample.features.account.definitions.auth.schemas.EmailPasswordUserCreate;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.entities.User;
import com.dgop92.authexample.features.account.usecases.user.UserDeleteUseCase;
import com.dgop92.authexample.features.account.usecases.user.UserEmailPasswordCreateUseCase;
import com.dgop92.authexample.features.account.usecases.user.UserFindUseCase;
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
public class UserPersistenceTests {

    @Autowired
    private UserEmailPasswordCreateUseCase userEmailPasswordCreateUseCase;

    @Autowired
    private IAuthUserService authUserService;

    @Autowired
    private IAppUserFindUseCase appUserFindUseCase;

    @Autowired
    private AppUserJPARepository appUserJPARepository;

    @Autowired
    private IEmailPasswordCreateAuthUserStrategy emailPasswordCreateAuthUserStrategy;

    @Autowired
    private UserDeleteUseCase userDeleteUseCase;

    @Autowired
    private UserFindUseCase userFindUseCase;

    @BeforeEach
    public void setup() {
        FirebaseTestDataUtil.deleteAllFirebaseUsers();
        appUserJPARepository.deleteAll();
    }

    @Test
    public void Should_CreateUser_WhenAppUserAndAuthUserDoNotExists() {
        EmailPasswordUserCreate input = UserTestDataUtil.getValidEmailPasswordUserCreateInput();

        User createdUser = userEmailPasswordCreateUseCase.create(input);
        String userId = createdUser.getAuthUser().getId();

        Optional<User> user = userFindUseCase.getOneByUserId(userId);

        Assertions.assertThat(user).isNotEmpty();
        AppUser appUser = user.get().getAppUser();

        Assertions.assertThat(appUser.getEmail()).isEqualTo(input.getEmail());
        Assertions.assertThat(appUser.getFirstName()).isEqualTo("");
        Assertions.assertThat(appUser.getLastName()).isEqualTo("");
    }

    @Test
    public void Should_CreateUser_WhenAppUserDoesNotExistAndAuthUserDoes() {
        EmailPasswordUserCreate input = UserTestDataUtil.getValidEmailPasswordUserCreateInput();
        AuthUser oldAuthUser = emailPasswordCreateAuthUserStrategy.create(input);

        User user = userEmailPasswordCreateUseCase.create(input);
        String userId = user.getAuthUser().getId();

        Optional<AuthUser> authUser = authUserService.getOneById(userId);
        Assertions.assertThat(authUser).isNotEmpty();
        Assertions.assertThat(authUser.get().getId()).isEqualTo(oldAuthUser.getId());

        Optional<AppUser> appUser = appUserFindUseCase.getOneBy(AppUserSearch.builder().authUserId(userId).build());
        Assertions.assertThat(appUser).isNotEmpty();

        Assertions.assertThat(appUser.get().getEmail()).isEqualTo(input.getEmail());
        Assertions.assertThat(appUser.get().getFirstName()).isEqualTo("");
        Assertions.assertThat(appUser.get().getLastName()).isEqualTo("");
    }

    @Test
    public void Should_DeleteUser_WhenBothUserExist() {
        EmailPasswordUserCreate input = UserTestDataUtil.getValidEmailPasswordUserCreateInput();

        User createdUser = userEmailPasswordCreateUseCase.create(input);
        String userId = createdUser.getAuthUser().getId();

        userDeleteUseCase.deleteByUserId(createdUser.getAuthUser().getId());

        Optional<User> user = userFindUseCase.getOneByUserId(userId);
        Assertions.assertThat(user).isEmpty();
    }

}
