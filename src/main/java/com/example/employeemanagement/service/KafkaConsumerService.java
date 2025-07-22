package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.BranchEventDto;
import com.example.employeemanagement.dto.EmployeeEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka Consumer Service for Employee Management System
 * Handles consuming events from Kafka topics
 */
@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    /**
     * Consume employee events from Kafka topic
     * @param employeeEvent Employee event data
     */
    @KafkaListener(topics = "employee-events", groupId = "employee-management-group")
    public void consumeEmployeeEvent(EmployeeEventDto employeeEvent) {
        try {
            logger.info("Received employee event: {} for employee ID: {} at {}",
                       employeeEvent.getEventType(),
                       employeeEvent.getEmployeeId(),
                       employeeEvent.getTimestamp());

            // Process the employee event based on event type
            switch (employeeEvent.getEventType()) {
                case "CREATE":
                    handleEmployeeCreated(employeeEvent);
                    break;
                case "UPDATE":
                    handleEmployeeUpdated(employeeEvent);
                    break;
                case "DELETE":
                    handleEmployeeDeleted(employeeEvent);
                    break;
                default:
                    logger.warn("Unknown employee event type: {}", employeeEvent.getEventType());
            }

        } catch (Exception e) {
            logger.error("Error processing employee event: {}", employeeEvent, e);
        }
    }

    /**
     * Consume branch events from Kafka topic
     * @param branchEvent Branch event data
     */
    @KafkaListener(topics = "branch-events", groupId = "employee-management-group")
    public void consumeBranchEvent(BranchEventDto branchEvent) {
        try {
            logger.info("Received branch event: {} for branch ID: {} at {}",
                       branchEvent.getEventType(),
                       branchEvent.getBranchId(),
                       branchEvent.getTimestamp());

            // Process the branch event based on event type
            switch (branchEvent.getEventType()) {
                case "CREATE":
                    handleBranchCreated(branchEvent);
                    break;
                case "UPDATE":
                    handleBranchUpdated(branchEvent);
                    break;
                case "DELETE":
                    handleBranchDeleted(branchEvent);
                    break;
                default:
                    logger.warn("Unknown branch event type: {}", branchEvent.getEventType());
            }

        } catch (Exception e) {
            logger.error("Error processing branch event: {}", branchEvent, e);
        }
    }

    /**
     * Consume notification events from Kafka topic
     * @param notification Notification event data
     */
    @KafkaListener(topics = "notification-events", groupId = "employee-management-group")
    public void consumeNotificationEvent(Object notification) {
        try {
            logger.info("Received notification: {}", notification);

            // Process notification (could be sent to external systems, email, etc.)
            handleNotification(notification);

        } catch (Exception e) {
            logger.error("Error processing notification event: {}", notification, e);
        }
    }

    // Private methods for handling specific event types

    private void handleEmployeeCreated(EmployeeEventDto event) {
        logger.info("Processing employee creation event for: {} (ID: {})",
                   event.getEmployeeName(), event.getEmployeeId());
        
        // Add business logic here:
        // - Send welcome email
        // - Create user account
        // - Update analytics
        // - Notify managers
        
        logger.debug("Employee created - Name: {}, Email: {}, Branch: {}",
                    event.getEmployeeName(), event.getEmail(), event.getBranchName());
    }

    private void handleEmployeeUpdated(EmployeeEventDto event) {
        logger.info("Processing employee update event for: {} (ID: {})",
                   event.getEmployeeName(), event.getEmployeeId());
        
        // Add business logic here:
        // - Update external systems
        // - Sync with HR systems
        // - Log audit trail
        
        logger.debug("Employee updated - Name: {}, Email: {}, Branch: {}",
                    event.getEmployeeName(), event.getEmail(), event.getBranchName());
    }

    private void handleEmployeeDeleted(EmployeeEventDto event) {
        logger.info("Processing employee deletion event for: {} (ID: {})",
                   event.getEmployeeName(), event.getEmployeeId());
        
        // Add business logic here:
        // - Deactivate accounts
        // - Archive data
        // - Update reports
        // - Notify stakeholders
        
        logger.debug("Employee deleted - Name: {}, Email: {}, Branch: {}",
                    event.getEmployeeName(), event.getEmail(), event.getBranchName());
    }

    private void handleBranchCreated(BranchEventDto event) {
        logger.info("Processing branch creation event for: {} (ID: {})",
                   event.getBranchName(), event.getBranchId());
        
        // Add business logic here:
        // - Setup branch infrastructure
        // - Create default configurations
        // - Notify regional managers
        
        logger.debug("Branch created - Name: {}, Address: {}, Phone: {}",
                    event.getBranchName(), event.getAddress(), event.getPhoneNumber());
    }

    private void handleBranchUpdated(BranchEventDto event) {
        logger.info("Processing branch update event for: {} (ID: {})",
                   event.getBranchName(), event.getBranchId());
        
        // Add business logic here:
        // - Update external directories
        // - Sync location data
        // - Update mapping services
        
        logger.debug("Branch updated - Name: {}, Address: {}, Phone: {}",
                    event.getBranchName(), event.getAddress(), event.getPhoneNumber());
    }

    private void handleBranchDeleted(BranchEventDto event) {
        logger.info("Processing branch deletion event for: {} (ID: {})",
                   event.getBranchName(), event.getBranchId());
        
        // Add business logic here:
        // - Archive branch data
        // - Reassign employees
        // - Update reporting structures
        
        logger.debug("Branch deleted - Name: {}, Address: {}, Phone: {}",
                    event.getBranchName(), event.getAddress(), event.getPhoneNumber());
    }

    private void handleNotification(Object notification) {
        logger.info("Processing notification: {}", notification);
        
        // Add business logic here:
        // - Send email notifications
        // - Push to mobile apps
        // - Update dashboards
        // - Trigger alerts
    }
}
