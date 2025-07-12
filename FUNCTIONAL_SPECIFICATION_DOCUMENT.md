# Functional Specification Document (FSD)
# Employee Management System

---

## Document Information

| **Attribute** | **Details** |
|---------------|-------------|
| **Document Title** | Employee Management System - Functional Specification Document |
| **Project Name** | Employee Management System |
| **Version** | 1.0 |
| **Date** | July 12, 2025 |
| **Author** | Development Team |
| **Repository** | employee-management-system |
| **Technology Stack** | Java 11, Spring Boot 2.7.18, PostgreSQL, Maven |

---

## Table of Contents

1. [Executive Summary](#1-executive-summary)
2. [Project Overview](#2-project-overview)
3. [System Architecture](#3-system-architecture)
4. [Functional Requirements](#4-functional-requirements)
5. [Technical Specifications](#5-technical-specifications)
6. [Database Design](#6-database-design)
7. [API Specifications](#7-api-specifications)
8. [Business Rules and Validations](#8-business-rules-and-validations)
9. [Error Handling](#9-error-handling)
10. [Testing Strategy](#10-testing-strategy)
11. [Deployment Requirements](#11-deployment-requirements)
12. [Performance Requirements](#12-performance-requirements)
13. [Security Considerations](#13-security-considerations)
14. [Future Enhancements](#14-future-enhancements)

---

## 1. Executive Summary

The Employee Management System is a comprehensive CRUD (Create, Read, Update, Delete) application designed to manage employee data and branch assignments within an organization. The system provides a robust backend API built with Spring Boot that handles employee registration, branch management, and various administrative operations.

### Key Features:
- **Employee Management**: Complete lifecycle management of employee records
- **Branch Management**: Organizational branch hierarchy management
- **Data Validation**: Comprehensive input validation including phone number format validation
- **Search Functionality**: Advanced search capabilities for employees and branches
- **RESTful API**: Well-structured REST API endpoints for all operations
- **Database Integration**: PostgreSQL for production, H2 for testing
- **Comprehensive Testing**: Unit tests, integration tests, and controller tests

---

## 2. Project Overview

### 2.1 Business Context
The Employee Management System addresses the need for organizations to efficiently manage their workforce across multiple branches. It provides a centralized platform for HR departments to maintain employee records, track branch assignments, and perform administrative tasks.

### 2.2 Scope
The system encompasses:
- Employee data management (personal information, contact details, employment information)
- Branch management (branch details, location information)
- Employee-branch relationship management
- Data validation and business rule enforcement
- RESTful API for external integrations

### 2.3 Stakeholders
- **HR Department**: Primary users for employee management
- **IT Department**: System administrators and maintainers
- **Management**: Access to organizational data and reports
- **External Systems**: Integration through REST API

---

## 3. System Architecture

### 3.1 Architectural Pattern
The system follows a **Layered Architecture** pattern:

```
┌─────────────────────────────────────┐
│          Controller Layer           │ ← REST API Endpoints
├─────────────────────────────────────┤
│           Service Layer             │ ← Business Logic
├─────────────────────────────────────┤
│          Repository Layer           │ ← Data Access
├─────────────────────────────────────┤
│           Entity Layer              │ ← Database Entities
└─────────────────────────────────────┘
```

### 3.2 Technology Stack

| **Layer** | **Technology** |
|-----------|----------------|
| **Framework** | Spring Boot 2.7.18 |
| **Language** | Java 11 |
| **Database** | PostgreSQL 17 (Production), H2 (Testing) |
| **ORM** | Spring Data JPA / Hibernate |
| **Build Tool** | Maven |
| **Testing** | JUnit 5, Mockito |
| **Validation** | Bean Validation API |
| **Logging** | SLF4J |

### 3.3 System Components

#### 3.3.1 Core Components
- **EmployeeController**: REST API endpoints for employee operations
- **BranchController**: REST API endpoints for branch operations
- **EmployeeService**: Business logic for employee management
- **BranchService**: Business logic for branch management
- **EmployeeRepository**: Data access layer for employee entities
- **BranchRepository**: Data access layer for branch entities

#### 3.3.2 Supporting Components
- **DTOs**: Data Transfer Objects for API communication
- **Entities**: JPA entities representing database tables
- **Exception Handlers**: Global exception handling
- **Validation**: Custom validation logic
- **Configuration**: Application configuration classes

---

## 4. Functional Requirements

### 4.1 Employee Management

#### 4.1.1 Employee Registration (FR-001)
**Description**: System shall allow creation of new employee records
**Priority**: High
**Inputs**: Employee details (code, name, email, phone, hire date, position, address, branch assignment)
**Outputs**: Created employee record with system-generated ID
**Business Rules**:
- Employee code must be unique across the system
- Email address must be unique (if provided)
- Phone number must follow specific format (12 digits, starting with '08')
- Branch assignment is mandatory

#### 4.1.2 Employee Information Retrieval (FR-002)
**Description**: System shall provide multiple ways to retrieve employee information
**Priority**: High
**Functionality**:
- Get all employees with pagination support
- Get employee by system ID
- Get employee by unique employee code
- Get employees by branch assignment
- Search employees by name (partial match)
- Search employees by position (partial match)

#### 4.1.3 Employee Information Update (FR-003)
**Description**: System shall allow modification of existing employee records
**Priority**: High
**Business Rules**:
- All validation rules apply to updates
- System maintains audit trail (created_at, updated_at)
- Email and employee code uniqueness validation on updates

#### 4.1.4 Employee Removal (FR-004)
**Description**: System shall allow deletion of employee records
**Priority**: Medium
**Business Rules**:
- Soft delete preferred for audit purposes
- Cascade considerations for related data

### 4.2 Branch Management

#### 4.2.1 Branch Creation (FR-005)
**Description**: System shall allow creation of new branch records
**Priority**: High
**Inputs**: Branch details (code, name, address, phone number)
**Business Rules**:
- Branch code must be unique across the system
- Branch name is mandatory
- Phone number validation applies

#### 4.2.2 Branch Information Management (FR-006)
**Description**: System shall provide complete branch information management
**Priority**: High
**Functionality**:
- Get all branches
- Get branch by system ID
- Get branch by unique branch code
- Search branches by name
- Update branch information
- Delete branch (with dependency checks)

#### 4.2.3 Branch-Employee Relationship (FR-007)
**Description**: System shall maintain relationship between branches and employees
**Priority**: High
**Business Rules**:
- Each employee must be assigned to exactly one branch
- Branch deletion requires handling of assigned employees
- Employee count per branch should be tracked

### 4.3 Search and Filtering

#### 4.3.1 Advanced Search (FR-008)
**Description**: System shall provide comprehensive search capabilities
**Priority**: Medium
**Functionality**:
- Case-insensitive search
- Partial match search
- Search across multiple fields
- Search result pagination

### 4.4 Data Validation

#### 4.4.1 Input Validation (FR-009)
**Description**: System shall validate all input data according to business rules
**Priority**: High
**Validation Rules**:
- Required field validation
- Format validation (email, phone numbers)
- Length validation
- Uniqueness validation
- Cross-field validation

---

## 5. Technical Specifications

### 5.1 Development Environment
- **Java Version**: 11
- **Spring Boot Version**: 2.7.18
- **Maven Version**: 3.6+
- **Database**: PostgreSQL 17
- **IDE**: Any Java-compatible IDE

### 5.2 Runtime Requirements
- **JVM**: Java 11 or higher
- **Memory**: Minimum 1GB heap space
- **Storage**: 500MB for application and temporary files
- **Network**: HTTP/HTTPS support for REST API

### 5.3 Dependencies
```xml
<!-- Core Spring Boot Dependencies -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Database Dependencies -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>

<!-- Testing Dependencies -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## 6. Database Design

### 6.1 Entity Relationship Diagram

```
┌─────────────────────────────────────┐
│              BRANCHES               │
├─────────────────────────────────────┤
│ id (PK)              │ BIGINT       │
│ code (UK)            │ VARCHAR(255) │
│ name                 │ VARCHAR(255) │
│ address              │ VARCHAR(500) │
│ phone_number         │ VARCHAR(255) │
│ created_at           │ TIMESTAMP    │
│ updated_at           │ TIMESTAMP    │
└─────────────────────────────────────┘
                    │
                    │ 1:N
                    │
┌─────────────────────────────────────┐
│             EMPLOYEES               │
├─────────────────────────────────────┤
│ id (PK)              │ BIGINT       │
│ employee_code (UK)   │ VARCHAR(255) │
│ first_name           │ VARCHAR(255) │
│ last_name            │ VARCHAR(255) │
│ email (UK)           │ VARCHAR(255) │
│ phone_number         │ VARCHAR(255) │
│ hire_date            │ DATE         │
│ position             │ VARCHAR(50)  │
│ address              │ VARCHAR(500) │
│ branch_id (FK)       │ BIGINT       │
│ created_at           │ TIMESTAMP    │
│ updated_at           │ TIMESTAMP    │
└─────────────────────────────────────┘
```

### 6.2 Database Schema

#### 6.2.1 Branches Table
```sql
CREATE TABLE branches (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    phone_number VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (id)
);
```

#### 6.2.2 Employees Table
```sql
CREATE TABLE employees (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    employee_code VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone_number VARCHAR(255),
    hire_date DATE,
    position VARCHAR(50),
    address VARCHAR(500),
    branch_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (branch_id) REFERENCES branches(id)
);
```

### 6.3 Indexing Strategy
- **Primary Keys**: Clustered indexes on ID fields
- **Unique Constraints**: Unique indexes on code and email fields
- **Foreign Keys**: Indexes on branch_id for join optimization
- **Search Indexes**: Composite indexes for name-based searches

---

## 7. API Specifications

### 7.1 Base URL
```
Production: https://api.employeemanagement.com
Development: http://localhost:8080
```

### 7.2 Branch API Endpoints

#### 7.2.1 Get All Branches
```http
GET /api/branches
```
**Response**: List of all branches with employee count
**Status Codes**: 200 OK

#### 7.2.2 Get Branch by ID
```http
GET /api/branches/{id}
```
**Parameters**: 
- `id` (path): Branch ID
**Response**: Branch details
**Status Codes**: 200 OK, 404 Not Found

#### 7.2.3 Get Branch by Code
```http
GET /api/branches/code/{code}
```
**Parameters**: 
- `code` (path): Branch code
**Response**: Branch details
**Status Codes**: 200 OK, 404 Not Found

#### 7.2.4 Create Branch
```http
POST /api/branches
```
**Request Body**:
```json
{
    "code": "HO",
    "name": "Head Office",
    "address": "Jl. Sudirman No. 1, Jakarta",
    "phoneNumber": "081234567890"
}
```
**Response**: Created branch details
**Status Codes**: 201 Created, 400 Bad Request, 409 Conflict

#### 7.2.5 Update Branch
```http
PUT /api/branches/{id}
```
**Parameters**: 
- `id` (path): Branch ID
**Request Body**: Branch update data
**Response**: Updated branch details
**Status Codes**: 200 OK, 404 Not Found, 400 Bad Request, 409 Conflict

#### 7.2.6 Delete Branch
```http
DELETE /api/branches/{id}
```
**Parameters**: 
- `id` (path): Branch ID
**Response**: No content
**Status Codes**: 204 No Content, 404 Not Found

#### 7.2.7 Search Branches
```http
GET /api/branches/search?name={name}
```
**Parameters**: 
- `name` (query): Search term
**Response**: List of matching branches
**Status Codes**: 200 OK

### 7.3 Employee API Endpoints

#### 7.3.1 Get All Employees
```http
GET /api/employees
```
**Response**: List of all employees with branch details
**Status Codes**: 200 OK

#### 7.3.2 Get Employee by ID
```http
GET /api/employees/{id}
```
**Parameters**: 
- `id` (path): Employee ID
**Response**: Employee details
**Status Codes**: 200 OK, 404 Not Found

#### 7.3.3 Get Employee by Code
```http
GET /api/employees/code/{code}
```
**Parameters**: 
- `code` (path): Employee code
**Response**: Employee details
**Status Codes**: 200 OK, 404 Not Found

#### 7.3.4 Get Employees by Branch
```http
GET /api/employees/branch/{branchId}
```
**Parameters**: 
- `branchId` (path): Branch ID
**Response**: List of employees in the branch
**Status Codes**: 200 OK, 404 Not Found

#### 7.3.5 Create Employee
```http
POST /api/employees
```
**Request Body**:
```json
{
    "employeeCode": "EMP001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@company.com",
    "phoneNumber": "081234567890",
    "hireDate": "2025-01-15",
    "position": "Software Developer",
    "address": "Jl. Merdeka No. 10, Jakarta",
    "branchId": 1
}
```
**Response**: Created employee details
**Status Codes**: 201 Created, 400 Bad Request, 409 Conflict

#### 7.3.6 Update Employee
```http
PUT /api/employees/{id}
```
**Parameters**: 
- `id` (path): Employee ID
**Request Body**: Employee update data
**Response**: Updated employee details
**Status Codes**: 200 OK, 404 Not Found, 400 Bad Request, 409 Conflict

#### 7.3.7 Delete Employee
```http
DELETE /api/employees/{id}
```
**Parameters**: 
- `id` (path): Employee ID
**Response**: No content
**Status Codes**: 204 No Content, 404 Not Found

#### 7.3.8 Search Employees
```http
GET /api/employees/search?name={name}
GET /api/employees/search/name?name={name}
GET /api/employees/search/position?position={position}
```
**Parameters**: 
- `name` (query): Search term for name
- `position` (query): Search term for position
**Response**: List of matching employees
**Status Codes**: 200 OK

### 7.4 Response Format

#### 7.4.1 Success Response
```json
{
    "id": 1,
    "employeeCode": "EMP001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@company.com",
    "phoneNumber": "081234567890",
    "hireDate": "2025-01-15",
    "position": "Software Developer",
    "address": "Jl. Merdeka No. 10, Jakarta",
    "createdAt": "2025-07-12T10:30:00",
    "updatedAt": "2025-07-12T10:30:00",
    "branchId": 1,
    "branchCode": "HO",
    "branchName": "Head Office",
    "fullName": "John Doe"
}
```

#### 7.4.2 Error Response
```json
{
    "status": 409,
    "error": "Data Conflict",
    "message": "Employee with code 'EMP001' already exists",
    "timestamp": "2025-07-12T10:30:00"
}
```

---

## 8. Business Rules and Validations

### 8.1 Employee Business Rules

#### 8.1.1 Employee Code Validation
- **Rule**: Employee code must be unique across the system
- **Implementation**: Database unique constraint + service layer validation
- **Error Message**: "Employee with code '{code}' already exists"

#### 8.1.2 Email Validation
- **Rule**: Email must be unique if provided
- **Implementation**: Database unique constraint + service layer validation
- **Format**: Valid email format using Bean Validation
- **Error Message**: "Employee with email '{email}' already exists"

#### 8.1.3 Phone Number Validation
- **Rule**: Phone number must be exactly 12 digits and start with '08'
- **Implementation**: Custom validation method in service layer
- **Format**: Numbers only, length validation, prefix validation
- **Error Messages**: 
  - "Phone number must be exactly 12 digits long"
  - "Phone number must start with '08'"

#### 8.1.4 Name Validation
- **Rule**: First name and last name are mandatory
- **Implementation**: Bean Validation annotations
- **Constraints**: 
  - Minimum length: 2 characters
  - Maximum length: 50 characters
  - Cannot be blank

#### 8.1.5 Branch Assignment
- **Rule**: Every employee must be assigned to a valid branch
- **Implementation**: Foreign key constraint + service layer validation
- **Error Message**: "Branch not found with id: {branchId}"

### 8.2 Branch Business Rules

#### 8.2.1 Branch Code Validation
- **Rule**: Branch code must be unique across the system
- **Implementation**: Database unique constraint + service layer validation
- **Error Message**: "Branch with code '{code}' already exists"

#### 8.2.2 Branch Name Validation
- **Rule**: Branch name is mandatory
- **Implementation**: Bean Validation annotations
- **Constraints**: Cannot be blank

#### 8.2.3 Branch Deletion
- **Rule**: Branch can only be deleted if no employees are assigned
- **Implementation**: Service layer validation before deletion
- **Error Message**: "Cannot delete branch with assigned employees"

### 8.3 Data Integrity Rules

#### 8.3.1 Referential Integrity
- **Rule**: Employee-Branch relationship must be maintained
- **Implementation**: Foreign key constraints
- **Cascade Rules**: No cascade delete to preserve data integrity

#### 8.3.2 Audit Trail
- **Rule**: System must track creation and modification timestamps
- **Implementation**: JPA lifecycle callbacks (@PrePersist, @PreUpdate)
- **Fields**: created_at, updated_at

---

## 9. Error Handling

### 9.1 Exception Hierarchy

#### 9.1.1 Custom Exceptions
- **ResourceNotFoundException**: For entity not found scenarios
- **DataConflictException**: For data validation and conflict scenarios
- **ValidationException**: For input validation errors

#### 9.1.2 Global Exception Handler
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, "Resource Not Found", ex.getMessage()));
    }
    
    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ErrorResponse> handleDataConflict(DataConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, "Data Conflict", ex.getMessage()));
    }
}
```

### 9.2 HTTP Status Codes

| **Status Code** | **Description** | **Usage** |
|-----------------|-----------------|-----------|
| 200 OK | Success | GET, PUT operations |
| 201 Created | Resource created | POST operations |
| 204 No Content | Success with no content | DELETE operations |
| 400 Bad Request | Invalid input | Validation errors |
| 404 Not Found | Resource not found | Entity not exists |
| 409 Conflict | Data conflict | Duplicate data |
| 500 Internal Server Error | Server error | Unexpected errors |

### 9.3 Error Response Format
```json
{
    "status": 404,
    "error": "Resource Not Found",
    "message": "Employee not found with id: 123",
    "timestamp": "2025-07-12T10:30:00"
}
```

---

## 10. Testing Strategy

### 10.1 Testing Pyramid

#### 10.1.1 Unit Tests (49 tests total)
- **Service Layer Tests**: 32 tests
  - EmployeeService: 19 tests
  - BranchService: 13 tests
- **Controller Layer Tests**: 14 tests
  - EmployeeController: 8 tests
  - BranchController: 6 tests
- **Integration Tests**: 2 tests
- **Application Tests**: 1 test

#### 10.1.2 Test Coverage Areas
- **CRUD Operations**: Complete test coverage for all CRUD operations
- **Validation Logic**: Phone number validation, uniqueness constraints
- **Business Rules**: Branch-employee relationships, data integrity
- **Error Scenarios**: Exception handling, error responses
- **API Endpoints**: REST API functionality testing

### 10.2 Testing Technologies
- **JUnit 5**: Test framework
- **Mockito**: Mocking framework for unit tests
- **Spring Boot Test**: Integration testing support
- **MockMvc**: Web layer testing
- **H2 Database**: In-memory database for testing
- **TestPropertySource**: Test configuration management

### 10.3 Test Data Management
- **Test Database**: H2 in-memory database
- **Test Data**: Programmatically created test data
- **Test Isolation**: Each test runs in isolation with clean state
- **Test Configuration**: Separate application properties for testing

---

## 11. Deployment Requirements

### 11.1 Environment Requirements

#### 11.1.1 Development Environment
- **Database**: H2 in-memory database
- **Configuration**: development profile
- **Logging**: DEBUG level for development

#### 11.1.2 Production Environment
- **Database**: PostgreSQL 17
- **Configuration**: production profile
- **Logging**: INFO level for production
- **Security**: HTTPS, authentication/authorization

### 11.2 Database Setup

#### 11.2.1 PostgreSQL Configuration
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/employeedb
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

#### 11.2.2 Database Migration
- **Schema Creation**: Automatic schema generation via Hibernate
- **Data Migration**: SQL scripts for initial data setup
- **Backup Strategy**: Regular database backups recommended

### 11.3 Application Configuration

#### 11.3.1 Server Configuration
```properties
server.port=8080
server.servlet.context-path=/
spring.application.name=employee-management-system
```

#### 11.3.2 JPA Configuration
```properties
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
```

---

## 12. Performance Requirements

### 12.1 Response Time Requirements
- **API Response Time**: < 200ms for simple operations
- **Database Query Time**: < 100ms for standard queries
- **Search Operations**: < 500ms for complex searches
- **Bulk Operations**: < 5 seconds for batch operations

### 12.2 Throughput Requirements
- **Concurrent Users**: Support up to 100 concurrent users
- **API Requests**: Handle 1000 requests per minute
- **Database Connections**: Connection pool size: 10 connections

### 12.3 Scalability Considerations
- **Horizontal Scaling**: Stateless application design
- **Database Scaling**: Read replicas for read-heavy operations
- **Caching Strategy**: Application-level caching for frequently accessed data
- **Load Balancing**: Support for multiple application instances

---

## 13. Security Considerations

### 13.1 Data Security
- **Data Encryption**: Sensitive data encryption at rest
- **Password Security**: Secure password hashing (if authentication added)
- **SQL Injection Prevention**: Parameterized queries via JPA
- **Input Validation**: Comprehensive input validation

### 13.2 API Security
- **Authentication**: JWT-based authentication (future enhancement)
- **Authorization**: Role-based access control (future enhancement)
- **CORS**: Cross-origin resource sharing configuration
- **Rate Limiting**: API rate limiting (future enhancement)

### 13.3 Data Privacy
- **Personal Information**: Proper handling of employee personal data
- **Audit Logging**: Comprehensive audit trail for data changes
- **Data Retention**: Data retention policies and procedures
- **GDPR Compliance**: Compliance with data protection regulations

---

## 14. Future Enhancements

### 14.1 Phase 2 Enhancements
- **Authentication & Authorization**: User management system
- **File Upload**: Employee photo and document management
- **Reporting**: Employee reports and analytics
- **Notification System**: Email notifications for HR operations

### 14.2 Phase 3 Enhancements
- **Mobile Application**: Mobile app for employee self-service
- **Advanced Search**: Full-text search capabilities
- **Integration**: Third-party system integrations
- **Workflow**: Approval workflows for HR processes

### 14.3 Technical Improvements
- **Caching**: Redis caching for improved performance
- **Monitoring**: Application monitoring and metrics
- **Documentation**: API documentation with Swagger
- **DevOps**: CI/CD pipeline implementation

---

## Appendices

### Appendix A: Sample Data
The system includes sample data for testing and development:
- **4 Sample Branches**: HO, JKT01, BDG01, SBY01
- **8 Sample Employees**: Various positions and branch assignments

### Appendix B: Configuration Files
- **application.properties**: Main application configuration
- **application-test.properties**: Test environment configuration
- **pom.xml**: Maven project configuration

### Appendix C: Database Scripts
- **schema.sql**: Database schema creation scripts
- **data.sql**: Sample data insertion scripts

---

**Document Version**: 1.0  
**Last Updated**: July 12, 2025  
**Next Review**: August 12, 2025
