package com.booking_dent.dentApp.service;

import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.database.repository.PatientRepository;
import com.booking_dent.dentApp.model.dto.PatientDTO;
import com.booking_dent.dentApp.model.mapper.PatientMapper;
import com.booking_dent.dentApp.security.UserEntity;
import com.booking_dent.dentApp.security.UserRepository;
import com.booking_dent.dentApp.security.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@AllArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PatientMapper patientMapper;

    @Transactional
    public PatientEntity addPatient(PatientDTO patientDTO, UserEntity userEntity) {
        PatientEntity newPatient = PatientEntity.builder()
                .name(patientDTO.getName())
                .surname(patientDTO.getSurname())
                .pesel(patientDTO.getPesel())
                .phone(patientDTO.getPhone())
                .email(patientDTO.getEmail())
                .user(userEntity)
                .build();
        return patientRepository.save(newPatient);
    }

    public List<PatientDTO> getAllPatient() {
        return patientRepository.findAll().stream().map(patientMapper::toDTO).toList();
    }

    public List<PatientDTO> searchPatients(PatientDTO patientDTO) {
        List<PatientEntity> patientEntities = patientRepository.searchPatients(
                patientDTO.getName(),
                patientDTO.getSurname(),
                patientDTO.getPesel(),
                patientDTO.getPhone()
        );

        return patientEntities.stream()
                .map(patientMapper::toDTO)
                .toList();
    }


    public PatientDTO findPatientById(Long patientId) {
        return patientRepository.findById(patientId).map(patientMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("patient not found, patientId: " + patientId));
    }


    public void deleteById(Long patientId) {
        patientRepository.deleteById(patientId);
    }

    @Transactional
    public PatientEntity updatePatient(PatientDTO patientDTO, Long patientId, Principal principal) {
        PatientEntity patientEntity = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("patient not found, patientId: " + patientId));
        ;

        if (userService.checkRole(principal, "patient")) {
            patientEntity.setEmail(patientDTO.getEmail());
            patientEntity.setPhone(patientDTO.getPhone());
            return patientRepository.save(patientEntity);
        } else {
            patientEntity.setName(patientDTO.getName());
        patientEntity.setSurname(patientDTO.getSurname());
        patientEntity.setPesel(patientDTO.getPesel());
        patientEntity.setEmail(patientDTO.getEmail());
        patientEntity.setPhone(patientDTO.getPhone());
            return patientRepository.save(patientEntity);
        }
    }

    public Long getPatientIdByUsername(String username){

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        //pobierz identyfikator pacjenta na podstawie nazwy użytkownika
        PatientEntity patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        return patient.getPatientId();
    }


    public PatientDTO getPatientDTObyUsername(String username) {
        // Najpierw znajdź UserEntity po userId
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Następnie znajdź pacjenta powiązanego z tym użytkownikiem
        return patientRepository.findByUser(user)
                .map(patientMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }
}
