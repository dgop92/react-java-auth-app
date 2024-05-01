package com.dgop92.authexample.features.account.database.repositories;

import com.dgop92.authexample.features.account.database.entities.AppUserJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserJPARepository extends JpaRepository<AppUserJPA, Long> {

    @Query("SELECT u FROM app_user u WHERE u.authUserId = ?1")
    Optional<AppUserJPA> findByAuthUserId(String authUserId);

    @Query("SELECT u FROM app_user u WHERE u.email = ?1")
    Optional<AppUserJPA> findByEmail(String email);
}
