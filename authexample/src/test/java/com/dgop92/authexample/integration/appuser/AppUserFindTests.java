package com.dgop92.authexample.integration.appuser;

import com.dgop92.authexample.features.account.database.repositories.AppUserJPARepository;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserCreate;
import com.dgop92.authexample.features.account.definitions.appuser.schemas.AppUserSearch;
import com.dgop92.authexample.features.account.entities.AppUser;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.usecases.appuser.AppUserCreateUseCase;
import com.dgop92.authexample.features.account.usecases.appuser.AppUserFindUseCase;
import com.dgop92.authexample.utils.UserTestDataUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppUserFindTests {

    @Autowired
    private AppUserJPARepository appUserRepository;

    @Autowired
    private AppUserFindUseCase appUserFindUseCase;

    @Autowired
    private AppUserCreateUseCase appUserCreateUseCase;


    private AppUser originalAppUser;


    @BeforeEach
    public void setup() {
        var appUserCreateInput1 = UserTestDataUtil.getValidAppUserCreateInput();
        this.originalAppUser = appUserCreateUseCase.create(appUserCreateInput1, UserTestDataUtil.getValidAuthUser());

        // Dummy app user
        var appUserCreateInput2 = AppUserCreate.builder()
                .firstName("Random 1")
                .lastName("Random 2")
                .email("jak@ex.com")
                .build();
        appUserCreateUseCase.create(appUserCreateInput2, new AuthUser("aslkdlasd-caosdiko"));
    }

    @AfterEach
    public void deleteTestAppUser1() {
        appUserRepository.deleteAll();
    }

    @Test
    public void Should_FindAppUser_WhenGivenAppUserId() {
        var appUserSearchInput = AppUserSearch.builder()
                .id(this.originalAppUser.getId())
                .build();

        var appUserFound = appUserFindUseCase.getOneBy(appUserSearchInput);

        Assertions.assertThat(appUserFound).isNotEmpty();
        compareAppUsers(appUserFound.get(), originalAppUser);
    }

    @Test
    public void Should_FindAppUser_WhenGivenAuthUserId() {
        var appUserSearchInput = AppUserSearch.builder()
                .authUserId(originalAppUser.getAuthUserId())
                .build();

        var appUserFound = appUserFindUseCase.getOneBy(appUserSearchInput);

        Assertions.assertThat(appUserFound).isNotEmpty();
        compareAppUsers(appUserFound.get(), originalAppUser);
    }

    @Test
    public void Should_FindAppUser_WhenGivenEmail() {
        var appUserSearchInput = AppUserSearch.builder()
                .email(originalAppUser.getEmail())
                .build();

        var appUserFound = appUserFindUseCase.getOneBy(appUserSearchInput);

        Assertions.assertThat(appUserFound).isNotEmpty();
        compareAppUsers(appUserFound.get(), originalAppUser);
    }

    private void compareAppUsers(AppUser appUser1, AppUser appUser2) {
        Assertions.assertThat(appUser1.getId()).isEqualTo(appUser2.getId());
        Assertions.assertThat(appUser1.getFirstName()).isEqualTo(appUser2.getFirstName());
        Assertions.assertThat(appUser1.getLastName()).isEqualTo(appUser2.getLastName());
        Assertions.assertThat(appUser1.getEmail()).isEqualTo(appUser2.getEmail());
        Assertions.assertThat(appUser1.getAuthUserId()).isEqualTo(appUser2.getAuthUserId());
    }
}
