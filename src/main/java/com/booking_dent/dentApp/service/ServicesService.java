package com.booking_dent.dentApp.service;

import com.booking_dent.dentApp.database.entity.forAdmin.ServiceCategoryEntity;
import com.booking_dent.dentApp.database.repository.ServiceCategoryRepository;
import com.booking_dent.dentApp.model.dto.forAdmin.ServiceCategoryDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ServicesService {

    private final ServiceCategoryRepository serviceCategoryRepository;

    public ServiceCategoryEntity addServiceCategory(ServiceCategoryDTO serviceCategoryDTO) {
        ServiceCategoryEntity newCategory = ServiceCategoryEntity.builder()
                .name(serviceCategoryDTO.getName())
                .build();
        return serviceCategoryRepository.save(newCategory);
    }

    public void deleteCategoryById(Long id) {
        serviceCategoryRepository.deleteById(id);
    }

    public ServiceCategoryEntity updateCategory(ServiceCategoryDTO serviceCategoryDTO, Long categoryId) {
        ServiceCategoryEntity categoryEntity = serviceCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found, employeeId: " + categoryId));

        categoryEntity.setName(serviceCategoryDTO.getName());

        return serviceCategoryRepository.save(categoryEntity);

    }

    public List<ServiceCategoryDTO> getAllServiceCategory() {
        return serviceCategoryRepository.findAll().stream()
                .map(category -> {
                    ServiceCategoryDTO dto = new ServiceCategoryDTO();
                    dto.setServiceCategoryId(category.getServiceCategoryId());
                    dto.setName(category.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
