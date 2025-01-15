package com.booking_dent.dentApp.service;

import com.booking_dent.dentApp.database.entity.forAdmin.ServiceCategoryEntity;
import com.booking_dent.dentApp.database.entity.forAdmin.ServiceEntity;
import com.booking_dent.dentApp.database.repository.ServiceCategoryRepository;
import com.booking_dent.dentApp.database.repository.ServiceRepository;
import com.booking_dent.dentApp.model.dto.forAdmin.ServiceCategoryDTO;
import com.booking_dent.dentApp.model.dto.forAdmin.ServiceDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PriceListService {

    private final ServiceCategoryRepository serviceCategoryRepository;
    private final ServiceRepository serviceRepository;

    public ServiceCategoryEntity addServiceCategory(ServiceCategoryDTO serviceCategoryDTO) {
        ServiceCategoryEntity newCategory = ServiceCategoryEntity.builder()
                .name(serviceCategoryDTO.getName())
                .build();
        return serviceCategoryRepository.save(newCategory);
    }

    public ServiceEntity addService(ServiceDTO serviceDTO) {
        ServiceEntity newService = ServiceEntity.builder()
                .name(serviceDTO.getName())
                .description(serviceDTO.getDescription())
                .price(serviceDTO.getPrice())
                .category(serviceDTO.getCategoryId())
                .build();
        return serviceRepository.save(newService);
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

    public List<ServiceDTO> getAllService() {
        return serviceRepository.findAll().stream()
                .map(service -> {
                    ServiceDTO dto = new ServiceDTO();
                    dto.setServiceId(service.getServiceId());
                    dto.setName(service.getName());
                    dto.setDescription(service.getDescription());
                    dto.setPrice(service.getPrice());
                    dto.setCategoryId(service.getCategory());


                    return dto;
                })
                .collect(Collectors.toList());
    }

}
