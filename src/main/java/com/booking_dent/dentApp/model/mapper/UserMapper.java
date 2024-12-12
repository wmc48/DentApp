package com.booking_dent.dentApp.model.mapper;

import com.booking_dent.dentApp.model.dto.UserDTO;
import com.booking_dent.dentApp.security.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(UserEntity userEntity); // Używaj EmployeeEntity, nie Optional

    UserEntity toEntity(UserDTO dto);
}
