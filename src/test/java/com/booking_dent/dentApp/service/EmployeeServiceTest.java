package com.booking_dent.dentApp.service;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.database.repository.EmployeeRepository;
import com.booking_dent.dentApp.database.repository.ShiftRepository;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ShiftRepository shiftRepository;

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

        //then
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
}


