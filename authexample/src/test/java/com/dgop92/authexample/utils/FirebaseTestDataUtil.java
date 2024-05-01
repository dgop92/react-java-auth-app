package com.dgop92.authexample.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class FirebaseTestDataUtil {

    public static void deleteAllFirebaseUsers() {
        // NOTE: the max number of users that can be deleted in one call is 1000
        // but as we are in a test environment, we don't need to worry about that
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        System.out.println(firebaseAuth);
        try {
            firebaseAuth.listUsers(null).getValues().forEach(userRecord -> {
                try {
                    firebaseAuth.deleteUser(userRecord.getUid());
                } catch (FirebaseAuthException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }

}
