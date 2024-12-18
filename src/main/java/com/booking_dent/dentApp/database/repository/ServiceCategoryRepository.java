package com.booking_dent.dentApp.database.repository;

import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.database.entity.forAdmin.ServiceCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategoryEntity, Long> {

}
