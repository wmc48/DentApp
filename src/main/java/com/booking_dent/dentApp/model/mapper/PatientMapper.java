package com.booking_dent.dentApp.model.mapper;


import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.model.dto.PatientDTO;
import com.booking_dent.dentApp.model.dto.UserDTO;
import com.booking_dent.dentApp.security.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDTO toDTO(PatientEntity patientEntity); // Używaj EmployeeEntity, nie Optional
    PatientEntity toEntity(PatientDTO dto);
}
