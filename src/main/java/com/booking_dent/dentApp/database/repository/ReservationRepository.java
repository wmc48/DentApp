package com.booking_dent.dentApp.database.repository;

import com.booking_dent.dentApp.database.entity.ReservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    @Query("SELECT r FROM ReservationEntity r WHERE " +
            "(:patientName IS NULL OR LOWER(r.patient.name) LIKE LOWER(CONCAT('%', :patientName, '%'))) AND " +
            "(:patientSurname IS NULL OR LOWER(r.patient.surname) LIKE LOWER(CONCAT('%', :patientSurname, '%'))) AND " +
            "(:patientPesel IS NULL OR r.patient.pesel LIKE CONCAT('%', :patientPesel, '%')) AND " +
            "(:employeeName IS NULL OR LOWER(r.employee.name) LIKE LOWER(CONCAT('%', :employeeName, '%'))) AND " +
            "(:employeeSurname IS NULL OR LOWER(r.employee.surname) LIKE LOWER(CONCAT('%', :employeeSurname, '%'))) AND " +
            "(:employeeId IS NULL OR r.employee.employeeId = :employeeId) AND " +
//            "(:dateFrom IS NULL OR r.dateAndTime >= :dateFrom) AND " +
//            "(:dateTo IS NULL OR r.dateAndTime <= :dateTo) AND " +
            "(:reservationId IS NULL OR r.reservationId = :reservationId)")
    Page<ReservationEntity> findReservation(
            @Param("patientName") String patientName,
            @Param("patientSurname") String patientSurname,
            @Param("patientPesel") String patientPesel,
            @Param("employeeName") String employeeName,
            @Param("employeeSurname") String employeeSurname,
            @Param("employeeId") Long employeeId,
            @Param("reservationId") Long reservationId,
            Pageable pageable // Accept pageable for pagination
    );
}
