package com.booking_dent.dentApp.controller;


import com.booking_dent.dentApp.security.UserEntity;
import com.booking_dent.dentApp.model.dto.PatientDTO;
import com.booking_dent.dentApp.model.dto.RegisterDTO;
import com.booking_dent.dentApp.model.dto.UserDTO;
import com.booking_dent.dentApp.service.PatientService;
import com.booking_dent.dentApp.security.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final PatientService patientService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUserDTO(new UserDTO()); //ustawianie obiektu userDTO
        registerDTO.setPatientDTO(new PatientDTO()); //ustawianie obiektu patientDTO
        model.addAttribute("registerDTO", registerDTO);
        return "register";
    }

    @Transactional
    @PostMapping("/create")
    public String registerUser(
            @ModelAttribute("registerDTO") RegisterDTO registerDTO,
            RedirectAttributes redirectAttributes
    ) {
        UserDTO userDTO = registerDTO.getUserDTO();
        PatientDTO patientDTO = registerDTO.getPatientDTO();

        UserEntity newUser = userService.createAccount(userDTO);
        patientService.addPatient(patientDTO, newUser);

        redirectAttributes.addFlashAttribute("message", "Rejestracja zakończona sukcesem. Zaloguj się.");
        return "redirect:/login";
    }
}