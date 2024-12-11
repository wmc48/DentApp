package com.booking_dent.dentApp.service;

import com.booking_dent.dentApp.security.UserEntity;
import com.booking_dent.dentApp.security.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;

@Service
@AllArgsConstructor
public class DashboardService {
    private final UserRepository userRepository;

    public String showDashboardForRole(Model model, Principal principal, String viewName) {
        //metoda wyodrębniona aby nie dublować jej w controllerze do dwóch różnych widoków tj. staffViev oraz patientView
        // pobieranie szczegółów użytkownika z Principal (username)
        String username = principal.getName();

        // pobierz dane użytkownika z bazy
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        model.addAttribute("userId", user.getUserId());
        model.addAttribute("username", user.getUsername());
        //zwracamy ten sam widok który przekazaliśmy
        return viewName;
    }



}
