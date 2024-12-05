package com.booking_dent.dentApp.unitTests.service;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.database.entity.ReservationEntity;
import com.booking_dent.dentApp.database.repository.EmployeeRepository;
import com.booking_dent.dentApp.database.repository.PatientRepository;
import com.booking_dent.dentApp.database.repository.ReservationRepository;
import com.booking_dent.dentApp.model.dto.ReservationDTO;
import com.booking_dent.dentApp.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void getAllReservations() {
        // given
        List<ReservationEntity> mockReservations = Arrays.asList(
                createTestReservation1(),
                createTestReservation2()
        );
        when(reservationRepository.findAll()).thenReturn(mockReservations);

        // when
        List<ReservationEntity> reservations = reservationService.getAllReservations();

        // then
        assertEquals(2, reservations.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void getReservations_WithPagination() {
        // given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Page<ReservationEntity> mockPage = new PageImpl<>(
                Arrays.asList(createTestReservation1(), createTestReservation2())
        );

        when(reservationRepository.findAll(pageable)).thenReturn(mockPage);

        // when
        Page<ReservationEntity> result = reservationService.getReservations(page, size);

        // then
        assertEquals(2, result.getContent().size());
        verify(reservationRepository).findAll(pageable);
    }

    @Test
    void deleteById() {
        // given
        Long reservationId = 1L;

        // when
        reservationService.deleteById(reservationId);

        // then
        verify(reservationRepository).deleteById(reservationId);
    }

    @Test
    void findReservation() {
        // given
        ReservationDTO searchCriteria = ReservationDTO.builder()
                .patientName("Jan")
                .patientSurname("Kowalski")
                .patientPesel("12345678901")
                .employeeName("Adam")
                .employeeSurname("Nowak")
                .employeeId(1L)
                .reservationId(1L)
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<ReservationEntity> expectedPage = new PageImpl<>(
                Collections.singletonList(createTestReservation1())
        );

        when(reservationRepository.findReservation(
                searchCriteria.getPatientName(),
                searchCriteria.getPatientSurname(),
                searchCriteria.getPatientPesel(),
                searchCriteria.getEmployeeName(),
                searchCriteria.getEmployeeSurname(),
                searchCriteria.getEmployeeId(),
                searchCriteria.getReservationId(),
                pageable
        )).thenReturn(expectedPage);

        // when
        Page<ReservationEntity> result = reservationService.findReservation(searchCriteria, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(reservationRepository).findReservation(
                searchCriteria.getPatientName(),
                searchCriteria.getPatientSurname(),
                searchCriteria.getPatientPesel(),
                searchCriteria.getEmployeeName(),
                searchCriteria.getEmployeeSurname(),
                searchCriteria.getEmployeeId(),
                searchCriteria.getReservationId(),
                pageable
        );
    }

//    @Test
//    void addReservation_Success() {
//        // given
//        Long employeeId = 1L;
//        Long patientId = 1L;
//        LocalDateTime dateTime = LocalDateTime.now();
//
//        ReservationDTO reservationDTO = ReservationDTO.builder()
//                .employeeId(employeeId)
//                .patientId(patientId)
//                .dateAndTime(dateTime)
//                .build();
//
//        EmployeeEntity mockEmployee = EmployeeEntity.builder()
//                .employeeId(employeeId)
//                .name("Adam")
//                .surname("Nowak")
//                .build();
//
//        PatientEntity mockPatient = PatientEntity.builder()
//                .patientId(patientId)
//                .name("Jan")
//                .surname("Kowalski")
//                .build();
//
//        ReservationEntity expectedReservation = ReservationEntity.builder()
//                .employee(mockEmployee)
//                .patient(mockPatient)
//                .dateAndTime(dateTime)
//                .confirmed(false)
//                .build();
//
//        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(mockEmployee));
//        when(patientRepository.findById(patientId)).thenReturn(Optional.of(mockPatient));
//        when(reservationRepository.save(any(ReservationEntity.class))).thenReturn(expectedReservation);
//
//        // when
//        reservationService.addReservation(reservationDTO);
//
//        // then
//        verify(employeeRepository).findById(employeeId);
//        verify(patientRepository).findById(patientId);
//        verify(reservationRepository).save(any(ReservationEntity.class));
//    }

//    @Test
//    void addReservation_EmployeeNotFound() {
//        // given
//        ReservationDTO reservationDTO = ReservationDTO.builder()
//                .employeeId(1L)
//                .patientId(1L)
//                .dateAndTime(LocalDateTime.now())
//                .build();
//
//        when(employeeRepository.findById(any())).thenReturn(Optional.empty());
//
//        // when & then
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> reservationService.addReservation(reservationDTO)
//        );
//        assertEquals("Nie znaleziono pracownika o ID: 1", exception.getMessage());
//        verify(reservationRepository, never()).save(any());
//    }

//    @Test
//    void addReservation_PatientNotFound() {
//        // given
//        ReservationDTO reservationDTO = ReservationDTO.builder()
//                .employeeId(1L)
//                .patientId(1L)
//                .dateAndTime(LocalDateTime.now())
//                .build();
//
//        when(employeeRepository.findById(any())).thenReturn(Optional.of(
//                EmployeeEntity.builder().employeeId(1L).build()
//        ));
//        when(patientRepository.findById(any())).thenReturn(Optional.empty());
//
//        // when & then
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> reservationService.addReservation(reservationDTO)
//        );
//        assertEquals("Nie znaleziono pacjenta o ID: 1", exception.getMessage());
//        verify(reservationRepository, never()).save(any());
//    }

    // helper method
    private ReservationEntity createTestReservation1() {
        return ReservationEntity.builder()
                .reservationId(1L)
                .employee(EmployeeEntity.builder().employeeId(1L).name("Adam").surname("Nowak").build())
                .patient(PatientEntity.builder().patientId(1L).name("Jan").surname("Kowalski").build())
                .dateAndTime(LocalDateTime.now())
                .confirmed(false)
                .build();
    }

    private ReservationEntity createTestReservation2() {
        return ReservationEntity.builder()
                .reservationId(2L)
                .employee(EmployeeEntity.builder().employeeId(2L).name("Piotr").surname("Kowalski").build())
                .patient(PatientEntity.builder().patientId(2L).name("Anna").surname("Nowak").build())
                .dateAndTime(LocalDateTime.now().plusDays(1))
                .confirmed(false)
                .build();
    }
}