package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import com.booking_dent.dentApp.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public String showEmployeesList(Model model) {
        List<EmployeeEntity> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employee";
    }


    @PostMapping("/add")
    public String addEmployee(@ModelAttribute("employeeDTO") EmployeeDTO employeeDTO) {
        employeeService.addEmployee(employeeDTO);
        return "redirect:/employee";
    }

    @DeleteMapping("/delete/{employeeId}")
    public String deleteStaff(@PathVariable("employeeId") Long employeeId) {
        employeeService.deleteById(employeeId);
        return "redirect:/employee";
    }

    @GetMapping("/show/{employeeId}")
    public String showEmployeeDetails(
            @PathVariable Long employeeId,
            Model model
    ) {

        EmployeeEntity employeeEntity = employeeService.findEmployeeById(employeeId);
        List<ScheduleDTO> schedules = employeeService.getEmployeeSchedules(employeeId);
        List<ShiftEntity> allShifts = employeeService.getAllShifts();

        // przekazanie pracownika, grafiku i zmian do modelu
        model.addAttribute("employee", employeeEntity);
        model.addAttribute("schedules", schedules);
        model.addAttribute("shifts", allShifts);
        return "employeeDetails";
    }

    @PostMapping("/scheduleadd")
    public String saveSchedule(@RequestParam("employeeId") Long employeeId,
                               @RequestParam("workDate") String workDateStr,
                               @RequestParam("shiftId") Integer shiftId) {

        employeeService.addSchedule(employeeId, workDateStr, shiftId);
        return "redirect:/employee/show/" + employeeId;
    }

    @PutMapping("/update/{employeeId}")
    public String updateEmployee(
            @PathVariable Long employeeId,
            @ModelAttribute("employee") EmployeeDTO employeeDTO
    ) {
        employeeService.updateEmployee(employeeDTO, employeeId);
        return "redirect:/employee/show/" + employeeId;
    }
}

