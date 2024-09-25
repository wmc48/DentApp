package com.booking_dent.dentApp.database.repository;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository <EmployeeEntity, Integer> {
}
