package com.booking_dent.dentApp.model.mapper;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeDTO toDTO(EmployeeEntity entity);
}