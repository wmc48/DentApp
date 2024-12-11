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

}
