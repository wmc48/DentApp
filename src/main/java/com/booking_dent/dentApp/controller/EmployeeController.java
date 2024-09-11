package com.booking_dent.dentApp.controller;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.repository.EmployeeRepository;
import com.booking_dent.dentApp.model.EmployeeDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    @GetMapping
    public String showEmployeesList (Model model){
        List<EmployeeEntity> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "employee";
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute("employeeDTO") EmployeeDTO employeeDTO){
        EmployeeEntity newEmployee = EmployeeEntity.builder()
                .name(employeeDTO.getName())
                .surname(employeeDTO.getSurname())
                .email(employeeDTO.getEmail())
                .phone(employeeDTO.getPhone())
                .build();

        employeeRepository.save(newEmployee);
        return "redirect:/employee";
    }

    @DeleteMapping("/delete/{employeeId}")
    public String deleteStaff(@PathVariable("employeeId") Integer employeeId){
        employeeRepository.deleteById(employeeId);
        return "redirect:/employee";
    }

    @GetMapping("/show/{employeeId}")
    public String showEmployeeDetails(
            @PathVariable Integer employeeId,
            Model model
    ){
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("employee not found, employeeId: [%s]".formatted(employeeId)));
        model.addAttribute("employee", employeeEntity);
        return "employeeDetails";
    }

    @PutMapping("/update/{employeeId}")
    public String updateEmployee(
            @PathVariable Integer employeeId,
            @ModelAttribute("employee") EmployeeDTO employeeDTO
    ) {
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found, employeeId: " + employeeId));

        // Zaktualizuj dane pracownika
        employeeEntity.setName(employeeDTO.getName());
        employeeEntity.setSurname(employeeDTO.getSurname());
        employeeEntity.setEmail(employeeDTO.getEmail());
        employeeEntity.setPhone(employeeDTO.getPhone());

        // Zapisz aktualizację
        employeeRepository.save(employeeEntity);

        // Przekierowanie po aktualizacji
        return "redirect:/employee/show/" + employeeId;
    }


}

