package com.example.employeemanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Employee Event DTO for Kafka messaging
 * Represents events related to employee operations (CREATE, UPDATE, DELETE)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEventDto {
    
    private String eventId;
    private String eventType; // CREATE, UPDATE, DELETE
    private Long employeeId;
    private String employeeName;
    private String email;
    private String phoneNumber;
    private Long branchId;
    private String branchName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    private String userId; // User who performed the action

    // Constructor with essential fields
    public EmployeeEventDto(String eventType, Long employeeId, String employeeName, String email, String phoneNumber, Long branchId, String branchName) {
        this.eventType = eventType;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.branchId = branchId;
        this.branchName = branchName;
        this.timestamp = LocalDateTime.now();
    }
}
