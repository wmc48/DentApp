package com.booking_dent.dentApp.unitTests.controller;

import com.booking_dent.dentApp.controller.EmployeeController;
import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.model.dto.MonthDetails;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import com.booking_dent.dentApp.service.EmployeeService;
import com.booking_dent.dentApp.service.ScheduleService;
import com.booking_dent.dentApp.unitTests.EntityFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc; //symulowanie żądań HTTP do kontrolera w testach

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ScheduleService scheduleService;

    @Test
    void showEmployeesList() throws Exception {
        // given
        List<EmployeeEntity> employees = Arrays.asList(EntityFixtures.testEmployee1(), EntityFixtures.testEmployee2());
        when(employeeService.getAllEmployees()).thenReturn(employees);

        // when & then
        mockMvc.perform(get("/employee"))
                .andExpect(status().isOk()) //czy zwracany status to 200 OK
                .andExpect(view().name("employee")) //czy zwracany widok to "employee"
                .andExpect(model().attributeExists("employees")) //czy atrybut employees jest obecny w modelu
                .andExpect(model().attribute("employees", employees)); //czy atrybut employees w modelu zawiera oczekiwaną listę pracowników
    }

    @Test
    void addEmployee() throws Exception {
        //given
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .name("Test1")
                .surname("TestoweNazw1")
                .email("email@email.com")
                .phone("8888888888")
                .build();

        //when
        //tworzymy przykładowy obiekt EmployeeDTO i przekazujemy go do kontrolera za pomocą flashAttr()
        //symuluje przesłania formularza POST
        mockMvc.perform(post("/employee/add").flashAttr("employeeDTO", employeeDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employee"));

        //then
        verify(employeeService, times(1)).addEmployee(employeeDTO);
    }

    @Test
    void deleteStaff() throws Exception {
        //given
        Long employeeId = 1L;

        //when
        mockMvc.perform(delete("/employee/delete/{employeeId}", employeeId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employee"));

        //then
        verify(employeeService, times(1)).deleteById(employeeId);
    }


    @Test
    public void showEmployeeDetails_ShouldHandleNullMonthParam() throws Exception {
        //given
        EmployeeEntity employee = EntityFixtures.testEmployee1();
        List<ShiftEntity> shifts = Arrays.asList(EntityFixtures.testShift1(), EntityFixtures.testShift2());

        MonthDetails monthDetails = MonthDetails.builder()
                .currentMonth(LocalDate.of(2024, 1, 1))
                .previousMonth(LocalDate.of(2023, 12, 1))
                .nextMonth(LocalDate.of(2024, 2, 1))
                .currentMonthName("Styczeń 2024")
                .build();

        List<ScheduleDTO> schedules = new ArrayList<>();

        //when
        when(employeeService.findEmployeeById(employee.getEmployeeId())).thenReturn(employee);
        when(employeeService.getMonthDetails(null)).thenReturn(monthDetails);
        when(scheduleService.getEmployeeSchedulesForMonth(eq(employee.getEmployeeId()), any(LocalDate.class)))
                .thenReturn(schedules);
        when(employeeService.getAllShifts()).thenReturn(shifts);

        //then
        mockMvc.perform(get("/employee/show/{employeeId}", employee.getEmployeeId())
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("employeeDetails"))
                .andExpect(model().attribute("employee", employee))
                .andExpect(model().attribute("schedules", schedules))
                .andExpect(model().attribute("shifts", shifts))
                .andExpect(model().attribute("previousMonth", "2023-12"))
                .andExpect(model().attribute("nextMonth", "2024-02"))
                .andExpect(model().attribute("currentMonthName", "Styczeń 2024"));
    }

    @Test
    public void showEmployeeDetails_ShouldHandleSpecificMonthParam() throws Exception {
        //given
        EmployeeEntity employee = EntityFixtures.testEmployee1();
        List<ShiftEntity> shifts = Arrays.asList(EntityFixtures.testShift1(), EntityFixtures.testShift2());

        MonthDetails monthDetails = MonthDetails.builder()
                .currentMonth(LocalDate.of(2024, 2, 1))
                .previousMonth(LocalDate.of(2024, 1, 1))
                .nextMonth(LocalDate.of(2024, 3, 1))
                .currentMonthName("Luty 2024")
                .build();

        List<ScheduleDTO> schedules = new ArrayList<>();

        //when
        when(employeeService.findEmployeeById(employee.getEmployeeId())).thenReturn(employee);
        when(employeeService.getMonthDetails("2024-02")).thenReturn(monthDetails);
        when(scheduleService.getEmployeeSchedulesForMonth(eq(employee.getEmployeeId()), eq(LocalDate.of(2024, 2, 1))))
                .thenReturn(schedules);
        when(employeeService.getAllShifts()).thenReturn(shifts);

        //then
        mockMvc.perform(get("/employee/show/{employeeId}", employee.getEmployeeId())
                        .param("month", "2024-02")
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("employeeDetails"))
                .andExpect(model().attribute("employee", employee))
                .andExpect(model().attribute("schedules", schedules))
                .andExpect(model().attribute("shifts", shifts))
                .andExpect(model().attribute("previousMonth", "2024-01"))
                .andExpect(model().attribute("nextMonth", "2024-03"))
                .andExpect(model().attribute("currentMonthName", "Luty 2024"));
    }
}
