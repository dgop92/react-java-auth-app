package com.dgop92.authexample.features.account.database.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "app_user")
@Table(name = "app_user", uniqueConstraints = {
        @UniqueConstraint(name = "auth_user_id_unique", columnNames = {"auth_user_id"}),
        @UniqueConstraint(name = "email_unique", columnNames = {"email", "deleted_at"})
})
public class AppUserJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_gen")
    @SequenceGenerator(name = "app_user_gen", sequenceName = "app_user_seq")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(length = 100, nullable = false)
    private String authUserId;

    @Column(length = 320, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 80, nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

}
