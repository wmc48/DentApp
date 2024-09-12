package com.booking_dent.dentApp.model.mapper;

import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.model.dto.ShiftDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShiftMapper {
    ShiftMapper INSTANCE = Mappers.getMapper(ShiftMapper.class);

    ShiftDTO toDTO(ShiftEntity entity);
    ShiftEntity toEntity(ShiftDTO dto);
}
