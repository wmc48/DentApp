package com.booking_dent.dentApp.model.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO { //utworzenie klasy aby połączyć dwa obiekty w jeden model dla thymeleaf
        private UserDTO userDTO;
        private PatientDTO patientDTO;

}
