package com.security.security.service;

import com.security.security.entity.Employee;
import com.security.security.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DefaultEmployeeService implements EmployeeService {

    private final EmployeeRepository employeeRepository;



    @Override
    public Employee createEmployee(Employee employeePayload) {
        // Лучше билдер,а не new как делал ранее
         Employee employee = Employee.builder()
            .login(employeePayload.getLogin())
            .password(employeePayload.getPassword()) // можно добавить шифрование
            .fullName(employeePayload.getFullName())
            .build();
        
        log.info("Creating new employee with login: {}", employee.getLogin());
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
    public Employee findEmployeeById(Long id) {
        return this.employeeRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("User with id: %s not found: ".formatted(id)));
    }


    @Override
    public void deleteEmployee(Long id) {
        this.employeeRepository.deleteById(id);

    }
}
