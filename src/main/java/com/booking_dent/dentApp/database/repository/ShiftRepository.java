package com.booking_dent.dentApp.database.repository;

import com.booking_dent.dentApp.database.entity.ScheduleEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShiftRepository extends JpaRepository<ShiftEntity, Integer> {

}
