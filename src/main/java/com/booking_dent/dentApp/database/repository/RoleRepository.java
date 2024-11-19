package com.booking_dent.dentApp.database.repository;

import com.booking_dent.dentApp.security.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
}
