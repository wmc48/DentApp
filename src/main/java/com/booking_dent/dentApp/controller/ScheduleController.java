package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ScheduleEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.database.repository.EmployeeRepository;
import com.booking_dent.dentApp.database.repository.ScheduleRepository;
import com.booking_dent.dentApp.database.repository.ShiftRepository;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import com.booking_dent.dentApp.model.dto.ShiftDTO;
import com.booking_dent.dentApp.model.mapper.EmployeeMapper;
import com.booking_dent.dentApp.model.mapper.ScheduleMapper;
import com.booking_dent.dentApp.model.mapper.ShiftMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {

    private EmployeeRepository employeeRepository;

    private ShiftRepository shiftRepository;

    private ScheduleRepository scheduleRepository;

    private final EmployeeMapper employeeMapper = EmployeeMapper.INSTANCE;
    private final ShiftMapper shiftMapper = ShiftMapper.INSTANCE;
    private final ScheduleMapper scheduleMapper = ScheduleMapper.INSTANCE;

    @GetMapping
    public String showScheduleForm(Model model) {
        List<EmployeeDTO> employees = employeeRepository.findAll().stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
        List<ShiftDTO> shifts = shiftRepository.findAll().stream()
                .map(shiftMapper::toDTO)
                .collect(Collectors.toList());

        model.addAttribute("employees", employees);
        model.addAttribute("shifts", shifts);
        model.addAttribute("schedules", Collections.emptyList());
        return "schedule";
    }

    @GetMapping("/getSchedules")
    public String getSchedules(@RequestParam("employeeId") Long employeeId, Model model) {
        List<ScheduleDTO> schedules = scheduleRepository.findByEmployeeEmployeeId(employeeId).stream()
                .map(scheduleMapper::toDTO)
                .collect(Collectors.toList());

        List<EmployeeDTO> employees = employeeRepository.findAll().stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
        List<ShiftDTO> shifts = shiftRepository.findAll().stream()
                .map(shiftMapper::toDTO)
                .collect(Collectors.toList());

        model.addAttribute("employees", employees);
        model.addAttribute("shifts", shifts);
        model.addAttribute("schedules", schedules);
        return "schedule";
    }

//    @PostMapping("/schedule")
//    public String saveSchedule(@RequestParam("employeeId") Integer employeeId,
//                               @RequestParam("workDate") String workDateStr,
//                               @RequestParam("shiftId") Integer shiftId) {
//
//        LocalDate workDate = LocalDate.parse(workDateStr);
//        // Pobierz pracownika z serwisu
//        EmployeeEntity employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new RuntimeException("Employee not found"));
//
//        // Pobierz zmianę z serwisu
//        ShiftEntity shift = shiftRepository.findById(shiftId)
//                .orElseThrow(() -> new RuntimeException("Shift not found"));
//
//        ScheduleEntity newSchedule = ScheduleEntity.builder()
//                .employee(employee)
//                .workDate(workDate)
//                .shift(shift)
//                .build();
//
//        // Zapis harmonogramu
//        scheduleRepository.save(newSchedule);
//
//        // Przekierowanie do formularza po zapisie
//        return "redirect:/schedule";
//    }
}




