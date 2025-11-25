package com.security.security.restController;


import com.security.security.entity.Employee;
import com.security.security.entity.EntityPayload.AuthResponse;
import com.security.security.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("v1/api/users")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentEmployee(
            @AuthenticationPrincipal UserDetails userDetails){
        Employee employee = this.employeeService.findEmployeeByLogin(userDetails.getUsername());

        AuthResponse authResponse = new AuthResponse(
                employee.getId(),
                employee.getLogin(),
                employee.getFullName(),
                employee.getRoles()
        );
        return  ResponseEntity.ok(authResponse);
    }

}
