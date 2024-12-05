package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ReservationEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.model.dto.ReservationDTO;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import com.booking_dent.dentApp.security.UserService;
import com.booking_dent.dentApp.service.EmployeeService;
import com.booking_dent.dentApp.service.ReservationService;
import com.booking_dent.dentApp.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private  final EmployeeService employeeService;
    private final ScheduleService scheduleService;
    private final UserService userService;

    @PostMapping("/schedule") // niezbędne aby przekazać pathBarable do showDoctors
    public String processScheduleReservation(
            @RequestParam("employeeId") Long employeeId,
            @RequestParam("patientId") Long patientId
    ) {
        return "redirect:/reservation/showDoctor/" + employeeId + "/" + patientId;
    }

    @GetMapping("/showDoctor/{employeeId}/{patientId}")
    public String availableHoursForDoctor(
            @PathVariable Long employeeId,
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        EmployeeEntity employeeEntity = employeeService.findEmployeeById(employeeId);
        Page<ScheduleDTO> schedulesPage = scheduleService.getAvailableSchedules(employeeId, page);
        List<ShiftEntity> allShifts = employeeService.getAllShifts();

        model.addAttribute("employee", employeeEntity);
        model.addAttribute("schedules", schedulesPage.getContent());
        model.addAttribute("shifts", allShifts);
        model.addAttribute("patientId", patientId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", schedulesPage.getTotalPages());

        return "scheduleReservation";
    }


    @GetMapping
    public String showAllReservations(
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Page<ReservationEntity> reservationsPage = reservationService.getReservations(page, 10);
        model.addAttribute("reservations", reservationsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reservationsPage.getTotalPages());
        return "reservation";
    }

    @DeleteMapping("/delete/{reservationId}")
    public String deleteReservation(@PathVariable("reservationId") Long reservationId, Principal principal) {
        reservationService.deleteById(reservationId);
        //sprawdzenie ról użytkownika
        //authorities w chechRole przechowuje wszystkie role przypisane do aktualnie zalogowanego użytkownika.
        boolean isPatient = userService.checkRole(principal, "patient");

        if (isPatient) {
            return "redirect:/patientView/reservation";
        }
        return "redirect:/reservation";
    }

    @PostMapping("/add")
    public String addReservation(
            @RequestParam("workDate") String workDate,
            @RequestParam("selectedHour") String selectedHour,
            @RequestParam("employeeId") Long employeeId,
            @RequestParam("patientId") Long patientId,
            Principal principal,
            Model model) {
        //bład jeśliużytkownik próbuje dodać wizyte chociaż od date.now ma już dodana
        //czyli uż może mieć zaplanowaną w przó tylko jedną wizyte
        reservationService.addReservation(workDate, selectedHour, employeeId, patientId);

        //sprawdzenie ról użytkownika
        //authorities w chechRole przechowuje wszystkie role przypisane do aktualnie zalogowanego użytkownika.
        boolean isPatient = userService.checkRole(principal, "patient");
        if (isPatient) {
            return "redirect:/patientView/reservation";
        }
        return "redirect:/reservation";
    }

    @GetMapping("/search")
    public String searchReservations(
            @ModelAttribute ReservationDTO reservationDTO,
            @RequestParam(value = "page", defaultValue = "0") int page, // Default to the first page
            @RequestParam(value = "size", defaultValue = "10") int size, // Number of records per page
            Model model
    ) {
        // pobranie wyników wyszikiwania i przypisanie do page
        Page<ReservationEntity> reservationPage = reservationService.findReservation(reservationDTO, PageRequest.of(page, size));

        // dodanie atrybutów do medelu widoku
        model.addAttribute("reservations", reservationPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reservationPage.getTotalPages());
        return "reservation";
    }
}
