package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.model.dto.PatientDTO;
import com.booking_dent.dentApp.service.EmployeeService;
import com.booking_dent.dentApp.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/patient")
@AllArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final EmployeeService employeeService;

    @GetMapping
    public String showPatientList(Model model){
        List<PatientEntity> patients = patientService.getAllPatient();
        model.addAttribute("patients", patients);
        return "patient";
    }

    @GetMapping("/search")
    public String searchPatients(@ModelAttribute PatientDTO patientDTO, Model model) {
        List<PatientEntity> patients = patientService.searchPatients(patientDTO);
        model.addAttribute("patients", patients);

        //dodanie atrybutów do medelu, aby po wyszukiwaniu atrybuty dalej były widoczne w formularzu
        model.addAttribute("name", patientDTO.getName());
        model.addAttribute("surname", patientDTO.getSurname());
        model.addAttribute("pesel", patientDTO.getPesel());
        model.addAttribute("email", patientDTO.getEmail());
        model.addAttribute("phone", patientDTO.getPhone());
        return "patient";
    }

    @GetMapping("/show/{patientId}")
    public String showPatientDetails(
            @PathVariable Long patientId,
            Model model
    ){
        PatientEntity patient = patientService.findPatientById(patientId);
        List<EmployeeEntity> employees = employeeService.getAllEmployees();
        model.addAttribute("patient", patient);
        model.addAttribute("employees", employees);
        return "patientDetails";
    }

    @PutMapping("/update/{patientId}")
    public String updatePatient(@PathVariable Long patientId,
                                @ModelAttribute PatientDTO patientDTO
    ){
        patientService.updateEmployee(patientDTO,patientId);
        return "redirect:/patient/show/" + patientId;
    }

    @DeleteMapping("/delete/{patientId}")
    public String deleteStaff(@PathVariable("patientId") Long patientId) {
        patientService.deleteById(patientId);
        return "redirect:/patient";
    }
}
