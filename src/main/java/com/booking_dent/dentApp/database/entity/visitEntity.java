package com.booking_dent.dentApp.database.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "visit", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "date_and_time"})
})
public class visitEntity {

//    CREATE TABLE visit (
//            visit_id SERIAL PRIMARY KEY,
//            dr_id INT NOT NULL,
//            date_and_time DATETIME NOT NULL,
//            confirmed BOOLEAN DEFAULT FALSE,
//            FOREIGN KEY (dr_id) REFERENCES doctor(dr_id),
//    CONSTRAINT uniq_visit_dr UNIQUE (dr_id, date_and_time)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id")
    private Long visitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @Column(name = "date_and_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "confirmed")
    private boolean confirmed;
}
