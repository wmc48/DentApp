package com.booking_dent.dentApp.unitTests.controller;

import com.booking_dent.dentApp.controller.EmployeeController;
import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import com.booking_dent.dentApp.model.dto.MonthDetails;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import com.booking_dent.dentApp.model.mapper.EmployeeMapper;
import com.booking_dent.dentApp.service.EmployeeService;
import com.booking_dent.dentApp.service.ScheduleService;
import com.booking_dent.dentApp.unitTests.utility.EmployeeFixtures;
import com.booking_dent.dentApp.unitTests.utility.MonthFixtures;
import com.booking_dent.dentApp.unitTests.utility.ShiftFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.nullValue;
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

    @MockBean
    private EmployeeMapper employeeMapper; // Mockujemy mappera

//    @Test
//    void showEmployeesList() throws Exception {
//        // given
//        List<EmployeeEntity> employees = Arrays.asList(EmployeeFixtures.testEmployee1(), EmployeeFixtures.testEmployee2());
//        List<EmployeeDTO> employeeDTOs = employees.stream()
//                .map(employeeMapper::toDTO)  // Zamiana encji na DTO
//                .collect(Collectors.toList());
//        when(employeeService.getAllEmployees()).thenReturn(employeeDTOs);
//
//        // when & then
//        mockMvc.perform(get("/employee"))
//                .andExpect(status().isOk()) //czy zwracany status to 200 OK
//                .andExpect(view().name("employee")) //czy zwracany widok to "employee"
//                .andExpect(model().attributeExists("employees")) //czy atrybut employees jest obecny w modelu
//                .andExpect(model().attribute("employees", employeeDTOs)); //czy atrybut employees w modelu zawiera oczekiwaną listę pracowników
//    }

//    @Test
//    void addEmployee() throws Exception {
//        //given
//        EmployeeDTO employeeDTO = EmployeeFixtures.testEmployeeDto();
//
//        // when & then
//        //tworzymy przykładowy obiekt EmployeeDTO i przekazujemy go do kontrolera za pomocą flashAttr()
//        //symuluje przesłania formularza POST
//        mockMvc.perform(post("/employee/add").flashAttr("employeeDTO", employeeDTO))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/employee"));
//        //czy metoda została wywołana z odpowiednimi argumentami
//        verify(employeeService, times(1)).addEmployee(employeeDTO);
//    }

    @Test
    void updateEmployee() throws Exception {
        //given
        EmployeeDTO employeeDTO = EmployeeFixtures.testEmployeeDto();
        Long employeeId = 1L;

        //when & then
        mockMvc.perform(put("/employee/update/" + employeeId)
                        .flashAttr("employee", employeeDTO))  // przekazujemy atrybut `employee`
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/show/" + employeeId));

        //czy metoda została wywołana z odpowiednimi argumentami
        verify(employeeService).updateEmployee(employeeDTO, employeeId);
    }

    @Test
    void saveSchedule_noConflict() throws Exception {
        //given
        Long employeeId = 1L;
        Integer shiftId = 1;
        String workDateFrom = "2024-02-01";
        String workDateTo = "2024-02-02";

        when(scheduleService.addSchedule(employeeId, workDateFrom, workDateTo, shiftId)).thenReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(post("/employee/scheduleadd")
                        .param("employeeId", String.valueOf(employeeId))
                        .param("workDateFrom", workDateFrom)
                        .param("workDateTo", workDateTo)
                        .param("shiftId", String.valueOf(shiftId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/show/" + employeeId))
                .andExpect(flash().attribute("errorMessage", nullValue())); // Sprawdzamy, że errorMessage nie istnieje    }
    }

    @Test
    void testSaveSchedule_withConflict() throws Exception {
        //given
        Long employeeId = 1L;
        String workDateFrom = "2024-11-10";
        String workDateTo = "2024-11-15";
        Integer shiftId = 1;

        List<LocalDate> conflictingDates = List.of(LocalDate.of(2024, 11, 11));
        when(scheduleService.addSchedule(employeeId, workDateFrom, workDateTo, shiftId)).thenReturn(conflictingDates);
        when(scheduleService.generateScheduleConflictMessage(conflictingDates)).thenReturn("Konflikt terminów");

        //when & then
        mockMvc.perform(post("/employee/scheduleadd")
                        .param("employeeId", String.valueOf(employeeId))
                        .param("workDateFrom", workDateFrom)
                        .param("workDateTo", workDateTo)
                        .param("shiftId", String.valueOf(shiftId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/show/" + employeeId))
                .andExpect(flash().attribute("errorMessage", "Konflikt terminów"));
    }

    @Test
    void deleteStaff() throws Exception {
        //given
        Long employeeId = 1L;

        //when & then
        mockMvc.perform(delete("/employee/delete/{employeeId}", employeeId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employee"));

        verify(employeeService, times(1)).deleteById(employeeId);
    }
}


//    @Test
//    public void showEmployeeDetails_ShouldHandleNullMonthParam() throws Exception {
//        //given
//        EmployeeEntity employee = EmployeeFixtures.testEmployee1();
//        List<ShiftEntity> shifts = Arrays.asList(ShiftFixtures.testShift1(), ShiftFixtures.testShift2());
//
//        MonthDetails monthDetails = MonthFixtures.testMonthDetails();
//        List<ScheduleDTO> schedules = new ArrayList<>();
//
//        //when
//        when(employeeService.findEmployeeById(employee.getEmployeeId())).thenReturn(employee);
//        when(employeeService.getMonthDetails(null)).thenReturn(monthDetails);
//        when(scheduleService.getEmployeeSchedulesForMonth(eq(employee.getEmployeeId()), any(LocalDate.class)))
//                .thenReturn(schedules);
//        when(employeeService.getAllShifts()).thenReturn(shifts);
//
//        //then
//        mockMvc.perform(get("/employee/show/{employeeId}", employee.getEmployeeId())
//                        .accept(MediaType.TEXT_HTML))
//                .andExpect(status().isOk())
//                .andExpect(view().name("employeeDetails"))
//                .andExpect(model().attribute("employee", employee))
//                .andExpect(model().attribute("schedules", schedules))
//                .andExpect(model().attribute("shifts", shifts))
//                .andExpect(model().attribute("previousMonth", "2024-01"))
//                .andExpect(model().attribute("nextMonth", "2024-03"))
//                .andExpect(model().attribute("currentMonthName", "Luty 2024"));
//    }

//    @Test
//    public void showEmployeeDetails_ShouldHandleSpecificMonthParam() throws Exception {
//        //given
//        EmployeeEntity employee = EmployeeFixtures.testEmployee1();
//        List<ShiftEntity> shifts = Arrays.asList(ShiftFixtures.testShift1(), ShiftFixtures.testShift2());
//
//        MonthDetails monthDetails = MonthFixtures.testMonthDetails();
//
//        List<ScheduleDTO> schedules = new ArrayList<>();
//
//        //when
//        when(employeeService.findEmployeeById(employee.getEmployeeId())).thenReturn(employee);
//        when(employeeService.getMonthDetails("2024-02")).thenReturn(monthDetails);
//        when(scheduleService.getEmployeeSchedulesForMonth(eq(employee.getEmployeeId()), eq(LocalDate.of(2024, 2, 1))))
//                .thenReturn(schedules);
//        when(employeeService.getAllShifts()).thenReturn(shifts);
//
//        //then
//        mockMvc.perform(get("/employee/show/{employeeId}", employee.getEmployeeId())
//                        .param("month", "2024-02")
//                        .accept(MediaType.TEXT_HTML))
//                .andExpect(status().isOk())
//                .andExpect(view().name("employeeDetails"))
//                .andExpect(model().attribute("employee", employee))
//                .andExpect(model().attribute("schedules", schedules))
//                .andExpect(model().attribute("shifts", shifts))
//                .andExpect(model().attribute("previousMonth", "2024-01"))
//                .andExpect(model().attribute("nextMonth", "2024-03"))
//                .andExpect(model().attribute("currentMonthName", "Luty 2024"));
//    }
//}
