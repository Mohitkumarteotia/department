package com.service.department.service;

import com.service.department.entity.Department;
import com.service.department.exception.custom.DepartmentNotFoundException;
import com.service.department.pojos.request.DepartmentRequest;
import com.service.department.pojos.response.DepartmentResponse;
import com.service.department.repository.DepartmentRepository;
import com.service.department.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    private static final Long DEPARTMENT_ID = 1L;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    void createDepartment_ShouldReturnDepartmentResponse() {

        DepartmentRequest request = createDepartmentRequest(
                "IT001",
                "IT",
                "Information Technology"
        );

        Department savedDepartment = createDepartment(
                DEPARTMENT_ID,
                "IT001",
                "IT",
                "Information Technology"
        );

        when(departmentRepository.save(any(Department.class)))
                .thenReturn(savedDepartment);

        DepartmentResponse response =
                departmentService.createDepartment(request);

        assertNotNull(response);

        assertAll(
                () -> assertEquals(
                        DEPARTMENT_ID,
                        response.getId()
                ),
                () -> assertEquals(
                        "IT001",
                        response.getDepartmentCode()
                ),
                () -> assertEquals(
                        "IT",
                        response.getDepartmentName()
                ),
                () -> assertEquals(
                        "Information Technology",
                        response.getDescription()
                )
        );

        ArgumentCaptor<Department> departmentCaptor =
                ArgumentCaptor.forClass(Department.class);

        verify(departmentRepository)
                .save(departmentCaptor.capture());

        Department capturedDepartment =
                departmentCaptor.getValue();

        assertAll(
                () -> assertEquals(
                        "IT001",
                        capturedDepartment.getDepartmentCode()
                ),
                () -> assertEquals(
                        "IT",
                        capturedDepartment.getDepartmentName()
                ),
                () -> assertEquals(
                        "Information Technology",
                        capturedDepartment.getDescription()
                )
        );

        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void getDepartmentById_ShouldReturnDepartment() {

        Department department = createDepartment(
                DEPARTMENT_ID,
                "IT001",
                "IT",
                "Information Technology"
        );

        when(departmentRepository.findById(DEPARTMENT_ID))
                .thenReturn(Optional.of(department));

        DepartmentResponse response =
                departmentService.getDepartmentById(DEPARTMENT_ID);

        assertNotNull(response);

        assertAll(
                () -> assertEquals(
                        DEPARTMENT_ID,
                        response.getId()
                ),
                () -> assertEquals(
                        "IT001",
                        response.getDepartmentCode()
                ),
                () -> assertEquals(
                        "IT",
                        response.getDepartmentName()
                ),
                () -> assertEquals(
                        "Information Technology",
                        response.getDescription()
                )
        );

        verify(departmentRepository)
                .findById(DEPARTMENT_ID);

        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void getDepartmentById_ShouldThrowException_WhenNotFound() {

        when(departmentRepository.findById(DEPARTMENT_ID))
                .thenReturn(Optional.empty());

        DepartmentNotFoundException exception =
                assertThrows(
                        DepartmentNotFoundException.class,
                        () -> departmentService.getDepartmentById(
                                DEPARTMENT_ID
                        )
                );

        assertEquals(
                "Department not found",
                exception.getMessage()
        );

        verify(departmentRepository)
                .findById(DEPARTMENT_ID);

        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void getAllDepartments_ShouldReturnDepartmentList() {

        Department firstDepartment = createDepartment(
                1L,
                "IT001",
                "IT",
                "Information Technology"
        );

        Department secondDepartment = createDepartment(
                2L,
                "HR001",
                "HR",
                "Human Resources"
        );

        when(departmentRepository.findAll())
                .thenReturn(
                        List.of(
                                firstDepartment,
                                secondDepartment
                        )
                );

        List<DepartmentResponse> responses =
                departmentService.getAllDepartments();

        assertNotNull(responses);
        assertEquals(2, responses.size());

        DepartmentResponse firstResponse = responses.get(0);
        DepartmentResponse secondResponse = responses.get(1);

        assertAll(
                () -> assertEquals(
                        1L,
                        firstResponse.getId()
                ),
                () -> assertEquals(
                        "IT001",
                        firstResponse.getDepartmentCode()
                ),
                () -> assertEquals(
                        "IT",
                        firstResponse.getDepartmentName()
                ),
                () -> assertEquals(
                        "Information Technology",
                        firstResponse.getDescription()
                ),
                () -> assertEquals(
                        2L,
                        secondResponse.getId()
                ),
                () -> assertEquals(
                        "HR001",
                        secondResponse.getDepartmentCode()
                ),
                () -> assertEquals(
                        "HR",
                        secondResponse.getDepartmentName()
                ),
                () -> assertEquals(
                        "Human Resources",
                        secondResponse.getDescription()
                )
        );

        verify(departmentRepository).findAll();

        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void getAllDepartments_ShouldReturnEmptyList_WhenNoDepartmentsExist() {

        when(departmentRepository.findAll())
                .thenReturn(List.of());

        List<DepartmentResponse> responses =
                departmentService.getAllDepartments();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(departmentRepository).findAll();

        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void updateDepartment_ShouldReturnUpdatedDepartment() {

        DepartmentRequest request = createDepartmentRequest(
                "FIN001",
                "Finance",
                "Finance Department"
        );

        Department existingDepartment = createDepartment(
                DEPARTMENT_ID,
                "IT001",
                "IT",
                "Information Technology"
        );

        when(departmentRepository.findByIdForUpdate(DEPARTMENT_ID))
                .thenReturn(Optional.of(existingDepartment));

        when(departmentRepository.save(any(Department.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(
                                0,
                                Department.class
                        )
                );

        DepartmentResponse response =
                departmentService.updateDepartment(
                        DEPARTMENT_ID,
                        request
                );

        assertNotNull(response);

        assertAll(
                () -> assertEquals(
                        DEPARTMENT_ID,
                        response.getId()
                ),
                () -> assertEquals(
                        "FIN001",
                        response.getDepartmentCode()
                ),
                () -> assertEquals(
                        "Finance",
                        response.getDepartmentName()
                ),
                () -> assertEquals(
                        "Finance Department",
                        response.getDescription()
                )
        );

        ArgumentCaptor<Department> departmentCaptor =
                ArgumentCaptor.forClass(Department.class);

        verify(departmentRepository)
                .findByIdForUpdate(DEPARTMENT_ID);

        verify(departmentRepository)
                .save(departmentCaptor.capture());

        Department capturedDepartment =
                departmentCaptor.getValue();

        assertAll(
                () -> assertEquals(
                        DEPARTMENT_ID,
                        capturedDepartment.getId()
                ),
                () -> assertEquals(
                        "FIN001",
                        capturedDepartment.getDepartmentCode()
                ),
                () -> assertEquals(
                        "Finance",
                        capturedDepartment.getDepartmentName()
                ),
                () -> assertEquals(
                        "Finance Department",
                        capturedDepartment.getDescription()
                )
        );

        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void updateDepartment_ShouldThrowException_WhenNotFound() {

        DepartmentRequest request = createDepartmentRequest(
                "FIN001",
                "Finance",
                "Finance Department"
        );

        when(departmentRepository.findByIdForUpdate(DEPARTMENT_ID))
                .thenReturn(Optional.empty());

        DepartmentNotFoundException exception =
                assertThrows(
                        DepartmentNotFoundException.class,
                        () -> departmentService.updateDepartment(
                                DEPARTMENT_ID,
                                request
                        )
                );

        assertEquals(
                "Department not found with id: 1",
                exception.getMessage()
        );

        verify(departmentRepository)
                .findByIdForUpdate(DEPARTMENT_ID);

        verify(departmentRepository, never())
                .save(any(Department.class));

        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void deleteDepartment_ShouldDeleteSuccessfully() {

        Department department = createDepartment(
                DEPARTMENT_ID,
                "IT001",
                "IT",
                "Information Technology"
        );

        /*
         * The current service implementation calls findById()
         * while deleting the department.
         */
        when(departmentRepository.findById(DEPARTMENT_ID))
                .thenReturn(Optional.of(department));

        departmentService.deleteDepartment(DEPARTMENT_ID);

        verify(departmentRepository)
                .findById(DEPARTMENT_ID);

        verify(departmentRepository)
                .delete(department);

        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void deleteDepartment_ShouldThrowException_WhenNotFound() {

        when(departmentRepository.findById(DEPARTMENT_ID))
                .thenReturn(Optional.empty());

        DepartmentNotFoundException exception =
                assertThrows(
                        DepartmentNotFoundException.class,
                        () -> departmentService.deleteDepartment(
                                DEPARTMENT_ID
                        )
                );

        assertEquals(
                "Department not found",
                exception.getMessage()
        );

        verify(departmentRepository)
                .findById(DEPARTMENT_ID);

        verify(departmentRepository, never())
                .delete(any(Department.class));

        verifyNoMoreInteractions(departmentRepository);
    }

    private DepartmentRequest createDepartmentRequest(
            String departmentCode,
            String departmentName,
            String description
    ) {
        DepartmentRequest request = new DepartmentRequest();
        request.setDepartmentCode(departmentCode);
        request.setDepartmentName(departmentName);
        request.setDescription(description);

        return request;
    }

    private Department createDepartment(
            Long id,
            String departmentCode,
            String departmentName,
            String description
    ) {
        return Department.builder()
                .id(id)
                .departmentCode(departmentCode)
                .departmentName(departmentName)
                .description(description)
                .build();
    }
}