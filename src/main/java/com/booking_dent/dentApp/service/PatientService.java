package com.booking_dent.dentApp.service;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.database.repository.PatientRepository;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.model.dto.PatientDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<PatientEntity> getAllPatient() {
        return patientRepository.findAll();
    }


    public List<PatientEntity> searchPatients(PatientDTO patientDTO) {
        return patientRepository.searchPatients(
                patientDTO.getName(),
                patientDTO.getSurname(),
                patientDTO.getPesel(),
                patientDTO.getEmail(),
                patientDTO.getPhone()
        );
    }

    public PatientEntity findPatientById(Long patientId){
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("patient not found, patientId: " + patientId));
    }

    public PatientEntity updateEmployee(PatientDTO patientDTO, Long patientId) {
        PatientEntity patientEntity = findPatientById(patientId);

        patientEntity.setName(patientDTO.getName());
        patientEntity.setSurname(patientDTO.getSurname());
        patientEntity.setPesel(patientDTO.getPesel());
        patientEntity.setEmail(patientDTO.getEmail());
        patientEntity.setPhone(patientDTO.getPhone());

        return patientRepository.save(patientEntity);
    }

    public void deleteById(Long patientId) {
        patientRepository.deleteById(patientId);
    }
}
