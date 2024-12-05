package com.booking_dent.dentApp.service;

import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.database.repository.PatientRepository;
import com.booking_dent.dentApp.model.dto.PatientDTO;
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

    public List<PatientEntity> getAllPatient() {
        return patientRepository.findAll();
    }

    public List<PatientEntity> searchPatients(PatientDTO patientDTO) {
        return patientRepository.searchPatients(
                patientDTO.getName(),
                patientDTO.getSurname(),
                patientDTO.getPesel(),
                patientDTO.getPhone()
        );
    }

    public PatientEntity findPatientById(Long patientId){
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("patient not found, patientId: " + patientId));
    }

    public void deleteById(Long patientId) {
        patientRepository.deleteById(patientId);
    }

    @Transactional
    public PatientEntity updatePatient(PatientDTO patientDTO, Long patientId, Principal principal) {
        PatientEntity patientEntity = findPatientById(patientId);

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

    public PatientDTO toPatientDTO(PatientEntity patient) {
        return PatientDTO.builder()
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .name(patient.getName())
                .surname(patient.getSurname())
                .pesel(patient.getPesel())
                .build();
    }
}
