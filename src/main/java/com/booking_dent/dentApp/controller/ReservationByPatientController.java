package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ReservationEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.database.repository.ReservationRepository;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import com.booking_dent.dentApp.service.EmployeeService;
import com.booking_dent.dentApp.service.PatientService;
import com.booking_dent.dentApp.service.ReservationService;
import com.booking_dent.dentApp.service.ScheduleService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/patientView/reservation")
@AllArgsConstructor
public class ReservationByPatientController {

    private final ReservationRepository reservationRepository;
    private final EmployeeService employeeService;
    private final ScheduleService scheduleService;
    private final PatientService patientService;
    private final ReservationService reservationService;


    @GetMapping
    public String showDashboard(Model model, Principal principal) {
        //pobierz nazwę użytkownika aktualnie zalogowanego użytkownika i na jej podstawie znajdź id pacjenta
        Long patientId = patientService.getPatientIdByUsername(principal.getName());

        List<ReservationEntity> reservations = reservationRepository.findByPatient_PatientId(patientId);
        List<EmployeeEntity> employees = employeeService.getAllEmployees();

        model.addAttribute("reservations", reservations);
        model.addAttribute("employees", employees);
        model.addAttribute("patientId", patientId);

        return "patientView/reservation";
    }

    @PostMapping("/schedule") // niezbędne aby przekazać pathBarable do showDoctor
    public String processScheduleReservation(@RequestParam("employeeId") Long employeeId) {
        return "redirect:/patientView/reservation/showDoctor/" + employeeId;
    }

    @GetMapping("/showDoctor/{employeeId}")
    public String availableHoursForDoctor(
            @PathVariable Long employeeId,
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        //pobierz nazwę użytkownika aktualnie zalogowanego użytkownika i na jej podstawie znajdź id pacjenta
        Long patientId = patientService.getPatientIdByUsername(principal.getName());

        EmployeeEntity employeeEntity = employeeService.findEmployeeById(employeeId);
        Page<ScheduleDTO> schedulesPage = scheduleService.getAvailableSchedules(employeeId, page);
        List<ShiftEntity> allShifts = employeeService.getAllShifts();

        model.addAttribute("employee", employeeEntity);
        model.addAttribute("schedules", schedulesPage.getContent());
        model.addAttribute("shifts", allShifts);
        model.addAttribute("patientId", patientId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", schedulesPage.getTotalPages());

        return "patientView/scheduleReservation";
    }
}
