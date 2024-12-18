package com.booking_dent.dentApp.database.entity.forAdmin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceId;

    private String name;
    private String description;
    private BigDecimal price;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private ServiceCategoryEntity category;

}
