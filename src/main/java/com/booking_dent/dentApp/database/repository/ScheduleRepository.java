package com.booking_dent.dentApp.database.repository;

import com.booking_dent.dentApp.database.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findByEmployeeEmployeeId(Long employeeId);
}


//    @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.employee e JOIN FETCH s.shift")
//    List<ScheduleEntity> findAllWithDetails();