package com.booking_dent.dentApp.unitTests.controller;

import com.booking_dent.dentApp.controller.ReservationController;
import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ReservationEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.model.dto.ReservationDTO;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import com.booking_dent.dentApp.service.EmployeeService;
import com.booking_dent.dentApp.service.ReservationService;
import com.booking_dent.dentApp.service.ScheduleService;
import com.booking_dent.dentApp.unitTests.utility.EmployeeFixtures;
import com.booking_dent.dentApp.unitTests.utility.ReservationFixtures;
import com.booking_dent.dentApp.unitTests.utility.ScheduleFixtures;
import com.booking_dent.dentApp.unitTests.utility.ShiftFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc; //symulowanie żądań HTTP do kontrolera w testach

    @MockBean
    private ReservationService reservationService;
    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private ScheduleService scheduleService;


    @Test
    void processScheduleReservation() throws Exception {
        // given
        Long employeeId = 1L;
        Long patientId = 2L;

        // when & then
        mockMvc.perform(post("/reservation/schedule")
                        .param("employeeId", employeeId.toString())
                        .param("patientId", patientId.toString()))
                .andExpect(status().is3xxRedirection()) //czy jest przekierowanie
                .andExpect(redirectedUrl("/reservation/showDoctor/" + employeeId + "/" + patientId)); //czy ścieżka przekierowania jest poprawna
    }

    @Test
    void availableHoursForDoctor() throws Exception {
        //given
        Long employeeId = 1L;
        Long patientId = 456L;
        int page = 0;

        EmployeeEntity employee = EmployeeFixtures.testEmployee1();
        List<ScheduleDTO> schedules = Arrays.asList(ScheduleFixtures.testSchedule1(), ScheduleFixtures.testSchedule2());
        Page<ScheduleDTO> schedulePage = new PageImpl<>(schedules, PageRequest.of(page, 10), 2);
        List<ShiftEntity> shifts = Arrays.asList(ShiftFixtures.testShift1(), ShiftFixtures.testShift2());

        when(employeeService.findEmployeeById(employeeId)).thenReturn(employee);
        when(scheduleService.getAvailableSchedules(employeeId, page)).thenReturn(schedulePage);
        when(employeeService.getAllShifts()).thenReturn(shifts);

        //when && then
        mockMvc.perform(get("/reservation/showDoctor/{employeeId}/{patientId}", employeeId, patientId)
                        .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(view().name("scheduleReservation"))
                .andExpect(model().attribute("employee", employee))
                .andExpect(model().attribute("schedules", schedules))
                .andExpect(model().attribute("shifts", shifts))
                .andExpect(model().attribute("patientId", patientId))
                .andExpect(model().attribute("currentPage", page))
                .andExpect(model().attribute("totalPages", 1));

        verify(employeeService).findEmployeeById(employeeId);
        verify(scheduleService).getAvailableSchedules(employeeId, page);
        verify(employeeService).getAllShifts();
    }

    @Test
    void showAllReservations() throws Exception{
        //given
        int page = 0;
        int pageSize = 10;

        List<ReservationEntity> existingReservations = Arrays.asList(
                ReservationFixtures.testReservation1(), ReservationFixtures.testReservation2()
        );
        Page<ReservationEntity> reservationsPage = new PageImpl<>(
                existingReservations, PageRequest.of(page, pageSize), 2
        );

        when(reservationService.getReservations(page, pageSize)).thenReturn(reservationsPage);

        // when & then
        mockMvc.perform(get("/reservation").param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"))
                .andExpect(model().attribute("reservations", existingReservations))
                .andExpect(model().attribute("currentPage", page))
                .andExpect(model().attribute("totalPages", reservationsPage.getTotalPages()));

        verify(reservationService).getReservations(page, pageSize);
    }

    @Test
    void deleteReservation() throws Exception {
        //given
        Long reservationId = 1L;

        //when & then
        mockMvc.perform(delete("/reservation/delete/{reservationId}", reservationId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/reservation"));

        verify(reservationService, times(1)).deleteById(reservationId);
    }

    @Test
    void addReservation() throws Exception {
        //given
        String workDate = "2024-11-14";
        String selectedHour = "10:00";
        Long employeeId = 1L;
        Long patientId = 2L;

        //when & then
        mockMvc.perform(post("/reservation/add")
                        .param("workDate", workDate)
                        .param("selectedHour", selectedHour)
                        .param("employeeId", String.valueOf(employeeId))
                        .param("patientId", String.valueOf(patientId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reservation"));

        verify(reservationService).addReservation(any(ReservationDTO.class));
    }

    @Test
    void searchReservations() throws Exception {
        //given
        ReservationDTO searchCriteria = new ReservationDTO();
        int page = 1;
        int size = 10;

        List<ReservationEntity> existingReservations = Arrays.asList(
                ReservationFixtures.testReservation1(), ReservationFixtures.testReservation2()
        );
        Page<ReservationEntity> reservationPage = new PageImpl<>(
                existingReservations, PageRequest.of(page, size), 2
        );

        //mockowanie odpowiedzi serwisu
        when(reservationService.findReservation(any(ReservationDTO.class), eq(PageRequest.of(page, size))))
                .thenReturn(reservationPage);

        //when & then
        mockMvc.perform(get("/reservation/search")
                        .flashAttr("reservationDTO", searchCriteria)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"))
                .andExpect(model().attribute("reservations", existingReservations))
                .andExpect(model().attribute("currentPage", page))
                .andExpect(model().attribute("totalPages", reservationPage.getTotalPages()));

        verify(reservationService).findReservation(any(ReservationDTO.class), eq(PageRequest.of(page, size)));
    }
}