package com.security.security.config;

import com.security.security.entity.Employee;
import com.security.security.entity.Role;
import com.security.security.repository.EmployeeRepository;
import com.security.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class DataInitializerExample implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roleRepository.save(adminRole);
        roleRepository.save(userRole);


        Employee employee = new Employee();
        employee.setLogin("admin");
        employee.setPassword(passwordEncoder.encode("admin"));
        employee.setFullName("Admin User");
        employee.getRoles().add(adminRole);
        employee.getRoles().add(userRole);
        employeeRepository.save(employee);

    }
}
