package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.security.UserEntity;
import com.booking_dent.dentApp.security.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/patientView/dashboard")
@AllArgsConstructor
public class DashboardController {
    private final UserRepository userRepository;

    @GetMapping
    public String showDashboard(Model model, Principal principal) {
        //pobieranie szczegółów użytkownika z Principal (username)
        String username = principal.getName();

        // Pobierz dane użytkownika (np. z bazy)
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Dodaj dane do modelu
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("username", user.getUsername());

        return "patientView/dashboard";
    }

}
