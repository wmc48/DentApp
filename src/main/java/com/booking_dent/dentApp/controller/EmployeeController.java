package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.model.dto.MonthDetails;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import com.booking_dent.dentApp.service.EmployeeService;
import com.booking_dent.dentApp.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/staffView/employee")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ScheduleService scheduleService;

    @GetMapping
    public String showEmployeesList(Model model) {
        List<EmployeeEntity> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "/staffView/employee";
    }

    @DeleteMapping("/delete/{employeeId}")
    public String deleteStaff(@PathVariable("employeeId") Long employeeId) {
        employeeService.deleteById(employeeId);
        return "redirect:/staffView/employee";
    }

    @GetMapping("/show/{employeeId}")
    public String showEmployeeDetails(
            @PathVariable Long employeeId,
            @RequestParam(value = "month", required = false) String monthParam,
            Model model
    ) {
        EmployeeEntity employeeEntity = employeeService.findEmployeeById(employeeId);

        //dla wyświetlania widoku danego miesiąca
        MonthDetails monthDetails = employeeService.getMonthDetails(monthParam);

        List<ScheduleDTO> schedules = scheduleService.getEmployeeSchedulesForMonth(employeeId, monthDetails.getCurrentMonth());
        List<ShiftEntity> allShifts = employeeService.getAllShifts();

        // przekazanie pracownika, grafiku i zmian do modelu
        model.addAttribute("employee", employeeEntity);
        model.addAttribute("schedules", schedules);
        model.addAttribute("shifts", allShifts);
        model.addAttribute("previousMonth", monthDetails.getPreviousMonth().getYear() + "-" + String.format("%02d", monthDetails.getPreviousMonth().getMonthValue()));
        model.addAttribute("nextMonth", monthDetails.getNextMonth().getYear() + "-" + String.format("%02d", monthDetails.getNextMonth().getMonthValue()));
        model.addAttribute("currentMonthName", monthDetails.getCurrentMonthName());

        return "staffView/employeeDetails";
    }

    @PostMapping("/scheduleadd")
    public String saveSchedule(@RequestParam("employeeId") Long employeeId,
                               @RequestParam("workDateFrom") String workDateFromStr,
                               @RequestParam("workDateTo") String workDateToStr,
                               @RequestParam("shiftId") Integer shiftId,
                               RedirectAttributes redirectAttributes) {

        List<LocalDate> existingDates = scheduleService.addSchedule(employeeId, workDateFromStr, workDateToStr, shiftId);
        String errorMessage = scheduleService.generateScheduleConflictMessage(existingDates);

        if (errorMessage != null) {
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return "redirect:/staffView/employee/show/" + employeeId;
    }

    @PutMapping("/update/{employeeId}")
    public String updateEmployee(
            @PathVariable Long employeeId,
            @ModelAttribute("employee") EmployeeDTO employeeDTO
    ) {
        employeeService.updateEmployee(employeeDTO, employeeId);
        return "redirect:/staffView/employee/show/" + employeeId;
    }
}

