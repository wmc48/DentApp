package com.booking_dent.dentApp.service;


import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.database.repository.EmployeeRepository;
import com.booking_dent.dentApp.database.repository.ShiftRepository;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.model.dto.MonthDetails;
import com.booking_dent.dentApp.security.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;

    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public EmployeeEntity addEmployee(EmployeeDTO employeeDTO, UserEntity userEntity) {
        EmployeeEntity newEmployee = EmployeeEntity.builder()
                .name(employeeDTO.getName())
                .surname(employeeDTO.getSurname())
                .email(employeeDTO.getEmail())
                .phone(employeeDTO.getPhone())
                .user(userEntity)
                .build();
        return employeeRepository.save(newEmployee);
    }

    public void deleteById(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    public EmployeeEntity findEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found, employeeId: " + employeeId));
    }

    public List<ShiftEntity> getAllShifts() {
        return shiftRepository.findAll();
    }

    public EmployeeEntity updateEmployee(EmployeeDTO employeeDTO, Long employeeId) {
        EmployeeEntity employeeEntity = findEmployeeById(employeeId);

        employeeEntity.setName(employeeDTO.getName());
        employeeEntity.setSurname(employeeDTO.getSurname());
        employeeEntity.setEmail(employeeDTO.getEmail());
        employeeEntity.setPhone(employeeDTO.getPhone());

        return employeeRepository.save(employeeEntity);
    }

    //dla wyświetlania widoku danego miesiąca
    //******************************************************************************************************************
    //dodać to do wyświetlania gotowych rezerwacji oraz tworzenia rezerwacji ******************************************************************************************************************
    //******************************************************************************************************************
    public MonthDetails getMonthDetails(String monthParam) {

        // ustal miesiąc na podstawie parametru lub ustaw na bieżący miesiąc
        LocalDate currentMonth = (monthParam != null) ? LocalDate.parse(monthParam + "-01") : LocalDate.now().withDayOfMonth(1);

        // daty poprzedniego i następnego miesiąca
        LocalDate previousMonth = currentMonth.minusMonths(1);
        LocalDate nextMonth = currentMonth.plusMonths(1);

        String currentMonthName = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"));

        return new MonthDetails(currentMonth, previousMonth, nextMonth, currentMonthName);
    }

}
