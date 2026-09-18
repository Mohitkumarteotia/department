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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DepartmentService departmentService;

    @Test
    void createDepartment_ShouldReturnCreated() throws Exception {

        DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentName("IT");

        DepartmentResponse response = new DepartmentResponse();
        response.setId(1L);
        response.setDepartmentName("IT");

        when(departmentService.createDepartment(any(DepartmentRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.departmentName").value("IT"));
    }

    @Test
    void getDepartmentById_ShouldReturnDepartment() throws Exception {

        DepartmentResponse response = new DepartmentResponse();
        response.setId(1L);
        response.setDepartmentName("IT");

        when(departmentService.getDepartmentById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.departmentName").value("IT"));
    }

    @Test
    void getAllDepartments_ShouldReturnList() throws Exception {

        DepartmentResponse dept1 = new DepartmentResponse();
        dept1.setId(1L);
        dept1.setDepartmentName("IT");

        DepartmentResponse dept2 = new DepartmentResponse();
        dept2.setId(2L);
        dept2.setDepartmentName("HR");

        when(departmentService.getAllDepartments())
                .thenReturn(List.of(dept1, dept2));

        mockMvc.perform(get("/api/v1/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].departmentName").value("IT"))
                .andExpect(jsonPath("$[1].departmentName").value("HR"));
    }

    @Test
    void updateDepartment_ShouldReturnUpdatedDepartment() throws Exception {

        DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentName("Finance");

        DepartmentResponse response = new DepartmentResponse();
        response.setId(1L);
        response.setDepartmentName("Finance");

        when(departmentService.updateDepartment(
                eq(1L),
                any(DepartmentRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.departmentName").value("Finance"));
    }

    @Test
    void deleteDepartment_ShouldReturnNoContent() throws Exception {

        doNothing().when(departmentService).deleteDepartment(1L);

        mockMvc.perform(delete("/api/v1/departments/1"))
                .andExpect(status().isNoContent());
    }
}