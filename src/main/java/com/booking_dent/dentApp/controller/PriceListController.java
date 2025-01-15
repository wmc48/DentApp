package com.booking_dent.dentApp.controller;


import com.booking_dent.dentApp.model.dto.forAdmin.ServiceCategoryDTO;
import com.booking_dent.dentApp.model.dto.forAdmin.ServiceDTO;
import com.booking_dent.dentApp.service.PriceListService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class PriceListController {

    private final PriceListService priceListService;

    @GetMapping("/staffView/admin/priceList")
    public String getAllCategories(Model model) {
        model.addAttribute("categories", priceListService.getAllServiceCategory());
        model.addAttribute("services", priceListService.getAllService());
        model.addAttribute("serviceDTO", new ServiceDTO()); // Przekazanie pustego obiektu do formularza
        model.addAttribute("serviceCategoryDTO", new ServiceCategoryDTO()); // form do dodania nowej kategorii
        return "/staffView/admin/priceList";
    }

    @PostMapping("/staffView/admin/priceList/addCategory")
    public String addServiceCategory(
            @ModelAttribute ServiceCategoryDTO serviceCategoryDTO,
            Model model) {

        priceListService.addServiceCategory(serviceCategoryDTO);
        model.addAttribute("categories", priceListService.getAllServiceCategory());
        model.addAttribute("serviceCategoryDTO", new ServiceCategoryDTO()); // Nowy formularz

        return "redirect:/staffView/admin/priceList"; // Przekierowanie do tej samej strony
    }

    @PostMapping("/staffView/admin/priceList/addService")
    public String addService(
            @ModelAttribute ServiceDTO serviceDTO,
            Model model) {

        priceListService.addService(serviceDTO);
        model.addAttribute("services", priceListService.getAllService());
        model.addAttribute("serviceDTO", new ServiceDTO()); // Nowy formularz

        return "redirect:/staffView/admin/priceList";
    }


    @DeleteMapping("/staffView/admin/priceList/deleteCategory/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Long categoryId) {
        priceListService.deleteCategoryById(categoryId);
        return "redirect:/staffView/admin/priceList";
    }

}
