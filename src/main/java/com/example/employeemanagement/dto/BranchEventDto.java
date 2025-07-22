package com.example.employeemanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Branch Event DTO for Kafka messaging
 * Represents events related to branch operations (CREATE, UPDATE, DELETE)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchEventDto {
    
    private String eventId;
    private String eventType; // CREATE, UPDATE, DELETE
    private Long branchId;
    private String branchName;
    private String address;
    private String phoneNumber;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    private String userId; // User who performed the action

    // Constructor with essential fields
    public BranchEventDto(String eventType, Long branchId, String branchName, String address, String phoneNumber) {
        this.eventType = eventType;
        this.branchId = branchId;
        this.branchName = branchName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.timestamp = LocalDateTime.now();
    }
}
