package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.BranchEventDto;
import com.example.employeemanagement.dto.EmployeeEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Kafka Producer Service for Employee Management System
 * Handles publishing events to Kafka topics
 */
@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private static final String EMPLOYEE_EVENTS_TOPIC = "employee-events";
    private static final String BRANCH_EVENTS_TOPIC = "branch-events";
    private static final String NOTIFICATION_EVENTS_TOPIC = "notification-events";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publish employee event to Kafka topic
     * @param eventDto Employee event data
     */
    public void publishEmployeeEvent(EmployeeEventDto eventDto) {
        try {
            // Generate unique event ID
            eventDto.setEventId(UUID.randomUUID().toString());
            
            logger.info("Publishing employee event: {} for employee ID: {}", 
                       eventDto.getEventType(), eventDto.getEmployeeId());

            kafkaTemplate.send(EMPLOYEE_EVENTS_TOPIC, eventDto.getEventId(), eventDto);
            
            logger.info("Employee event published successfully: {}", eventDto.getEventId());

        } catch (Exception e) {
            logger.error("Error publishing employee event: {}", eventDto, e);
        }
    }

    /**
     * Publish branch event to Kafka topic
     * @param eventDto Branch event data
     */
    public void publishBranchEvent(BranchEventDto eventDto) {
        try {
            // Generate unique event ID
            eventDto.setEventId(UUID.randomUUID().toString());
            
            logger.info("Publishing branch event: {} for branch ID: {}", 
                       eventDto.getEventType(), eventDto.getBranchId());

            kafkaTemplate.send(BRANCH_EVENTS_TOPIC, eventDto.getEventId(), eventDto);
            
            logger.info("Branch event published successfully: {}", eventDto.getEventId());

        } catch (Exception e) {
            logger.error("Error publishing branch event: {}", eventDto, e);
        }
    }

    /**
     * Publish generic notification message
     * @param message Notification message
     * @param userId User ID for the notification
     */
    public void publishNotification(String message, String userId) {
        try {
            String eventId = UUID.randomUUID().toString();
            
            logger.info("Publishing notification for user: {}", userId);

            // Create simple notification object
            NotificationEvent notification = new NotificationEvent(eventId, message, userId);

            kafkaTemplate.send(NOTIFICATION_EVENTS_TOPIC, eventId, notification);
            
            logger.info("Notification published successfully: {}", eventId);

        } catch (Exception e) {
            logger.error("Error publishing notification: {}", message, e);
        }
    }

    /**
     * Inner class for notification events
     */
    public static class NotificationEvent {
        private String eventId;
        private String message;
        private String userId;
        private long timestamp;

        public NotificationEvent(String eventId, String message, String userId) {
            this.eventId = eventId;
            this.message = message;
            this.userId = userId;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters and setters
        public String getEventId() { return eventId; }
        public void setEventId(String eventId) { this.eventId = eventId; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

        @Override
        public String toString() {
            return "NotificationEvent{" +
                    "eventId='" + eventId + '\'' +
                    ", message='" + message + '\'' +
                    ", userId='" + userId + '\'' +
                    ", timestamp=" + timestamp +
                    '}';
        }
    }
}
