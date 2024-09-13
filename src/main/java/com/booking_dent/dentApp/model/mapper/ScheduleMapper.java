package com.booking_dent.dentApp.model.mapper;

import com.booking_dent.dentApp.database.entity.ScheduleEntity;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    @Mapping(source = "employee.name", target = "employeeName")
    @Mapping(source = "employee.surname", target = "employeeSurname")
    @Mapping(source = "shift.name", target = "shiftName")
    @Mapping(source = "shift.startTime", target = "startTime")
    @Mapping(source = "shift.endTime", target = "endTime")
    ScheduleDTO toDTO(ScheduleEntity entity);

    @Mapping(source = "employeeId", target = "employee.employeeId")
    @Mapping(source = "shiftId", target = "shift.shiftId")
    ScheduleEntity toEntity(ScheduleDTO dto);
}
