package com.booking_dent.dentApp.database.repository;

import com.booking_dent.dentApp.database.entity.forAdmin.ServiceCategoryEntity;
import com.booking_dent.dentApp.database.entity.forAdmin.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

}
