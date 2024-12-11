package com.booking_dent.dentApp.model.mapper;


import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ReservationEntity;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.model.dto.ReservationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(source = "patient.name", target = "patientName")
    @Mapping(source = "patient.surname", target = "patientSurname")
    ReservationDTO toDTO(ReservationEntity reservation);

    ReservationEntity toEntity(ReservationDTO dto);

    List<ReservationDTO> toDTOList(List<ReservationEntity> dtoList);
}
