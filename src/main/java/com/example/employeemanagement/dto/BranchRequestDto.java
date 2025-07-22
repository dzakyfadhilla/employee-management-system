package com.example.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO for Branch requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchRequestDto {
    
    @NotBlank(message = "Branch code is required")
    @Size(min = 2, max = 10, message = "Branch code must be between 2 and 10 characters")
    private String code;
    
    @NotBlank(message = "Branch name is required")
    @Size(min = 2, max = 100, message = "Branch name must be between 2 and 100 characters")
    private String name;
    
    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;
    
    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phoneNumber;
}
