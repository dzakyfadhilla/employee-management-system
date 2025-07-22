package com.example.employeemanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * DTO for Employee requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDto {
    
    @NotBlank(message = "Employee code is required")
    @Size(min = 3, max = 20, message = "Employee code must be between 3 and 20 characters")
    private String employeeCode;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @Email(message = "Please provide a valid email address")
    private String email;
    
    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phoneNumber;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    
    @Size(max = 50, message = "Position cannot exceed 50 characters")
    private String position;
    
    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;
    
    @NotNull(message = "Branch ID is required")
    private Long branchId;
}
