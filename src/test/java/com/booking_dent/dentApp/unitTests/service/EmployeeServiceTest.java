package com.booking_dent.dentApp.unitTests.service;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.database.repository.EmployeeRepository;
import com.booking_dent.dentApp.database.repository.ScheduleRepository;
import com.booking_dent.dentApp.database.repository.ShiftRepository;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.service.EmployeeService;
import com.booking_dent.dentApp.unitTests.EntityFixtures;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void getAllEmployees() {
        //given
        List<EmployeeEntity> mockEmployees = Arrays.asList(EntityFixtures.testEmployee1(), EntityFixtures.testEmployee2());
        when(employeeRepository.findAll()).thenReturn(mockEmployees);

        //when
        List<EmployeeEntity> employees = employeeService.getAllEmployees();

        //then
        assertEquals(2, employees.size());
        assertEquals("Test1", employees.get(0).getName());
        assertEquals("Test2", employees.get(1).getName());

        verify(employeeRepository, times(1)).findAll();
    }


    @Test
    void addEmployee() {
        //given
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .name("Test1")
                .surname("TestoweNazw1")
                .email("email@email.com")
                .phone("8888888888")
                .build();
        EmployeeEntity expectedEmployee = EntityFixtures.testEmployee1();

        // mockowanie działania repozytorium - symulujemy zwracanie obiektu po zapisaniu
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(expectedEmployee);

        //when
        EmployeeEntity savedEmployee = employeeService.addEmployee(employeeDTO);

        assertEquals("Test1", savedEmployee.getName());
        assertEquals("TestoweNazw1", savedEmployee.getSurname());
        assertEquals("email@email.com", savedEmployee.getEmail());
        assertEquals("8888888888", savedEmployee.getPhone());

        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    void deleteById() {
        // given
        Long employeeId = 1L;

        // when:
        employeeService.deleteById(employeeId);

        // then: sprawdzenie, czy metoda deleteById z repozytorium została wywołana raz z odpowiednim ID
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    public void findEmployeeById_WhenEmployeeExists_ShouldReturnEmployee() {

        //given
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(EntityFixtures.testEmployee1()));

        //when
        EmployeeEntity foundEmployee = employeeService.findEmployeeById(employeeId);

        //then: Sprawdzenie, czy zwrócony pracownik jest poprawny
        assertEquals(employeeId, foundEmployee.getEmployeeId());
        assertEquals("Test1", foundEmployee.getName());
        assertEquals("TestoweNazw1", foundEmployee.getSurname());

        verify(employeeRepository, times(1)).findById(employeeId);

    }
    @Test
    void findEmployeeById_WhenEmployeeDoesNotExist_ShouldThrowException() {
        //given
        Long employeeId = 1L;

        // mockowanie repozytorium - zwracamy pusty optional
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // when
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeService.findEmployeeById(employeeId);
        });
        //then: czy wyjątek został rzucony
        assertEquals("Employee not found, employeeId: 1", exception.getMessage());

        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    void getAllShifts(){
        //given
        List<ShiftEntity> mockShifts = Arrays.asList(EntityFixtures.testShift1(), EntityFixtures.testShift2());
        when(shiftRepository.findAll()).thenReturn(mockShifts);

        //when
        List<ShiftEntity> shifts = employeeService.getAllShifts();

        //then
        assertEquals(2, shifts.size());
        assertEquals("morning", shifts.get(0).getName());
        assertEquals("afternoon", shifts.get(1).getName());

        verify(shiftRepository, times(1)).findAll();
    }

    @Test
    void updateEmployee(){
        //given
        Long employeeId = 1L;

        EmployeeEntity existingEmployee = EntityFixtures.testEmployee1();
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .name("edit")
                .surname("TestoweNazw1")
                .email("email@email.com")
                .phone("8888888888")
                .build();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);

        //when
        EmployeeEntity updatedEmployee = employeeService.updateEmployee(employeeDTO, employeeId);

        //then
        assertEquals("edit", updatedEmployee.getName());
        assertEquals("TestoweNazw1", updatedEmployee.getSurname());
        assertEquals("email@email.com", updatedEmployee.getEmail());
        assertEquals("8888888888", updatedEmployee.getPhone());

        // Weryfikacja, że metody findById i save zostały wywołane
        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).save(existingEmployee);
    }



//    @Test
//    public void addSchedule() {
//
//        // given
//        Long employeeId = 1L;
//        String workDateStr = "2024-10-07";
//        Integer shiftId = 1;
//        LocalDate expectedWorkDate = LocalDate.parse(workDateStr);
//
//        EmployeeEntity existingEmployee = EntityFixtures.testEmployee1();
//        ShiftEntity existingShift = EntityFixtures.testShift1();
//
//        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
//        when(shiftRepository.findById(shiftId)).thenReturn(Optional.of(existingShift));
//
//        ScheduleEntity newSchedule = ScheduleEntity.builder()
//                .employee(existingEmployee)
//                .workDate(expectedWorkDate)
//                .shift(existingShift)
//                .build();
//
//        when(scheduleRepository.save(any(ScheduleEntity.class))).thenReturn(newSchedule);
//
//        // when
//        ScheduleEntity newScheduleTest = employeeService.addSchedule(employeeId, workDateStr, shiftId);
//
//        // then
//        assertNotNull(newScheduleTest); //czy newScheduleTest nie jest nullem
//        assertEquals(existingEmployee, newScheduleTest.getEmployee());
//        assertEquals(expectedWorkDate, newScheduleTest.getWorkDate());
//        assertEquals(existingShift, newScheduleTest.getShift());
//
//        verify(employeeRepository).findById(employeeId);
//        verify(shiftRepository).findById(shiftId);
//        verify(scheduleRepository).save(any(ScheduleEntity.class));
//    }
}


