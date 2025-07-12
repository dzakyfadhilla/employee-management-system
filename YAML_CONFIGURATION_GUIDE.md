# Spring Boot Configuration Files - YAML Format

## 📋 Configuration Files Overview

This project now includes comprehensive YAML configuration files for different environments and purposes.

### 🗂️ **Configuration Files Structure**

```
src/
├── main/resources/
│   ├── application.yml              # Main configuration (PostgreSQL)
│   ├── application-dev.yml          # Development environment (H2)
│   ├── application-prod.yml         # Production environment (PostgreSQL)
│   └── application.properties       # Legacy properties format
└── test/resources/
    └── application-test.yml         # Test environment (H2)
```

---

## 🔧 **Configuration Files Details**

### 1. **application.yml** (Main Configuration)
- **Database**: PostgreSQL
- **Purpose**: Default configuration for local development with PostgreSQL
- **Port**: 8080
- **Logging**: INFO level with SQL debugging

### 2. **application-dev.yml** (Development Profile)
- **Database**: H2 In-Memory
- **Purpose**: Quick development without PostgreSQL setup
- **Port**: 8080
- **H2 Console**: Enabled at `/h2-console`
- **Logging**: DEBUG level for detailed debugging

### 3. **application-prod.yml** (Production Profile)
- **Database**: PostgreSQL with environment variables
- **Purpose**: Production deployment
- **Port**: Configurable via environment variable
- **Features**: 
  - Connection pooling optimization
  - Response compression
  - File logging
  - Production-level security
- **Logging**: WARN level for performance

### 4. **application-test.yml** (Test Profile)
- **Database**: H2 In-Memory (create-drop)
- **Purpose**: Unit and integration testing
- **Port**: 8081
- **Logging**: DEBUG level for test debugging

---

## 🚀 **How to Use Profiles**

### **Running with Different Profiles**

#### **1. Development Profile (H2 Database)**
```bash
# Using Maven
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev

# Using Java JAR
java -jar target/employee-management-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# Using Environment Variable
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

#### **2. Production Profile (PostgreSQL)**
```bash
# Using Maven with environment variables
export SPRING_PROFILES_ACTIVE=prod
export DB_URL=jdbc:postgresql://localhost:5432/employeedb
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
export SERVER_PORT=8080
mvn spring-boot:run

# Using Java JAR
java -jar target/employee-management-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --DB_URL=jdbc:postgresql://localhost:5432/employeedb \
  --DB_USERNAME=postgres \
  --DB_PASSWORD=your_password
```

#### **3. Test Profile (Automatic)**
```bash
# Tests automatically use test profile
mvn test
```

---

## 🔧 **Configuration Features**

### **Database Configuration**
```yaml
# PostgreSQL (Production)
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/employeedb
    driver-class-name: org.postgresql.Driver
    username: postgres
    password: postgres

# H2 (Development/Testing)
spring:
  datasource:
    url: jdbc:h2:mem:devdb
    driver-class-name: org.h2.Driver
    username: sa
    password: ""
```

### **Connection Pool Configuration**
```yaml
spring:
  datasource:
    hikari:
      connection-timeout: 20000
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 300000
      max-lifetime: 1200000
```

### **JPA/Hibernate Configuration**
```yaml
spring:
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update    # create-drop for dev/test, validate for prod
    show-sql: true        # false for production
```

### **Logging Configuration**
```yaml
logging:
  level:
    "[com.example.employeemanagement]": INFO
    "[org.hibernate.SQL]": DEBUG
    root: WARN
```

---

## 🌐 **Environment Variables**

### **Production Environment Variables**
```bash
# Database Configuration
DB_URL=jdbc:postgresql://your-host:5432/employeedb
DB_USERNAME=your_username
DB_PASSWORD=your_password

# Server Configuration
SERVER_PORT=8080

# Spring Profile
SPRING_PROFILES_ACTIVE=prod
```

### **Docker Environment Variables**
```yaml
# docker-compose.yml example
environment:
  - SPRING_PROFILES_ACTIVE=prod
  - DB_URL=jdbc:postgresql://postgres:5432/employeedb
  - DB_USERNAME=postgres
  - DB_PASSWORD=postgres
  - SERVER_PORT=8080
```

---

## 🔍 **Profile-Specific Features**

### **Development Profile Features**
- ✅ H2 Console enabled at `/h2-console`
- ✅ Detailed SQL logging
- ✅ Debug level logging
- ✅ Auto DDL creation

### **Production Profile Features**
- ✅ Environment variable configuration
- ✅ Optimized connection pooling
- ✅ Response compression
- ✅ File logging with rotation
- ✅ Minimal logging for performance
- ✅ Database validation mode

### **Test Profile Features**
- ✅ In-memory H2 database
- ✅ Create-drop DDL mode
- ✅ Debug logging for troubleshooting
- ✅ Separate port (8081)

---

## 📝 **Migration from Properties to YAML**

If you want to use YAML instead of properties format:

1. **Keep the original**: `application.properties` (for backward compatibility)
2. **Use YAML files**: Spring Boot automatically prioritizes YAML over properties
3. **Profile-specific**: Use `application-{profile}.yml` for environment-specific configs

### **Precedence Order** (Highest to Lowest)
1. Command line arguments
2. Environment variables
3. `application-{profile}.yml`
4. `application.yml`
5. `application-{profile}.properties`
6. `application.properties`

---

## 🚨 **Important Notes**

### **YAML Syntax Considerations**
- Use quotes for keys with special characters: `"[format_sql]": true`
- Indentation matters (use spaces, not tabs)
- Boolean values: `true`/`false` (not `True`/`False`)
- Environment variables: `${VARIABLE_NAME:default_value}`

### **Database Setup Requirements**
- **PostgreSQL**: Must be running and database created for prod profile
- **H2**: No setup required for dev/test profiles
- **Test Database**: Automatically created and destroyed for each test

### **Security Considerations**
- Never commit actual passwords in YAML files
- Use environment variables for sensitive data in production
- Consider using Spring Cloud Config for centralized configuration

---

**Created**: July 12, 2025  
**Updated**: July 12, 2025  
**Version**: 1.0
