package com.booking_dent.dentApp.unitTests.controller;


import com.booking_dent.dentApp.controller.PatientListController;
import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.model.dto.PatientDTO;
import com.booking_dent.dentApp.service.EmployeeService;
import com.booking_dent.dentApp.service.PatientService;
import com.booking_dent.dentApp.unitTests.utility.PatientFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientListController.class)
class PatientListControllerTest {

    @Autowired
    private MockMvc mockMvc; //symulowanie żądań HTTP do kontrolera w testach

    @MockBean
    private PatientService patientService;
    @MockBean
    private EmployeeService employeeService;

    @Test
    void showPatientList() throws Exception {
        //given
        List<PatientEntity> patients = Arrays.asList(PatientFixtures.createTestPatient1(), PatientFixtures.createTestPatient2());
        when(patientService.getAllPatient()).thenReturn(patients);

        //when & then
        mockMvc.perform(get("/patient"))
                .andExpect(status().isOk()) //czy zwracany status to 200 OK
                .andExpect(view().name("patient")) //czy zwracany widok to "patient"
                .andExpect(model().attributeExists("patients")) //czy atrybut patients jest obecny w modelu
                .andExpect(model().attribute("patients", patients)); //czy atrybut patients w modelu zawiera oczekiwaną listę pacjentów
    }

    @Test
    void deletePatient() throws Exception {
        //given
        Long patientId = 1L;

        //when & then
        mockMvc.perform(delete("/patient/delete/{patientId}", patientId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient"));

        verify(patientService, times(1)).deleteById(patientId);
    }

//    @Test
//    void updatePatient() throws Exception {
//        // given
//        PatientDTO patientDTO = PatientFixtures.testPatientDto();
//        Long patientId = 1L;
//
//        // when & then
//        mockMvc.perform(put("/patient/update/" + patientId)
//                        .flashAttr("patient", patientDTO))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/patient/show/" + patientId));
//
//        verify(patientService).updatePatient(patientDTO, patientId);
//    }

    @Test
    void searchPatients() throws Exception {
        //given
        PatientDTO searchCriteria = PatientDTO.builder()
                .name("Jan")
                .build();

        List<PatientEntity> existingPatients = Arrays.asList(PatientFixtures.createTestPatient1(), PatientFixtures.createTestPatient2());
        when(patientService.searchPatients(searchCriteria)).thenReturn(existingPatients);

        //whe&& then
        mockMvc.perform(get("/patient/search")
                        .flashAttr("patientDTO", searchCriteria))
                .andExpect(status().isOk())
                .andExpect(view().name("patient"))
                .andExpect(model().attribute("patients", existingPatients))
                .andExpect(model().attribute("name", existingPatients.get(0).getName()));

        verify(patientService).searchPatients(searchCriteria);
    }

    @Test
    void searchPatients_noMatch_returnsEmptyList() throws Exception {
        //given
        PatientDTO searchCriteria = PatientDTO.builder()
                .name("Jana")
                .build();

        List<PatientEntity> emptyPatientList = Collections.emptyList();
        when(patientService.searchPatients(searchCriteria)).thenReturn(emptyPatientList);

        // when then
        mockMvc.perform(get("/patient/search")
                        .flashAttr("patientDTO", searchCriteria))
                .andExpect(status().isOk())
                .andExpect(view().name("patient"))
                .andExpect(model().attribute("patients", emptyPatientList)); // oczekujemy pustej listy

        verify(patientService).searchPatients(searchCriteria);
    }
}