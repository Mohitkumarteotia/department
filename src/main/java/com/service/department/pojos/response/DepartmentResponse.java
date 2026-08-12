package com.service.department.pojos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentResponse {
    private Long id;
    private String departmentCode;
    private String departmentName;
    private String description;
}