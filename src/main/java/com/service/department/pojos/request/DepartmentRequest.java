package com.service.department.pojos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentRequest {

    @NotBlank(message = "Department code is required")
    private String departmentCode;

    @NotBlank(message = "Department name is required")
    private String departmentName;

    @NotBlank(message = "Description is required")
    private String description;
}