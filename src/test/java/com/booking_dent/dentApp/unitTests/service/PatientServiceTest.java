package com.booking_dent.dentApp.unitTests.service;

import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.database.repository.PatientRepository;
import com.booking_dent.dentApp.model.dto.PatientDTO;
import com.booking_dent.dentApp.service.PatientService;
import com.booking_dent.dentApp.unitTests.utility.PatientFixtures;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void getAllPatient() {
        //given
        List<PatientEntity> mockPatients = Arrays.asList(PatientFixtures.createTestPatient1(), PatientFixtures.createTestPatient2());
        when(patientRepository.findAll()).thenReturn(mockPatients);

        //when
        List<PatientEntity> patients = patientService.getAllPatient();

        //then
        assertEquals(2, patients.size());
        assertEquals("Kowalski", patients.get(0).getSurname());
        assertEquals("Testowy", patients.get(1).getSurname());

        verify(patientRepository, times(1)).findAll();
    }

//    @Test
//    void addPatient(){
//        //given
//        PatientDTO patientDTO = PatientDTO.builder()
//                .name("Jan")
//                .surname("Kowalski")
//                .email("jan.kowalski@gmail.com")
//                .phone("123456789")
//                .pesel("12345678901")
//                .build();
//        PatientEntity expectedPatient = PatientFixtures.createTestPatient1();
//        when(patientRepository.save(any(PatientEntity.class))).thenReturn(expectedPatient);
//
//        //when
//        PatientEntity savedPatient = patientService.addPatient(patientDTO);
//
//        //then
//        assertEquals("Jan", savedPatient.getName());
//        assertEquals("Kowalski", savedPatient.getSurname());
//        assertEquals("jan.kowalski@gmail.com", savedPatient.getEmail());
//        assertEquals("123456789", savedPatient.getPhone());
//        assertEquals("12345678901", savedPatient.getPesel());
//
//        verify(patientRepository, times(1)).save(any(PatientEntity.class));
//    }

//    @Test
//    void updatePatient() {
//        //given
//        Long patientId = 1L;
//        PatientEntity existingPatient = PatientFixtures.createTestPatient1();
//
//        PatientDTO patientDTO = PatientFixtures.testPatientDto();
//
//        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
//        when(patientRepository.save(existingPatient)).thenReturn(existingPatient);
//
//        //when
//        PatientEntity updatePatient = patientService.updatePatient(patientDTO, patientId);
//
//        //them
//        assertEquals(1L, updatePatient.getPatientId());
//        assertEquals("Jan", updatePatient.getName());
//        assertEquals("Dto", updatePatient.getSurname());
//        assertEquals("12378901", updatePatient.getPesel());
//
//        verify(patientRepository).findById(patientId);
//        verify(patientRepository).save(existingPatient);
//    }

    @Test
    void deleteById() {
        // given
        Long patientId = 1L;

        // when:
        patientRepository.deleteById(patientId);

        // then: sprawdzenie, czy metoda deleteById z repozytorium została wywołana raz z odpowiednim ID
        verify(patientRepository, times(1)).deleteById(patientId);
    }

    @Test
    public void findPatientById_WhenPatientExists_ShouldReturnPatient() {
        //given
        Long patientId = 1L;
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(PatientFixtures.createTestPatient1()));

        //when
        PatientEntity foundPatient = patientService.findPatientById(patientId);

        //then: sprawdzenie, czy zwrócony pracownik jest poprawny
        assertEquals(patientId, foundPatient.getPatientId());
        assertEquals("Jan", foundPatient.getName());
        assertEquals("Kowalski", foundPatient.getSurname());
        verify(patientRepository, times(1)).findById(patientId);

    }
    @Test
    void findPatientById_WhenPatientDoesNotExist_ShouldThrowException() {
        //given
        Long patientId = 1L;

        // mockowanie repozytorium - zwracamy pusty optional
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // when
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            patientService.findPatientById(patientId);
        });
        //then: czy wyjątek został rzucony
        assertEquals("patient not found, patientId: 1", exception.getMessage());
        verify(patientRepository, times(1)).findById(patientId);
    }
    @Test
    void searchPatients_WithAllParameters() {
        // given
        PatientDTO searchCriteria = PatientDTO.builder()
                .name("Jan")
                .surname("Kowalski")
                .pesel("12345678901")
                .phone("123456789")
                .build();

        List<PatientEntity> expectedResults = Collections.singletonList(
                PatientFixtures.createTestPatient1()
        );

        when(patientRepository.searchPatients(
                searchCriteria.getName(),
                searchCriteria.getSurname(),
                searchCriteria.getPesel(),
                searchCriteria.getPhone()
        )).thenReturn(expectedResults);

        // when
        List<PatientEntity> results = patientService.searchPatients(searchCriteria);

        // then
        assertEquals(1, results.size());
        assertEquals("Jan", results.get(0).getName());
        assertEquals("Kowalski", results.get(0).getSurname());

        verify(patientRepository).searchPatients(
                searchCriteria.getName(),
                searchCriteria.getSurname(),
                searchCriteria.getPesel(),
                searchCriteria.getPhone()
        );
    }

    @Test
    void searchPatients_WithPartialParameters() {
        // given
        PatientDTO searchCriteria = PatientDTO.builder()
                .surname("Kowalski")
                .phone("123456789")
                .build();

        List<PatientEntity> expectedResults = Arrays.asList(
                PatientFixtures.createTestPatient1(),
                PatientFixtures.createTestPatient2()
        );

        when(patientRepository.searchPatients(
                null,
                searchCriteria.getSurname(),
                null,
                searchCriteria.getPhone()
        )).thenReturn(expectedResults);

        // when
        List<PatientEntity> results = patientService.searchPatients(searchCriteria);

        // then
        assertEquals(2, results.size());
        verify(patientRepository).searchPatients(
                null,
                "Kowalski",
                null,
                "123456789"
        );
    }

    @Test
    void searchPatients_NoResults() {
        // given
        PatientDTO searchCriteria = PatientDTO.builder()
                .name("Nieistniejący")
                .build();

        when(patientRepository.searchPatients(
                searchCriteria.getName(),
                null,
                null,
                null
        )).thenReturn(Collections.emptyList());

        // when
        List<PatientEntity> results = patientService.searchPatients(searchCriteria);

        // then
        assertTrue(results.isEmpty());
        verify(patientRepository).searchPatients(
                "Nieistniejący",
                null,
                null,
                null
        );
    }
}