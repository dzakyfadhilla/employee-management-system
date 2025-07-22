# Kafka Configuration Guide for Employee Management System

## Overview

Konfigurasi ini menambahkan Apache Kafka untuk event-driven messaging dalam Employee Management System. Kafka memungkinkan aplikasi untuk mengirim dan menerima events secara asynchronous ketika terjadi operasi CRUD pada Employee dan Branch.

## Prerequisites

### 1. Install Apache Kafka Locally

#### Option 1: Using Homebrew (macOS)
```bash
# Install Kafka
brew install kafka

# Start Zookeeper
brew services start zookeeper

# Start Kafka
brew services start kafka
```

#### Option 2: Manual Installation
```bash
# Download Kafka
wget https://downloads.apache.org/kafka/2.8.2/kafka_2.13-2.8.2.tgz
tar -xzf kafka_2.13-2.8.2.tgz
cd kafka_2.13-2.8.2

# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka Server
bin/kafka-server-start.sh config/server.properties
```

### 2. Create Kafka Topics (Optional)

Topics akan dibuat otomatis oleh aplikasi, tapi Anda juga bisa membuatnya manual:

```bash
# Create employee-events topic
kafka-topics.sh --create --topic employee-events --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

# Create branch-events topic
kafka-topics.sh --create --topic branch-events --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

# Create notification-events topic
kafka-topics.sh --create --topic notification-events --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

# List topics
kafka-topics.sh --list --bootstrap-server localhost:9092
```

## Kafka Configuration

### 1. Dependencies Added

Dependency berikut telah ditambahkan ke `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

### 2. YAML Configuration

Konfigurasi Kafka telah ditambahkan ke `application-dev.yml`:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      acks: all
      retries: 3
      batch-size: 16384
      linger-ms: 5
      buffer-memory: 33554432
      properties:
        "[linger.ms]": 5
    consumer:
      group-id: employee-management-group
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      auto-offset-reset: earliest
      enable-auto-commit: true
      auto-commit-interval: 1000
      properties:
        "[spring.json.trusted.packages]": "com.example.employeemanagement.dto"
```

### 3. Configuration Parameters Explained

- **bootstrap-servers**: Alamat Kafka broker (localhost:9092 untuk development)
- **producer.acks**: "all" memastikan semua replica menerima message
- **producer.retries**: Jumlah retry jika pengiriman gagal
- **consumer.group-id**: Group ID untuk consumer
- **auto-offset-reset**: "earliest" membaca dari awal jika tidak ada offset
- **trusted.packages**: Package yang dipercaya untuk deserialization

## Event Types and Topics

### 1. Employee Events
- **Topic**: `employee-events`
- **Event Types**: CREATE, UPDATE, DELETE
- **DTO**: `EmployeeEventDto`

### 2. Branch Events
- **Topic**: `branch-events`
- **Event Types**: CREATE, UPDATE, DELETE
- **DTO**: `BranchEventDto`

### 3. Notification Events
- **Topic**: `notification-events`
- **Event Types**: General notifications
- **DTO**: `NotificationEvent`

## Component Architecture

### 1. KafkaConfig.java
```java
@Configuration
@EnableKafka
public class KafkaConfig {
    // Producer dan Consumer configuration
    // Topic definitions
}
```

### 2. KafkaProducerService.java
```java
@Service
public class KafkaProducerService {
    // publishEmployeeEvent()
    // publishBranchEvent()
    // publishNotification()
}
```

### 3. KafkaConsumerService.java
```java
@Service
public class KafkaConsumerService {
    // @KafkaListener untuk setiap topic
    // Event processing logic
}
```

### 4. Event DTOs
- `EmployeeEventDto`: Data untuk employee events
- `BranchEventDto`: Data untuk branch events

## Event Flow

### Employee Operations
1. **CREATE Employee**:
   - EmployeeService.createEmployee() → KafkaProducerService.publishEmployeeEvent()
   - Event dikirim ke topic `employee-events`
   - KafkaConsumerService.consumeEmployeeEvent() memproses event

2. **UPDATE Employee**:
   - EmployeeService.updateEmployee() → Kafka event dengan type "UPDATE"

3. **DELETE Employee**:
   - EmployeeService.deleteEmployee() → Kafka event dengan type "DELETE"

### Branch Operations
Sama seperti Employee operations tapi menggunakan `branch-events` topic.

## Testing Kafka

### 1. Monitor Kafka Topics
```bash
# Consumer console untuk memonitor employee-events
kafka-console-consumer.sh --topic employee-events --from-beginning --bootstrap-server localhost:9092

# Consumer console untuk memonitor branch-events
kafka-console-consumer.sh --topic branch-events --from-beginning --bootstrap-server localhost:9092
```

### 2. Test API Endpoints

Setelah aplikasi berjalan, test CRUD operations:

```bash
# Create Employee (akan trigger Kafka event)
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "081234567890",
    "branchId": 1,
    "position": "Developer"
  }'

# Update Employee
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP001",
    "firstName": "John",
    "lastName": "Smith",
    "email": "john.smith@example.com",
    "phoneNumber": "081234567890",
    "branchId": 1,
    "position": "Senior Developer"
  }'

# Delete Employee
curl -X DELETE http://localhost:8080/api/employees/1
```

### 3. Check Logs

Monitor application logs untuk melihat Kafka events:

```bash
# Run aplikasi dan monitor logs
mvn spring-boot:run

# Logs akan menampilkan:
# - "Publishing employee event: CREATE for employee ID: 1"
# - "Successfully published employee event [event-id] with offset=[0]"
# - "Received employee event: CREATE for employee ID: 1"
```

## Error Handling

### 1. Kafka Connection Issues
Jika Kafka tidak berjalan, aplikasi akan tetap berfungsi normal tapi events tidak akan dikirim. Logs akan menampilkan warning:
```
Failed to publish employee event [event-id] - Connection refused
```

### 2. Serialization Issues
Jika ada masalah dengan JSON serialization, periksa:
- Package name di `trusted.packages` configuration
- DTO class structure

### 3. Consumer Issues
Jika consumer tidak bisa membaca events:
- Periksa group-id configuration
- Periksa deserializer configuration

## Production Configuration

Untuk production environment, tambahkan konfigurasi di `application-prod.yml`:

```yaml
spring:
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
    producer:
      acks: all
      retries: 10
      batch-size: 32768
      linger-ms: 10
      buffer-memory: 67108864
      compression-type: gzip
    consumer:
      group-id: ${KAFKA_CONSUMER_GROUP:employee-management-prod}
      auto-offset-reset: latest
      enable-auto-commit: false
      max-poll-records: 100
```

## Monitoring

### 1. Kafka Manager/UI
Install Kafka UI untuk monitoring:
```bash
# Using Docker
docker run -p 8080:8080 -e KAFKA_CLUSTERS_0_NAME=local -e KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=localhost:9092 provectuslabs/kafka-ui:latest
```

### 2. Application Metrics
Monitor aplikasi metrics:
- Event publishing success/failure rate
- Consumer lag
- Processing time

## Troubleshooting

### Common Issues

1. **Port 9092 already in use**
   ```bash
   # Check what's using port 9092
   lsof -i :9092
   
   # Kill process if needed
   kill -9 <PID>
   ```

2. **Topics not created automatically**
   ```bash
   # Create topics manually
   kafka-topics.sh --create --topic employee-events --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
   ```

3. **Consumer not receiving messages**
   - Check consumer group ID
   - Verify topic exists
   - Check offset position

### Useful Commands

```bash
# List consumer groups
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list

# Describe consumer group
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group employee-management-group

# Reset consumer offset
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --reset-offsets --to-earliest --execute --group employee-management-group --topic employee-events
```

## Next Steps

1. **Event Sourcing**: Implement event sourcing untuk audit trail
2. **Dead Letter Queue**: Tambahkan DLQ untuk failed events
3. **Event Replay**: Implement mechanism untuk replay events
4. **Schema Registry**: Gunakan Confluent Schema Registry untuk schema management
5. **Metrics**: Tambahkan Micrometer metrics untuk monitoring Kafka performance
