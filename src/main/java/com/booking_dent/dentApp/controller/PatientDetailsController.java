package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.database.repository.PatientRepository;
import com.booking_dent.dentApp.model.dto.PatientDTO;
import com.booking_dent.dentApp.model.dto.UserDTO;
import com.booking_dent.dentApp.security.UserRepository;
import com.booking_dent.dentApp.security.UserService;
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
    private final UserService userService;

    @GetMapping
    public String showDashboard(Model model, Principal principal) {

        //szukamy i pobieramy usera jako dto po username
        UserDTO user = userService.getUserDTObyUsername(principal.getName());
        //szukamy i pobieramy patienta jako dto po username
        PatientDTO patient = patientService.getPatientDTObyUsername(principal.getName());

        model.addAttribute("patientDTO", patient);
        model.addAttribute("userDTO", user);
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
