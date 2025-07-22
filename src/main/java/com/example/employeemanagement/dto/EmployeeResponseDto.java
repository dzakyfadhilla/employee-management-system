package com.example.employeemanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for Employee responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {
    
    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    
    private String position;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Branch information
    private Long branchId;
    private String branchCode;
    private String branchName;

    /**
     * Get full name of the employee
     * @return first name + last name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
