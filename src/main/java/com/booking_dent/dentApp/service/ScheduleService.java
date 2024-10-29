package com.booking_dent.dentApp.service;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ReservationEntity;
import com.booking_dent.dentApp.database.entity.ScheduleEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.database.repository.EmployeeRepository;
import com.booking_dent.dentApp.database.repository.ScheduleRepository;
import com.booking_dent.dentApp.database.repository.ShiftRepository;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import com.booking_dent.dentApp.model.mapper.ScheduleMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class ScheduleService {
    private final EmployeeRepository employeeRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReservationService reservationService;
    private final ShiftRepository shiftRepository;
    private final EmployeeService employeeService;


    private final ScheduleMapper scheduleMapper = ScheduleMapper.INSTANCE;

    public List<ScheduleDTO> getSchedulesForEmployee(Long employeeId) {
        List<ScheduleEntity> schedules = scheduleRepository.findByEmployeeEmployeeIdOrderByWorkDateAsc(employeeId);
        return schedules.stream()
                .map(scheduleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<ScheduleDTO> getAvailableSchedules(Long employeeId, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        List<ScheduleDTO> schedules = getSchedulesForEmployee(employeeId);

        // filtracja dostępnych godzin dla każdego harmonogramu
        List<ReservationEntity> reservations = reservationService.getAllReservations();
        for (ScheduleDTO schedule : schedules) {
            Set<LocalTime> reservedHours = new HashSet<>();
            for (ReservationEntity reservation : reservations) {
                if (reservation.getEmployee().getEmployeeId().equals(employeeId) &&
                        reservation.getDateAndTime().toLocalDate().equals(schedule.getWorkDate())) {
                    reservedHours.add(reservation.getDateAndTime().toLocalTime());
                }
            }

            List<String> availableHours = new ArrayList<>();
            LocalTime startTime = schedule.getStartTime();
            LocalTime endTime = schedule.getEndTime();

            while (startTime.isBefore(endTime)) {
                if (!reservedHours.contains(startTime)) {
                    availableHours.add(startTime.toString());
                }
                startTime = startTime.plusHours(1);
            }
            schedule.setAvailableHours(availableHours);
        }

        // paginacja wyników
        int start = Math.min((int) pageable.getOffset(), schedules.size());
        int end = Math.min((start + pageable.getPageSize()), schedules.size());
        List<ScheduleDTO> paginatedSchedules = schedules.subList(start, end);

        return new PageImpl<>(paginatedSchedules, pageable, schedules.size());
    }


    public List<LocalDate> addSchedule(Long employeeId, String workDateFromStr, String workDateToStr, Integer shiftId) {
        LocalDate workDateFrom = LocalDate.parse(workDateFromStr);
        LocalDate workDateTo = LocalDate.parse(workDateToStr);
        EmployeeEntity employee = employeeService.findEmployeeById(employeeId);
        ShiftEntity shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        List<LocalDate> existingDates = new ArrayList<>();
        for (LocalDate date = workDateFrom; !date.isAfter(workDateTo); date = date.plusDays(1)) {
            // sprawdź, czy grafik na dany dzień już istnieje
            if (!scheduleRepository.existsByEmployeeAndWorkDate(employee, date)) {
                ScheduleEntity newSchedule = ScheduleEntity.builder()
                        .employee(employee)
                        .workDate(date)
                        .shift(shift)
                        .build();

                scheduleRepository.save(newSchedule);
            }else {
                System.out.println("nie można dodać zmiany na dzień: " + date + "ponieważ zmiana na dany dzeń już istnieje");
                existingDates.add(date);
            }
        }
        return existingDates;
    }

    public String generateScheduleConflictMessage(List<LocalDate> existingDates) {
        if (existingDates.isEmpty()) {
            return null; // brak konfliktu, więc brak wiadomości
        }
        return "Nie można dodać zmiany na następujące dni, ponieważ zmiana na te dni już istnieje: "
                + existingDates.stream()
                .map(LocalDate::toString)
                .collect(Collectors.joining(", "));
    }

    public List<ScheduleDTO> getEmployeeSchedulesForMonth(Long employeeId, LocalDate month) {
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());

        return scheduleRepository.findByEmployeeEmployeeIdAndWorkDateBetweenOrderByWorkDateAsc(
                        employeeId,
                        startOfMonth,
                        endOfMonth
                ).stream()
                .map(scheduleMapper::toDTO)
                .collect(Collectors.toList());
    }

}
