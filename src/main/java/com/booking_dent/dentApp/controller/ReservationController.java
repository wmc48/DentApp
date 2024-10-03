package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.database.repository.EmployeeRepository;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import com.booking_dent.dentApp.service.EmployeeService;
import com.booking_dent.dentApp.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private  final EmployeeService employeeService;

    @PostMapping("/schedule")
    public String processScheduleReservation(
            @RequestParam("employeeId") Integer employeeId,
            @RequestParam("patientId") Long patientId
    ) {
        return "redirect:/reservation/showDoctor/" + employeeId + "/" + patientId;
    }


    @GetMapping("/showDoctor/{employeeId}/{patientId}")
    public String showDoctors(
            @PathVariable Integer employeeId,
            @PathVariable Long patientId,
            Model model
    ) {
        EmployeeEntity employeeEntity = employeeService.findEmployeeById(employeeId);
        List<ScheduleDTO> schedules = employeeService.getEmployeeSchedules(employeeId);
        List<ShiftEntity> allShifts = employeeService.getAllShifts();

        for (ScheduleDTO schedule : schedules) { //ustala liste dostępnych godzin na podstawie godzin pracy w danym dniu
            schedule.setAvailableHours(employeeService.getAvailableHours(schedule.getStartTime(), schedule.getEndTime()));
        }

        // przekazanie danych do modelu
        model.addAttribute("employee", employeeEntity);
        model.addAttribute("schedules", schedules);
        model.addAttribute("shifts", allShifts);
        model.addAttribute("patientId", patientId);

        // zwracamy widok "scheduleReservation"
        return "scheduleReservation";
    }
}
