package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ScheduleEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.database.repository.EmployeeRepository;
import com.booking_dent.dentApp.database.repository.ScheduleRepository;
import com.booking_dent.dentApp.database.repository.ShiftRepository;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import com.booking_dent.dentApp.model.mapper.ScheduleMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {

    private EmployeeRepository employeeRepository;
    private ScheduleRepository scheduleRepository;
    private ShiftRepository shiftRepository;

    private final ScheduleMapper scheduleMapper = ScheduleMapper.INSTANCE;

    @GetMapping
    public String showEmployeesList(Model model) {
        List<EmployeeEntity> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "employee";
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute("employeeDTO") EmployeeDTO employeeDTO) {
        EmployeeEntity newEmployee = EmployeeEntity.builder()
                .name(employeeDTO.getName())
                .surname(employeeDTO.getSurname())
                .email(employeeDTO.getEmail())
                .phone(employeeDTO.getPhone())
                .build();

        employeeRepository.save(newEmployee);
        return "redirect:/employee";
    }

    @DeleteMapping("/delete/{employeeId}")
    public String deleteStaff(@PathVariable("employeeId") Integer employeeId) {
        employeeRepository.deleteById(employeeId);
        return "redirect:/employee";
    }

    @GetMapping("/show/{employeeId}")
    public String showEmployeeDetails(
            @PathVariable Integer employeeId,
            Model model
    ) {
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found, employeeId: " + employeeId));

        // pobranie grafiku pracownika
        List<ScheduleDTO> schedules = scheduleRepository.findByEmployeeEmployeeIdOrderByWorkDateAsc(Long.valueOf(employeeId)).stream()
                .map(scheduleMapper::toDTO)
                .collect(Collectors.toList());

        // Pobranie listy zmian
        List<ShiftEntity> shifts = shiftRepository.findAll(); // Możesz dostosować tę metodę do własnych potrzeb

        // przekazanie pracownika, grafiku i zmian do modelu
        model.addAttribute("employee", employeeEntity);
        model.addAttribute("schedules", schedules);
        model.addAttribute("shifts", shifts);

        return "employeeDetails";
    }

    @PostMapping("/scheduleadd")
    public String saveSchedule(@RequestParam("employeeId") Integer employeeId,
                               @RequestParam("workDate") String workDateStr,
                               @RequestParam("shiftId") Integer shiftId) {

        LocalDate workDate = LocalDate.parse(workDateStr);

        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        ShiftEntity shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        ScheduleEntity newSchedule = ScheduleEntity.builder()
                .employee(employee)
                .workDate(workDate)
                .shift(shift)
                .build();

        //zapis grafiku
        scheduleRepository.save(newSchedule);
        return "redirect:/employee/show/" + employeeId;
    }

    @PutMapping("/update/{employeeId}")
    public String updateEmployee(
            @PathVariable Integer employeeId,
            @ModelAttribute("employee") EmployeeDTO employeeDTO
    ) {
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found, employeeId: " + employeeId));

        // zaktualizuj dane pracownika
        employeeEntity.setName(employeeDTO.getName());
        employeeEntity.setSurname(employeeDTO.getSurname());
        employeeEntity.setEmail(employeeDTO.getEmail());
        employeeEntity.setPhone(employeeDTO.getPhone());

        employeeRepository.save(employeeEntity);
        return "redirect:/employee/show/" + employeeId;
    }
}

