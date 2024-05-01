package com.dgop92.authexample.features.account.external.firebase;

import com.dgop92.authexample.common.exceptions.ExceptionCode;
import com.dgop92.authexample.common.exceptions.RepositoryException;
import com.dgop92.authexample.features.account.definitions.auth.IAuthUserService;
import com.dgop92.authexample.features.account.entities.AuthUser;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FirebaseAuthUserService implements IAuthUserService {


    @Override
    public void delete(AuthUser user) {
        try {
            FirebaseAuth.getInstance().deleteUser(user.getId());
        } catch (FirebaseAuthException e) {
            if (e.getAuthErrorCode().equals(AuthErrorCode.USER_NOT_FOUND)) {
                throw new RepositoryException("user not found", ExceptionCode.NOT_FOUND, e);
            }
            throw new RepositoryException("error deleting firebase user", ExceptionCode.APPLICATION_UNEXPECTED_ERROR, e);
        }
    }

    @Override
    public Optional<AuthUser> getOneById(String id) {
        try {
            UserRecord user = FirebaseAuth.getInstance().getUser(id);
            AuthUser authUser = new AuthUser(user.getUid());
            return Optional.of(authUser);
        } catch (FirebaseAuthException e) {
            if (e.getAuthErrorCode().equals(AuthErrorCode.USER_NOT_FOUND)) {
                return Optional.empty();
            }
            throw new RepositoryException("error getting firebase user", ExceptionCode.APPLICATION_UNEXPECTED_ERROR, e);
        }
    }

    @Override
    public Optional<AuthUser> getOneByEmail(String email) {
        try {
            UserRecord user = FirebaseAuth.getInstance().getUserByEmail(email);
            AuthUser authUser = new AuthUser(user.getUid());
            return Optional.of(authUser);
        } catch (FirebaseAuthException e) {
            if (e.getAuthErrorCode().equals(AuthErrorCode.USER_NOT_FOUND)) {
                return Optional.empty();
            }
            throw new RepositoryException("error getting firebase user", ExceptionCode.APPLICATION_UNEXPECTED_ERROR, e);
        }
    }


}
