package com.booking_dent.dentApp.database.repository;

import com.booking_dent.dentApp.database.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findByEmployeeEmployeeIdOrderByWorkDateAsc(Long employeeId);}
