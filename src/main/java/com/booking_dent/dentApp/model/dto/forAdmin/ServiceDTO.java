package com.booking_dent.dentApp.model.dto.forAdmin;

import com.booking_dent.dentApp.database.entity.forAdmin.ServiceCategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {

    private Long serviceId;
    private String name;
    private String description;
    private BigDecimal price;
    private String notes;
    private ServiceCategoryEntity categoryId;
}
