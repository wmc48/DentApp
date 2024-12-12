package com.booking_dent.dentApp.model.mapper;

import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.model.dto.ShiftDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShiftMapper {
    ShiftDTO toDTO(ShiftEntity shiftEntity); // Używaj EmployeeEntity, nie Optional

    ShiftEntity toEntity(ShiftDTO dto);
}
