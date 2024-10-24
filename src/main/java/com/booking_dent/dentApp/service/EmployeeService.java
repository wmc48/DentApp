package com.booking_dent.dentApp.service;


import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ReservationEntity;
import com.booking_dent.dentApp.database.entity.ScheduleEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.database.repository.EmployeeRepository;
import com.booking_dent.dentApp.database.repository.ScheduleRepository;
import com.booking_dent.dentApp.database.repository.ShiftRepository;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import com.booking_dent.dentApp.model.mapper.ScheduleMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReservationService reservationService;
    private final ShiftRepository shiftRepository;

    private final ScheduleMapper scheduleMapper = ScheduleMapper.INSTANCE;

    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public EmployeeEntity addEmployee(EmployeeDTO employeeDTO) {
        EmployeeEntity newEmployee = EmployeeEntity.builder()
                .name(employeeDTO.getName())
                .surname(employeeDTO.getSurname())
                .email(employeeDTO.getEmail())
                .phone(employeeDTO.getPhone())
                .build();

        return employeeRepository.save(newEmployee);
    }

    public void deleteById(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    public EmployeeEntity findEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found, employeeId: " + employeeId));
    }

    public List<ScheduleDTO> getEmployeeSchedules(Long employeeId) {
        return scheduleRepository.findByEmployeeEmployeeIdOrderByWorkDateAsc(Long.valueOf(employeeId)).stream()
                .map(scheduleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ShiftEntity> getAllShifts() {
        return shiftRepository.findAll();
    }

    public EmployeeEntity updateEmployee(EmployeeDTO employeeDTO, Long employeeId) {
        EmployeeEntity employeeEntity = findEmployeeById(employeeId);

        employeeEntity.setName(employeeDTO.getName());
        employeeEntity.setSurname(employeeDTO.getSurname());
        employeeEntity.setEmail(employeeDTO.getEmail());
        employeeEntity.setPhone(employeeDTO.getPhone());

        return employeeRepository.save(employeeEntity);
    }

    public ScheduleEntity addSchedule(Long employeeId, String workDateStr, Integer shiftId) {
        LocalDate workDate = LocalDate.parse(workDateStr);
        EmployeeEntity employee = findEmployeeById(employeeId);
        ShiftEntity shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        ScheduleEntity newSchedule = ScheduleEntity.builder()
                .employee(employee)
                .workDate(workDate)
                .shift(shift)
                .build();

        return scheduleRepository.save(newSchedule);
    }

    public List<ScheduleDTO> getAvailableSchedules(Long employeeId) {
        List<ScheduleDTO> schedules = getEmployeeSchedules(employeeId);
        List<ReservationEntity> reservations = reservationService.getAllReservations();

        for (ScheduleDTO schedule : schedules) {
            Set<LocalTime> reservedHours = new HashSet<>();

            // sprawdź rezerwacje dla danego pracownika w danym dniu
            for (ReservationEntity reservation : reservations) {
                if (reservation.getEmployee().getEmployeeId().equals(employeeId) &&
                        reservation.getDateAndTime().toLocalDate().equals(schedule.getWorkDate())) {
                    reservedHours.add(reservation.getDateAndTime().toLocalTime());
                }
            }

            // ustalamy listę dostępnych godzin na podstawie godzin pracy w danym dniu
            List<String> availableHours = new ArrayList<>();
            LocalTime startTime = schedule.getStartTime();
            LocalTime endTime = schedule.getEndTime();

            while (startTime.isBefore(endTime)) {
                // dodaj godzinę tylko jeśli nie jest zarezerwowana
                if (!reservedHours.contains(startTime)) {
                    availableHours.add(startTime.toString());
                }
                startTime = startTime.plusHours(1);
            }
            schedule.setAvailableHours(availableHours);
        }
        return schedules;
    }
}
