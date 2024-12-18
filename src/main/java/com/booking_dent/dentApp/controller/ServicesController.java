package com.booking_dent.dentApp.controller;


import com.booking_dent.dentApp.model.dto.forAdmin.ServiceCategoryDTO;
import com.booking_dent.dentApp.service.ServicesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staffView/priceList")
@AllArgsConstructor
public class ServicesController {

    private final ServicesService servicesService;

    @GetMapping
    public String getAllCategories(Model model) {
        model.addAttribute("categories", servicesService.getAllServiceCategory());
        model.addAttribute("serviceCategoryDTO", new ServiceCategoryDTO()); // Form do dodania nowej kategorii
        return "/staffView/priceList";
    }

    @PostMapping("/addCategory")
    public String addServiceCategory(
            @ModelAttribute ServiceCategoryDTO serviceCategoryDTO,
            Model model) {

        servicesService.addServiceCategory(serviceCategoryDTO);
        model.addAttribute("categories", servicesService.getAllServiceCategory());
        model.addAttribute("serviceCategoryDTO", new ServiceCategoryDTO()); // Nowy formularz

        return "redirect:/staffView/priceList"; // Przekierowanie do tej samej strony
    }

    @DeleteMapping("/deleteCategory/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Long categoryId) {
        servicesService.deleteCategoryById(categoryId);
        return "redirect:/staffView/priceList";
    }

}
