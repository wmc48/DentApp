package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.model.dto.AccountDTO;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.model.dto.UserDTO;
import com.booking_dent.dentApp.security.UserEntity;
import com.booking_dent.dentApp.security.UserService;
import com.booking_dent.dentApp.service.EmployeeService;
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
@RequestMapping("/staffView/register")
@AllArgsConstructor
public class RegistrationStaffController {
    private final UserService userService;
    private final EmployeeService employeeService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserDTO(new UserDTO()); //ustawianie obiektu userDTO
        accountDTO.setEmployeeDTO(new EmployeeDTO()); //ustawianie obiektu patientDTO
        model.addAttribute("accountDTO", accountDTO);
        return "/staffView/register";
    }

    @Transactional
    @PostMapping("/create")
    public String registerUser(
            @ModelAttribute("registerDTO") AccountDTO registerDTO,
            RedirectAttributes redirectAttributes
    ) {
        UserDTO userDTO = registerDTO.getUserDTO();
        EmployeeDTO employeeDTO = registerDTO.getEmployeeDTO();
        //pobiera wartość "1 ADMIN", "3 LEKARZ", "2 PERSONEL"
        int roleId = userDTO.getRoleId();
        UserEntity newUser = userService.createAccount(userDTO, roleId);
        // Dodanie pracownika powiązanego z użytkownikiem
        employeeService.addEmployee(employeeDTO, newUser);

        redirectAttributes.addFlashAttribute("message", "Rejestracja zakończona sukcesem. Zaloguj się.");
        return "redirect:/index";
    }
}
