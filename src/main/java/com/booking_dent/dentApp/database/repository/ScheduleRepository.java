package com.booking_dent.dentApp.database.repository;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findByEmployeeEmployeeIdOrderByWorkDateAsc(Long employeeId);

    //aby nie dodawać więcej zmian niż 1 na jeden dzień
    boolean existsByEmployeeAndWorkDate(EmployeeEntity employee, LocalDate workDate);

    List<ScheduleEntity> findByEmployeeEmployeeIdAndWorkDateBetweenOrderByWorkDateAsc(Long employeeId, LocalDate startDate, LocalDate endDate);
}
