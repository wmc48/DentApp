package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.database.repository.PatientRepository;
import com.booking_dent.dentApp.model.dto.PatientDTO;
import com.booking_dent.dentApp.security.UserEntity;
import com.booking_dent.dentApp.security.UserRepository;
import com.booking_dent.dentApp.service.PatientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        PatientDTO patientDTO = patientService.toPatientDTO(patient);//aby podstawiły się uzupełnione pola w form edit

        model.addAttribute("userId", user.getUserId());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", patient.getEmail());
        model.addAttribute("patientName", patient.getName());
        model.addAttribute("patientSurname", patient.getSurname());
        model.addAttribute("pesel", patient.getPesel());
        model.addAttribute("phone", patient.getPhone());
        model.addAttribute("patientDTO", patientDTO);
        return "patientView/patientDetails";
    }

    @PostMapping("/update")
    public String updatePatient(
            Principal principal,
            @ModelAttribute("patient") @Valid PatientDTO patientDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        //walidacja danych wejściowych
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nieprawidłowe dane. Spróbuj ponownie.");
            return "redirect:/patientView/patientDetails";
        }

        Long patientId = patientService.getPatientIdByUsername(principal.getName());

        //zaktualizuj dane pacjenta
        try {
            patientService.updatePatient(patientDTO, patientId, principal);
            redirectAttributes.addFlashAttribute("successMessage", "Dane pacjenta zostały zaktualizowane.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd podczas aktualizacji danych pacjenta.");
        }
        return "redirect:/patientView/patientDetails";
    }
}
