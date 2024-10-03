package com.booking_dent.dentApp.database.repository;

import com.booking_dent.dentApp.database.entity.PatientEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
    @Query("SELECT p FROM PatientEntity p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:surname IS NULL OR LOWER(p.surname) LIKE LOWER(CONCAT('%', :surname, '%'))) AND " +
            "(:pesel IS NULL OR p.pesel LIKE CONCAT('%', :pesel, '%')) AND " +
            "(:email IS NULL OR LOWER(p.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:phone IS NULL OR p.phone LIKE CONCAT('%', :phone, '%'))")
    List<PatientEntity> searchPatients(@Param("name") String name,
                                       @Param("surname") String surname,
                                       @Param("pesel") String pesel,
                                       @Param("email") String email,
                                       @Param("phone") String phone);
    @Override
    @Query("SELECT p FROM PatientEntity p ORDER BY p.surname ASC") // Sortowanie rosnące według nazwiska
    List<PatientEntity> findAll();
}
