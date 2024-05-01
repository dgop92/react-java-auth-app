package com.dgop92.authexample.integration.appuser;

import com.dgop92.authexample.features.account.database.repositories.AppUserJPARepository;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserCreate;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserSearch;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserUpdate;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.usecases.appuser.AppUserCreateUseCase;
import com.dgop92.authexample.features.account.usecases.appuser.AppUserDeleteUseCase;
import com.dgop92.authexample.features.account.usecases.appuser.AppUserFindUseCase;
import com.dgop92.authexample.features.account.usecases.appuser.AppUserUpdateUseCase;
import com.dgop92.authexample.utils.UserTestDataUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AppUserPersistenceTests {

    @Autowired
    private AppUserJPARepository appUserRepository;

    @Autowired
    private AppUserCreateUseCase appUserCreateUseCase;

    @Autowired
    private AppUserFindUseCase appUserFindUseCase;

    @Autowired
    private AppUserUpdateUseCase appUserUpdateUseCase;

    @Autowired
    private AppUserDeleteUseCase appUserDeleteUseCase;

    private AppUser originalAppUser;


    @BeforeEach
    public void setup() {
        var appUserCreateRepoData = UserTestDataUtil.getValidAppUserCreateInput();
        this.originalAppUser = appUserCreateUseCase.create(appUserCreateRepoData, UserTestDataUtil.getValidAuthUser());
    }

    @AfterEach
    public void deleteTestAppUser1() {
        appUserRepository.deleteAll();
    }

    @Test
    public void Should_CreateAndPersistAppUser_WhenDataIsValid() {
        var authUser = new AuthUser("aslkdlasd-caosdiko");
        var appUserCreateInput = AppUserCreate.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .build();

        var appUserCreated = appUserCreateUseCase.create(appUserCreateInput, authUser);
        var appUserSearchInput = AppUserSearch.builder().id(appUserCreated.getId()).build();
        var appUserResult = appUserFindUseCase.getOneBy(appUserSearchInput);

        if (appUserResult.isPresent()) {
            var appUser = appUserResult.get();
            Assertions.assertThat(appUser.getFirstName()).isEqualTo(appUserCreateInput.getFirstName());
            Assertions.assertThat(appUser.getLastName()).isEqualTo(appUserCreateInput.getLastName());
            Assertions.assertThat(appUser.getAuthUserId()).isEqualTo(authUser.getId());
        } else {
            Assertions.fail("appUser not found");
        }

    }

    @Test
    public void Should_UpdateAppUser_WhenFirstNameIsProvided() {
        // update the first name
        var data = AppUserUpdate.builder()
                .appUserId(originalAppUser.getId())
                .firstName("Charlie")
                .build();
        appUserUpdateUseCase.update(data);

        // look for the appUser
        var appUserSearchInput = AppUserSearch.builder().id(originalAppUser.getId()).build();
        var appUserResult = appUserFindUseCase.getOneBy(appUserSearchInput);

        if (appUserResult.isPresent()) {
            // assert that the first name has been updated
            var foundAppUser = appUserResult.get();
            Assertions.assertThat(foundAppUser.getFirstName()).isEqualTo(data.getFirstName());

            // assert that the remaining fields has not been updated
            var manualUpdatedAppUser = originalAppUser.toBuilder().firstName(data.getFirstName()).build();
            Assertions.assertThat(manualUpdatedAppUser).isEqualTo(foundAppUser);
        } else {
            Assertions.fail("appUser not found");
        }
    }

    @Test
    public void Should_UpdateAppUser_WhenLastNameIsProvided() {
        // update the last name
        var data = AppUserUpdate.builder()
                .appUserId(originalAppUser.getId())
                .lastName("Brown")
                .build();
        appUserUpdateUseCase.update(data);

        // look for the appUser
        var appUserSearchInput = AppUserSearch.builder().id(originalAppUser.getId()).build();
        var appUserResult = appUserFindUseCase.getOneBy(appUserSearchInput);

        if (appUserResult.isPresent()) {
            // assert that the last name has been updated
            var foundAppUser = appUserResult.get();
            Assertions.assertThat(foundAppUser.getLastName()).isEqualTo(data.getLastName());

            // assert that the remaining fields has not been updated
            var manualUpdatedAppUser = originalAppUser.toBuilder().lastName(data.getLastName()).build();
            Assertions.assertThat(manualUpdatedAppUser).isEqualTo(foundAppUser);
        } else {
            Assertions.fail("appUser not found");
        }
    }
    @Test

    public void Should_DeleteAppUser_WhenAppUserExists() {
        appUserDeleteUseCase.deleteOneById(originalAppUser.getId());
        var appUserSearchInput = AppUserSearch.builder().id(originalAppUser.getId()).build();
        var appUserResult = appUserFindUseCase.getOneBy(appUserSearchInput);
        Assertions.assertThat(appUserResult).isEmpty();
    }
}
