package com.security.security.service;

import com.security.security.entity.Employee;
import com.security.security.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DefaultEmployeeService implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Employee createEmployee(Employee employeePayload) {
        Employee employee = new Employee();
        employee.setLogin(employeePayload.getLogin());
        employee.setPassword(employeePayload.getPassword());
        employee.setFullName(employeePayload.getFullName());
        return this.employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAllEmployees() {
        return this.employeeRepository.findAll();
    }

    @Override
    public Employee findEmployeeByLogin(String login) {
        return this.employeeRepository.findUserByLogin(login).orElseThrow(() ->
                new EntityNotFoundException("User not found: " + login));
    }

    @Override
    public void validatePAssword(Employee employee, String rawPassword) {
        if(!passwordEncoder.matches(rawPassword, employee.getPassword())){
            throw new RuntimeException("Invalid password for: "+ employee.getLogin());
        }
    }

    @Override
    public void deleteEmployee(Long id) {
        this.employeeRepository.deleteById(id);

    }
}
