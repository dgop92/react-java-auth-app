package com.dgop92.authexample.external;

import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.common.exceptions.RepositoryException;
import com.dgop92.authexample.features.account.definitions.auth.schemas.EmailPasswordUserCreate;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.dgop92.authexample.features.account.external.firebase.FirebaseAuthUserService;
import com.dgop92.authexample.features.account.external.firebase.FirebaseEmailPasswordService;
import com.dgop92.authexample.utils.FirebaseTestDataUtil;
import com.dgop92.authexample.utils.UserTestDataUtil;
import jakarta.validation.Validation;
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
public class FirebaseEmailPasswordCreateTests {

    @Autowired
    FirebaseEmailPasswordService firebaseEmailPasswordService;

    @Autowired
    FirebaseAuthUserService firebaseAuthUserService;

    @BeforeEach
    public void setup() {
        FirebaseTestDataUtil.deleteAllFirebaseUsers();
    }

    @AfterEach
    public void tearDown() {
        FirebaseTestDataUtil.deleteAllFirebaseUsers();
    }

    @Test
    public void Should_CreateAuthUser_WhenDataIsValid() {
        var userCreateInput = UserTestDataUtil.getValidEmailPasswordUserCreateInput();

        this.firebaseEmailPasswordService.create(userCreateInput);
        var authUser2 = this.firebaseAuthUserService.getOneByEmail(userCreateInput.getEmail());

        Assertions.assertThat(authUser2).isNotEmpty();
    }

    @Test
    public void Should_ThrowRepositoryException_WhenEmailAlreadyExists() {
        var userCreateInput = UserTestDataUtil.getValidEmailPasswordUserCreateInput();

        this.firebaseEmailPasswordService.create(userCreateInput);

        RepositoryException throwable = Assertions.catchThrowableOfType(
                () -> this.firebaseEmailPasswordService.create(userCreateInput),
                RepositoryException.class
        );
        Assertions.assertThat(throwable).withFailMessage("RepositoryException was not thrown").isNotNull();
        Assertions.assertThat(throwable.getExceptionCode()).isEqualTo(ExceptionCode.DUPLICATED_RECORD);
    }

}
