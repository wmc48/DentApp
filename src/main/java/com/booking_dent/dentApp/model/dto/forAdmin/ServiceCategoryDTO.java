package com.booking_dent.dentApp.model.dto.forAdmin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategoryDTO {

        private Long serviceCategoryId;
        private String name;
        private String description;
}
