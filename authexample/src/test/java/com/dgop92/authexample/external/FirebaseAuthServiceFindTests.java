package com.dgop92.authexample.external;

import com.dgop92.authexample.features.account.definitions.auth.schemas.EmailPasswordUserCreate;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.external.firebase.FirebaseAuthUserService;
import com.dgop92.authexample.features.account.external.firebase.FirebaseEmailPasswordService;
import com.dgop92.authexample.utils.FirebaseTestDataUtil;
import com.dgop92.authexample.utils.UserTestDataUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
@Import(AuthFirebaseTestConfig.class)
public class FirebaseAuthServiceFindTests {

    @Autowired
    FirebaseAuthUserService firebaseAuthUserService;

    AuthUser authUser1;

    @BeforeEach
    public void setup() {
        FirebaseTestDataUtil.deleteAllFirebaseUsers();

        FirebaseEmailPasswordService firebaseEmailPasswordService
                = new FirebaseEmailPasswordService();

        this.authUser1 = firebaseEmailPasswordService.create(UserTestDataUtil.getValidEmailPasswordUserCreateInput());

        EmailPasswordUserCreate input2 = EmailPasswordUserCreate.builder()
                .email("random@ex.com")
                .password("123PAsaac456")
                .build();
        firebaseEmailPasswordService.create(input2);
    }

    @AfterEach
    public void tearDown() {
        FirebaseTestDataUtil.deleteAllFirebaseUsers();
    }

    @Test
    public void Should_FindAuthUser_WhenGivenId() {
        var authUserResult = this.firebaseAuthUserService.getOneById(this.authUser1.getId());

        Assertions.assertThat(authUserResult).isNotEmpty();
        Assertions.assertThat(authUserResult.get().getId()).isEqualTo(this.authUser1.getId());
    }

    @Test
    public void Should_FindAuthUser_WhenGivenEmail() {
        var email = UserTestDataUtil.validEmail;

        var authUserResult = this.firebaseAuthUserService.getOneByEmail(email);

        Assertions.assertThat(authUserResult).isNotEmpty();
        Assertions.assertThat(authUserResult.get().getId()).isEqualTo(this.authUser1.getId());
    }

    @Test
    public void Should_NotFindAuthUser_WhenGivenUnknownId() {
        var authUserResult = this.firebaseAuthUserService.getOneById("nonexistentid");

        Assertions.assertThat(authUserResult).isEmpty();
    }

    @Test
    public void Should_NotFindAuthUser_WhenGivenUnknownEmail() {
        var authUserResult = this.firebaseAuthUserService.getOneByEmail("authuser15@pe.com");

        Assertions.assertThat(authUserResult).isEmpty();
    }
}
