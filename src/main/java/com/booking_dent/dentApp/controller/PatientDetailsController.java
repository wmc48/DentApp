package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.database.repository.PatientRepository;
import com.booking_dent.dentApp.model.dto.PatientDTO;
import com.booking_dent.dentApp.security.UserEntity;
import com.booking_dent.dentApp.security.UserRepository;
import com.booking_dent.dentApp.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/patientView/patientDetails")
@AllArgsConstructor
public class PatientDetailsController {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PatientService patientService;


    @GetMapping
    public String showDashboard(Model model, Principal principal) {
        //pobierz nazwę użytkownika zalogowanego użytkownika
        String username = principal.getName();

        //znajdź użytkownika w bazie danych
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        //znajdź pacjenta powiązanego z użytkownikiem
        PatientEntity patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        //dodaj dane użytkownika i pacjenta do modelu
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", patient.getEmail());
        model.addAttribute("patientName", patient.getName());
        model.addAttribute("patientSurname", patient.getSurname());
        model.addAttribute("pesel", patient.getPesel());
        model.addAttribute("phone", patient.getPhone());

        return "patientView/patientDetails";
    }

    @PutMapping("/update")
    public String updatePatient(Principal principal, @ModelAttribute("patient") PatientDTO patientDTO) {
        //pobierz nazwę użytkownika aktualnie zalogowanego użytkownika
        String username = principal.getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        //pobierz identyfikator pacjenta na podstawie nazwy użytkownika
        PatientEntity patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        Long patientId = patient.getPatientId();

        patientService.updatePatient(patientDTO, patientId);

        return "redirect:/patientView/patientDetails";
    }

}
