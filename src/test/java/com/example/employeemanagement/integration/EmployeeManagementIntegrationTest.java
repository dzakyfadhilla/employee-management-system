package com.example.employeemanagement.integration;

import com.example.employeemanagement.dto.BranchRequestDto;
import com.example.employeemanagement.dto.BranchResponseDto;
import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Employee Management System
 */
@SpringBootTest
@AutoConfigureWebMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.h2.console.enabled=true"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmployeeManagementIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void completeEmployeeManagementWorkflow_ShouldWork() throws Exception {
        // 1. Create a branch
        BranchRequestDto branchRequest = new BranchRequestDto();
        branchRequest.setCode("HO");
        branchRequest.setName("Head Office");
        branchRequest.setAddress("Jakarta");
        branchRequest.setPhoneNumber("081234567890");

        String branchResponse = mockMvc.perform(post("/api/branches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(branchRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("HO"))
                .andExpect(jsonPath("$.name").value("Head Office"))
                .andReturn().getResponse().getContentAsString();

        BranchResponseDto createdBranch = objectMapper.readValue(branchResponse, BranchResponseDto.class);

        // 2. Create an employee
        EmployeeRequestDto employeeRequest = new EmployeeRequestDto();
        employeeRequest.setEmployeeCode("EMP001");
        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");
        employeeRequest.setEmail("john.doe@company.com");
        employeeRequest.setPhoneNumber("081234567890");
        employeeRequest.setHireDate(LocalDate.of(2023, 1, 15));
        employeeRequest.setPosition("Manager");
        employeeRequest.setAddress("Jakarta");
        employeeRequest.setBranchId(createdBranch.getId());

        String employeeResponse = mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeCode").value("EMP001"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.branchCode").value("HO"))
                .andReturn().getResponse().getContentAsString();

        EmployeeResponseDto createdEmployee = objectMapper.readValue(employeeResponse, EmployeeResponseDto.class);

        // 3. Get all employees
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeCode").value("EMP001"));

        // 4. Get employee by ID
        mockMvc.perform(get("/api/employees/" + createdEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeCode").value("EMP001"));

        // 5. Update employee
        employeeRequest.setPosition("Senior Manager");
        mockMvc.perform(put("/api/employees/" + createdEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position").value("Senior Manager"));

        // 6. Search employees by name
        mockMvc.perform(get("/api/employees/search")
                        .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));

        // 7. Search employees by position
        mockMvc.perform(get("/api/employees/search/position")
                        .param("position", "Senior"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].position").value("Senior Manager"));

        // 8. Get branch with employee count
        mockMvc.perform(get("/api/branches/" + createdBranch.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeCount").value(1));

        // 9. Try to delete branch with employees (should fail)
        mockMvc.perform(delete("/api/branches/" + createdBranch.getId()))
                .andExpect(status().isConflict());

        // 10. Delete employee first
        mockMvc.perform(delete("/api/employees/" + createdEmployee.getId()))
                .andExpect(status().isNoContent());

        // 11. Now delete branch (should succeed)
        mockMvc.perform(delete("/api/branches/" + createdBranch.getId()))
                .andExpect(status().isNoContent());

        // 12. Verify branch is deleted
        mockMvc.perform(get("/api/branches/" + createdBranch.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void phoneNumberValidation_ShouldWork() throws Exception {
        // Create a branch first
        BranchRequestDto branchRequest = new BranchRequestDto();
        branchRequest.setCode("HO");
        branchRequest.setName("Head Office");
        branchRequest.setAddress("Jakarta");
        branchRequest.setPhoneNumber("081234567890");

        String branchResponse = mockMvc.perform(post("/api/branches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(branchRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BranchResponseDto createdBranch = objectMapper.readValue(branchResponse, BranchResponseDto.class);

        // Test invalid phone number (too short)
        EmployeeRequestDto employeeRequest = new EmployeeRequestDto();
        employeeRequest.setEmployeeCode("EMP001");
        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");
        employeeRequest.setEmail("john.doe@company.com");
        employeeRequest.setPhoneNumber("081234567"); // Invalid: too short
        employeeRequest.setHireDate(LocalDate.of(2023, 1, 15));
        employeeRequest.setPosition("Manager");
        employeeRequest.setAddress("Jakarta");
        employeeRequest.setBranchId(createdBranch.getId());

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Phone number must be exactly 12 digits long"));

        // Test invalid phone number (doesn't start with 08)
        employeeRequest.setPhoneNumber("071234567890"); // Invalid: doesn't start with 08
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Phone number must start with '08'"));

        // Test valid phone number
        employeeRequest.setPhoneNumber("081234567890"); // Valid
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phoneNumber").value("081234567890"));
    }
}
