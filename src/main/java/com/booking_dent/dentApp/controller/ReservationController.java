package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ReservationEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.model.dto.ReservationDTO;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import com.booking_dent.dentApp.service.EmployeeService;
import com.booking_dent.dentApp.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam("employeeId") Long employeeId,
            @RequestParam("patientId") Long patientId
    ) {
        return "redirect:/reservation/showDoctor/" + employeeId + "/" + patientId;
    }


    @GetMapping("/showDoctor/{employeeId}/{patientId}")
    public String showDoctors(
            @PathVariable Long employeeId,
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

    @GetMapping
    public String showAllReservations(Model model) {
        List<ReservationEntity> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "reservation";
    }

    @DeleteMapping("/delete/{reservationId}")
    public String deleteStaff(@PathVariable("reservationId") Long reservationId) {
        reservationService.deleteById(reservationId);
        return "redirect:/reservation";
    }

    @PostMapping("/add")
    public String addReservation(
            @RequestParam("workDate") String workDate,
            @RequestParam("selectedHour") String selectedHour,
            @RequestParam("employeeId") Long employeeId,
            @RequestParam("patientId") Long patientId) {

        // połączenie daty i godziny
        String dateTimeString = workDate + "T" + selectedHour; // tworzenie formatu ISO 8601
        LocalDateTime dateAndTime = LocalDateTime.parse(dateTimeString);

        // tworzenie DTO
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setEmployeeId(employeeId);
        reservationDTO.setPatientId(patientId);
        reservationDTO.setDateAndTime(dateAndTime);

        reservationService.addReservation(reservationDTO);
        return "redirect:/reservation";
        //działa, dodawane są rekordy do bazy
    }

    @GetMapping("/search")
    public String searchReservations(@ModelAttribute ReservationDTO reservationDTO, Model model) {

        List<ReservationEntity> reservations = reservationService.findReservation(reservationDTO);
        model.addAttribute("reservations", reservations);
        return "reservation";
    }


}
