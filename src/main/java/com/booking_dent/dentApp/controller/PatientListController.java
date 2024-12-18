package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.model.dto.PatientDTO;
import com.booking_dent.dentApp.service.EmployeeService;
import com.booking_dent.dentApp.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/staffView/patients")
@AllArgsConstructor
public class PatientListController {

    private final PatientService patientService;
    private final EmployeeService employeeService;

    @GetMapping
    public String showPatientList(Model model){
        List<PatientDTO> patients = patientService.getAllPatient();
        model.addAttribute("patients", patients);
        return "staffView/patients";
    }

    @GetMapping("/search")
    public String searchPatients(@ModelAttribute PatientDTO patientDTO, Model model) {
        List<PatientDTO> patients = patientService.searchPatients(patientDTO);
        model.addAttribute("patients", patients);

        //dodanie atrybutów do medelu, aby po wyszukiwaniu atrybuty dalej były widoczne w formularzu
        model.addAttribute("name", patientDTO.getName());
        model.addAttribute("surname", patientDTO.getSurname());
        model.addAttribute("pesel", patientDTO.getPesel());
        model.addAttribute("email", patientDTO.getEmail());
        model.addAttribute("phone", patientDTO.getPhone());
        return "staffView/patients";
    }

    @GetMapping("/show/{patientId}")
    public String showPatientDetails(
            @PathVariable Long patientId,
            Model model
    ){
        PatientDTO patient = patientService.findPatientById(patientId);
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        model.addAttribute("patient", patient);
        model.addAttribute("employees", employees);
        return "patientDetails";
    }

    @PutMapping("/update/{patientId}")
    public String updatePatient(@PathVariable Long patientId,
                                @ModelAttribute("patient") PatientDTO patientDTO,
                                Principal principal
    ){
        patientService.updatePatient(patientDTO, patientId, principal);
        return "redirect:/staffView/patients/show/" + patientId;
    }

    @DeleteMapping("/delete/{patientId}")
    public String deletePatient(@PathVariable("patientId") Long patientId) {
        patientService.deleteById(patientId);
        return "redirect:/staffView/patients";
    }
}
