package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;
import com.example.employeemanagement.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for EmployeeController
 */
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeResponseDto employeeResponseDto;
    private EmployeeRequestDto employeeRequestDto;

    @BeforeEach
    void setUp() {
        employeeResponseDto = new EmployeeResponseDto(
                1L,
                "EMP001",
                "John",
                "Doe",
                "john.doe@company.com",
                "081234567890",
                LocalDate.of(2023, 1, 15),
                "Manager",
                "Jakarta",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L,
                "HO",
                "Head Office"
        );

        employeeRequestDto = new EmployeeRequestDto();
        employeeRequestDto.setEmployeeCode("EMP001");
        employeeRequestDto.setFirstName("John");
        employeeRequestDto.setLastName("Doe");
        employeeRequestDto.setEmail("john.doe@company.com");
        employeeRequestDto.setPhoneNumber("081234567890");
        employeeRequestDto.setHireDate(LocalDate.of(2023, 1, 15));
        employeeRequestDto.setPosition("Manager");
        employeeRequestDto.setAddress("Jakarta");
        employeeRequestDto.setBranchId(1L);
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees() throws Exception {
        // Arrange
        List<EmployeeResponseDto> employees = Arrays.asList(employeeResponseDto);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        // Act & Assert
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeCode").value("EMP001"))
                .andExpect(jsonPath("$[0].firstName").value("John"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void getEmployeeById_WhenEmployeeExists_ShouldReturnEmployee() throws Exception {
        // Arrange
        when(employeeService.getEmployeeById(1L)).thenReturn(employeeResponseDto);

        // Act & Assert
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeCode").value("EMP001"))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void createEmployee_WithValidData_ShouldCreateEmployee() throws Exception {
        // Arrange
        when(employeeService.createEmployee(any(EmployeeRequestDto.class))).thenReturn(employeeResponseDto);

        // Act & Assert
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeCode").value("EMP001"))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(employeeService, times(1)).createEmployee(any(EmployeeRequestDto.class));
    }

    @Test
    void updateEmployee_WithValidData_ShouldUpdateEmployee() throws Exception {
        // Arrange
        when(employeeService.updateEmployee(eq(1L), any(EmployeeRequestDto.class))).thenReturn(employeeResponseDto);

        // Act & Assert
        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeCode").value("EMP001"))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(employeeService, times(1)).updateEmployee(eq(1L), any(EmployeeRequestDto.class));
    }

    @Test
    void deleteEmployee_ShouldDeleteEmployee() throws Exception {
        // Arrange
        doNothing().when(employeeService).deleteEmployee(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    void searchEmployees_ShouldReturnMatchingEmployees() throws Exception {
        // Arrange
        List<EmployeeResponseDto> employees = Arrays.asList(employeeResponseDto);
        when(employeeService.searchEmployeesByName("John")).thenReturn(employees);

        // Act & Assert
        mockMvc.perform(get("/api/employees/search")
                        .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));

        verify(employeeService, times(1)).searchEmployeesByName("John");
    }

    @Test
    void searchEmployeesByName_ShouldReturnMatchingEmployees() throws Exception {
        // Arrange
        List<EmployeeResponseDto> employees = Arrays.asList(employeeResponseDto);
        when(employeeService.searchEmployeesByName("John")).thenReturn(employees);

        // Act & Assert
        mockMvc.perform(get("/api/employees/search/name")
                        .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));

        verify(employeeService, times(1)).searchEmployeesByName("John");
    }

    @Test
    void searchEmployeesByPosition_ShouldReturnMatchingEmployees() throws Exception {
        // Arrange
        List<EmployeeResponseDto> employees = Arrays.asList(employeeResponseDto);
        when(employeeService.searchEmployeesByPosition("Manager")).thenReturn(employees);

        // Act & Assert
        mockMvc.perform(get("/api/employees/search/position")
                        .param("position", "Manager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].position").value("Manager"));

        verify(employeeService, times(1)).searchEmployeesByPosition("Manager");
    }
}
