package com.booking_dent.dentApp.model.mapper;


import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeDTO toDTO(EmployeeEntity employee); // Używaj EmployeeEntity, nie Optional

    EmployeeEntity toEntity(EmployeeDTO dto);
}
