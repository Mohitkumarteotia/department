package com.service.department.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.department.pojos.request.DepartmentRequest;
import com.service.department.pojos.response.DepartmentResponse;
import com.service.department.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    private static final String BASE_URL = "/api/v1/departments";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DepartmentService departmentService;

    @Test
    void createDepartment_ShouldReturnCreated() throws Exception {

        DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentCode("FIN001");
        request.setDepartmentName("Finance");
        request.setDescription("Finance Department");

        DepartmentResponse response = new DepartmentResponse();
        response.setId(1L);
        response.setDepartmentCode("FIN001");
        response.setDepartmentName("Finance");
        response.setDescription("Finance Department");

        when(departmentService.createDepartment(any(DepartmentRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.departmentCode").value("FIN001"))
                .andExpect(jsonPath("$.departmentName").value("Finance"))
                .andExpect(jsonPath("$.description")
                        .value("Finance Department"));

        verify(departmentService)
                .createDepartment(any(DepartmentRequest.class));
    }

    @Test
    void createDepartment_WithInvalidRequest_ShouldReturnBadRequest()
            throws Exception {

        DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentName("Finance");

        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("One or more fields are invalid"))
                .andExpect(jsonPath("$.fieldErrors.departmentCode")
                        .value("Department code is required"))
                .andExpect(jsonPath("$.fieldErrors.description")
                        .value("Description is required"));

        verifyNoInteractions(departmentService);
    }

    @Test
    void getDepartmentById_ShouldReturnDepartment() throws Exception {

        DepartmentResponse response = new DepartmentResponse();
        response.setId(1L);
        response.setDepartmentCode("IT001");
        response.setDepartmentName("IT");
        response.setDescription("Information Technology");

        when(departmentService.getDepartmentById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get(BASE_URL + "/{id}", 1L)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.departmentCode").value("IT001"))
                .andExpect(jsonPath("$.departmentName").value("IT"))
                .andExpect(jsonPath("$.description")
                        .value("Information Technology"));

        verify(departmentService).getDepartmentById(1L);
    }

    @Test
    void getAllDepartments_ShouldReturnList() throws Exception {

        DepartmentResponse departmentOne = new DepartmentResponse();
        departmentOne.setId(1L);
        departmentOne.setDepartmentCode("IT001");
        departmentOne.setDepartmentName("IT");
        departmentOne.setDescription("Information Technology");

        DepartmentResponse departmentTwo = new DepartmentResponse();
        departmentTwo.setId(2L);
        departmentTwo.setDepartmentCode("HR001");
        departmentTwo.setDepartmentName("HR");
        departmentTwo.setDescription("Human Resources");

        when(departmentService.getAllDepartments())
                .thenReturn(List.of(departmentOne, departmentTwo));

        mockMvc.perform(
                        get(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].departmentCode").value("IT001"))
                .andExpect(jsonPath("$[0].departmentName").value("IT"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].departmentCode").value("HR001"))
                .andExpect(jsonPath("$[1].departmentName").value("HR"));

        verify(departmentService).getAllDepartments();
    }

    @Test
    void updateDepartment_ShouldReturnUpdatedDepartment() throws Exception {

        Long departmentId = 1L;

        DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentCode("FIN001");
        request.setDepartmentName("Finance");
        request.setDescription("Updated Finance Department");

        DepartmentResponse response = new DepartmentResponse();
        response.setId(departmentId);
        response.setDepartmentCode("FIN001");
        response.setDepartmentName("Finance");
        response.setDescription("Updated Finance Department");

        when(departmentService.updateDepartment(
                eq(departmentId),
                any(DepartmentRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put(BASE_URL + "/{id}", departmentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.departmentCode").value("FIN001"))
                .andExpect(jsonPath("$.departmentName").value("Finance"))
                .andExpect(jsonPath("$.description")
                        .value("Updated Finance Department"));

        verify(departmentService).updateDepartment(
                eq(departmentId),
                any(DepartmentRequest.class)
        );
    }

    @Test
    void updateDepartment_WithInvalidRequest_ShouldReturnBadRequest()
            throws Exception {

        Long departmentId = 1L;

        DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentName("Finance");

        mockMvc.perform(
                        put(BASE_URL + "/{id}", departmentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("One or more fields are invalid"))
                .andExpect(jsonPath("$.fieldErrors.departmentCode")
                        .value("Department code is required"))
                .andExpect(jsonPath("$.fieldErrors.description")
                        .value("Description is required"));

        verifyNoInteractions(departmentService);
    }

    @Test
    void deleteDepartment_ShouldReturnNoContent() throws Exception {

        doNothing()
                .when(departmentService)
                .deleteDepartment(1L);

        mockMvc.perform(delete(BASE_URL + "/{id}", 1L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(departmentService).deleteDepartment(1L);
    }
}