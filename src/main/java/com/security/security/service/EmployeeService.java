package com.security.security.service;

import com.security.security.entity.Employee;

import java.util.List;

public interface EmployeeService {

    Employee createEmployee(Employee employeePayload);

    List<Employee> findAllEmployees();

    Employee findEmployeeByLogin(String login);

    void validatePAssword(Employee employee, String rawPassword);


    void deleteEmployee(Long id);


}
