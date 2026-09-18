package com.service.department.service;

import com.service.department.entity.Department;
import com.service.department.exception.custom.DepartmentNotFoundException;
import com.service.department.pojos.request.DepartmentRequest;
import com.service.department.pojos.response.DepartmentResponse;
import com.service.department.repository.DepartmentRepository;
import com.service.department.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    void createDepartment_ShouldReturnDepartmentResponse() {

        DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentCode("IT001");
        request.setDepartmentName("IT");
        request.setDescription("Information Technology");

        Department department = Department.builder()
                .id(1L)
                .departmentCode("IT001")
                .departmentName("IT")
                .description("Information Technology")
                .build();

        when(departmentRepository.save(any(Department.class)))
                .thenReturn(department);

        DepartmentResponse response =
                departmentService.createDepartment(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("IT001", response.getDepartmentCode());
        assertEquals("IT", response.getDepartmentName());

        verify(departmentRepository, times(1))
                .save(any(Department.class));
    }

    @Test
    void getDepartmentById_ShouldReturnDepartment() {

        Department department = Department.builder()
                .id(1L)
                .departmentCode("IT001")
                .departmentName("IT")
                .description("Information Technology")
                .build();

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        DepartmentResponse response =
                departmentService.getDepartmentById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("IT", response.getDepartmentName());

        verify(departmentRepository).findById(1L);
    }

    @Test
    void getDepartmentById_ShouldThrowException_WhenNotFound() {

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        DepartmentNotFoundException exception =
                assertThrows(
                        DepartmentNotFoundException.class,
                        () -> departmentService.getDepartmentById(1L)
                );

        assertEquals("Department not found", exception.getMessage());

        verify(departmentRepository).findById(1L);
    }

    @Test
    void getAllDepartments_ShouldReturnList() {

        Department dept1 = Department.builder()
                .id(1L)
                .departmentCode("IT001")
                .departmentName("IT")
                .description("Technology")
                .build();

        Department dept2 = Department.builder()
                .id(2L)
                .departmentCode("HR001")
                .departmentName("HR")
                .description("Human Resource")
                .build();

        when(departmentRepository.findAll())
                .thenReturn(List.of(dept1, dept2));

        List<DepartmentResponse> responses =
                departmentService.getAllDepartments();

        assertEquals(2, responses.size());
        assertEquals("IT", responses.get(0).getDepartmentName());
        assertEquals("HR", responses.get(1).getDepartmentName());

        verify(departmentRepository).findAll();
    }

    @Test
    void updateDepartment_ShouldReturnUpdatedDepartment() {

        DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentCode("FIN001");
        request.setDepartmentName("Finance");
        request.setDescription("Finance Department");

        Department existingDepartment = Department.builder()
                .id(1L)
                .departmentCode("IT001")
                .departmentName("IT")
                .description("Technology")
                .build();

        Department updatedDepartment = Department.builder()
                .id(1L)
                .departmentCode("FIN001")
                .departmentName("Finance")
                .description("Finance Department")
                .build();

        when(departmentRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(existingDepartment));

        when(departmentRepository.save(any(Department.class)))
                .thenReturn(updatedDepartment);

        DepartmentResponse response =
                departmentService.updateDepartment(1L, request);

        assertNotNull(response);
        assertEquals("Finance", response.getDepartmentName());
        assertEquals("FIN001", response.getDepartmentCode());

        verify(departmentRepository).findByIdForUpdate(1L);
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void updateDepartment_ShouldThrowException_WhenNotFound() {

        DepartmentRequest request = new DepartmentRequest();

        when(departmentRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                DepartmentNotFoundException.class,
                () -> departmentService.updateDepartment(1L, request)
        );

        verify(departmentRepository).findByIdForUpdate(1L);
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void deleteDepartment_ShouldDeleteSuccessfully() {

        Department department = Department.builder()
                .id(1L)
                .departmentCode("IT001")
                .departmentName("IT")
                .build();

        when(departmentRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(department));

        doNothing().when(departmentRepository)
                .delete(department);

        departmentService.deleteDepartment(1L);

        verify(departmentRepository).findByIdForUpdate(1L);
        verify(departmentRepository).delete(department);
    }

    @Test
    void deleteDepartment_ShouldThrowException_WhenNotFound() {

        when(departmentRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                DepartmentNotFoundException.class,
                () -> departmentService.deleteDepartment(1L)
        );

        verify(departmentRepository).findByIdForUpdate(1L);
        verify(departmentRepository, never()).delete(any());
    }
}