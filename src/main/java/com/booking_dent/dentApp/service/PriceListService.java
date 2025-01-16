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

    //**********************CATEGORY

    public void addServiceCategory(ServiceCategoryDTO serviceCategoryDTO) {
        ServiceCategoryEntity newCategory = ServiceCategoryEntity.builder()
                .name(serviceCategoryDTO.getName())
                .build();
        serviceCategoryRepository.save(newCategory);
    }

    public void updateCategory(Long categoryId, ServiceCategoryDTO categoryDTO) {

        ServiceCategoryEntity categoryEntity = serviceCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("category not found, patientId: " + categoryId));

        categoryEntity.setName(categoryDTO.getName());
        serviceCategoryRepository.save(categoryEntity);
    }

    public void deleteCategoryById(Long id) {
        serviceCategoryRepository.deleteById(id);
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

    //***************** SERVICE

    public void addService(ServiceDTO serviceDTO) {
        ServiceEntity newService = ServiceEntity.builder()
                .name(serviceDTO.getName())
                .description(serviceDTO.getDescription())
                .price(serviceDTO.getPrice())
                .category(serviceDTO.getCategoryId())
                .build();
        serviceRepository.save(newService);
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

    public void updateService(Long serviceId, ServiceDTO serviceDTO) {
        ServiceEntity serviceEntity = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("service not found, patientId: " + serviceId));

        serviceEntity.setName(serviceDTO.getName());
        serviceEntity.setDescription(serviceDTO.getDescription());
        serviceEntity.setPrice(serviceDTO.getPrice());
        serviceRepository.save(serviceEntity);
    }
}
