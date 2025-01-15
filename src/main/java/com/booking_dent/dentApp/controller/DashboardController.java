package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.service.DashboardService;
import com.booking_dent.dentApp.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private final EmployeeService employeeService;

    @GetMapping("/patientView/dashboard")
    public String showDashboard(Model model, Principal principal) {

        return dashboardService.showDashboardForRole(model, principal, "patientView/dashboard");
    }

    @GetMapping("/staffView/admin/dashboard")
    public String showDashboardReservationForAdmin(Model model, Principal principal) {
        //pobierz nazwę użytkownika aktualnie zalogowanego użytkownika i na jej podstawie znajdź id pacjenta
        //Long employeeId = employeeService.getEmployeeIdByUsername(principal.getName());

        //model.addAttribute("employee", employeeService.getEmployeeDTObyId(employeeId));
        model.addAttribute("employee", "włącz to wyżej po testach");
        return "/staffView/admin/dashboard";
    }

}
