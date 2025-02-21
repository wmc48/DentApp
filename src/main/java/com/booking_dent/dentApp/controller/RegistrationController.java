package com.booking_dent.dentApp.controller;
import com.booking_dent.dentApp.model.dto.AccountDTO;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.model.dto.PatientDTO;
import com.booking_dent.dentApp.model.dto.UserDTO;
import com.booking_dent.dentApp.security.UserEntity;
import com.booking_dent.dentApp.security.UserService;
import com.booking_dent.dentApp.service.EmployeeService;
import com.booking_dent.dentApp.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final PatientService patientService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        AccountDTO accountDTO = new AccountDTO();
        model.addAttribute("accountDTO", accountDTO);
        return "register";
    }

    @Transactional
    @PostMapping("/register/create")
    public String registerUser(
            @ModelAttribute("registerDTO") AccountDTO registerDTO,
            RedirectAttributes redirectAttributes
    ) {
        UserDTO userDTO = registerDTO.getUserDTO();
        PatientDTO patientDTO = registerDTO.getPatientDTO();

        UserEntity newUser = userService.createAccount(userDTO, 4);
        patientService.addPatient(patientDTO, newUser);

        redirectAttributes.addFlashAttribute("message", "Rejestracja zakończona sukcesem. Zaloguj się.");
        return "redirect:/index";
    }


    @GetMapping("/staffView/admin/createEmployee")
    public String showRegistrationFormEmpl(Model model) {
        //tworzenie pustego obiektu AccountDTO dla Thymeleaf
        model.addAttribute("accountDTO", new AccountDTO());
        return "staffView/admin/createEmployee";
    }
    //tworzenie konta pracownika przez admina
    @Transactional
    @PostMapping("/staffView/admin/createEmployee")
    public String registerUserEmployee(
            @ModelAttribute("registerDTO") AccountDTO registerDTO,
            RedirectAttributes redirectAttributes
    ) {
        UserDTO userDTO = registerDTO.getUserDTO();
        EmployeeDTO employeeDTO = registerDTO.getEmployeeDTO();
        //pobiera wartość "1 ADMIN", "3 LEKARZ", "2 PERSONEL"
        int roleId = userDTO.getRoleId();
        UserEntity newUser = userService.createAccount(userDTO, roleId);
        //ddanie pracownika powiązanego z użytkownikiem
        employeeService.addEmployee(employeeDTO, newUser);

        redirectAttributes.addFlashAttribute("message", "Rejestracja zakończona sukcesem. Zaloguj się.");
        return "redirect:/staffView/admin/dashboard";
    }
}
