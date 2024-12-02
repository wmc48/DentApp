package com.booking_dent.dentApp.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Modifying
    @Query(value = "INSERT INTO user_role (user_id, role_id) VALUES (:userId, :roleId)", nativeQuery = true)
    void assignUserRole(
            @Param("userId") Long userId,
            @Param("roleId") Integer roleId
    );

    Optional<UserEntity> findByUsername(String username);
}
