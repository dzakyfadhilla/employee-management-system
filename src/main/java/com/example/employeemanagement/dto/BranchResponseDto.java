package com.example.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Branch responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponseDto {
    
    private Long id;
    private String code;
    private String name;
    private String address;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int employeeCount;
}
