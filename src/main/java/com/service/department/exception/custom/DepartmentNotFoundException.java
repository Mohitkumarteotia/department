package com.service.department.exception.custom;

public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException(String message){
        super(message);
    }
}
