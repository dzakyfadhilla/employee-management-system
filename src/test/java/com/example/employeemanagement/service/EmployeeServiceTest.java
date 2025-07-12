package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;
import com.example.employeemanagement.entity.Branch;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.DataConflictException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.BranchRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EmployeeService
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Branch testBranch;
    private Employee testEmployee;
    private EmployeeRequestDto testEmployeeRequestDto;

    @BeforeEach
    void setUp() {
        // Setup test data
        testBranch = new Branch();
        testBranch.setId(1L);
        testBranch.setCode("HO");
        testBranch.setName("Head Office");
        testBranch.setAddress("Jakarta");
        testBranch.setCreatedAt(LocalDateTime.now());
        testBranch.setUpdatedAt(LocalDateTime.now());

        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setEmployeeCode("EMP001");
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setEmail("john.doe@company.com");
        testEmployee.setPhoneNumber("081234567890");
        testEmployee.setHireDate(LocalDate.of(2023, 1, 15));
        testEmployee.setPosition("Manager");
        testEmployee.setAddress("Jakarta");
        testEmployee.setBranch(testBranch);
        testEmployee.setCreatedAt(LocalDateTime.now());
        testEmployee.setUpdatedAt(LocalDateTime.now());

        testEmployeeRequestDto = new EmployeeRequestDto();
        testEmployeeRequestDto.setEmployeeCode("EMP001");
        testEmployeeRequestDto.setFirstName("John");
        testEmployeeRequestDto.setLastName("Doe");
        testEmployeeRequestDto.setEmail("john.doe@company.com");
        testEmployeeRequestDto.setPhoneNumber("081234567890");
        testEmployeeRequestDto.setHireDate(LocalDate.of(2023, 1, 15));
        testEmployeeRequestDto.setPosition("Manager");
        testEmployeeRequestDto.setAddress("Jakarta");
        testEmployeeRequestDto.setBranchId(1L);
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees() {
        // Arrange
        List<Employee> employees = Arrays.asList(testEmployee);
        when(employeeRepository.findAllWithBranch()).thenReturn(employees);

        // Act
        List<EmployeeResponseDto> result = employeeService.getAllEmployees();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("EMP001", result.get(0).getEmployeeCode());
        assertEquals("John", result.get(0).getFirstName());
        verify(employeeRepository, times(1)).findAllWithBranch();
    }

    @Test
    void getEmployeeById_WhenEmployeeExists_ShouldReturnEmployee() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        // Act
        EmployeeResponseDto result = employeeService.getEmployeeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("EMP001", result.getEmployeeCode());
        assertEquals("John", result.getFirstName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getEmployeeById_WhenEmployeeNotExists_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById(1L);
        });
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getEmployeeByCode_WhenEmployeeExists_ShouldReturnEmployee() {
        // Arrange
        when(employeeRepository.findByEmployeeCode("EMP001")).thenReturn(Optional.of(testEmployee));

        // Act
        EmployeeResponseDto result = employeeService.getEmployeeByCode("EMP001");

        // Assert
        assertNotNull(result);
        assertEquals("EMP001", result.getEmployeeCode());
        assertEquals("John", result.getFirstName());
        verify(employeeRepository, times(1)).findByEmployeeCode("EMP001");
    }

    @Test
    void getEmployeeByCode_WhenEmployeeNotExists_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(employeeRepository.findByEmployeeCode("EMP001")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeByCode("EMP001");
        });
        verify(employeeRepository, times(1)).findByEmployeeCode("EMP001");
    }

    @Test
    void createEmployee_WithValidData_ShouldCreateEmployee() {
        // Arrange
        when(employeeRepository.existsByEmployeeCode("EMP001")).thenReturn(false);
        when(employeeRepository.existsByEmail("john.doe@company.com")).thenReturn(false);
        when(branchRepository.findById(1L)).thenReturn(Optional.of(testBranch));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        // Act
        EmployeeResponseDto result = employeeService.createEmployee(testEmployeeRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("EMP001", result.getEmployeeCode());
        assertEquals("John", result.getFirstName());
        verify(employeeRepository, times(1)).existsByEmployeeCode("EMP001");
        verify(employeeRepository, times(1)).existsByEmail("john.doe@company.com");
        verify(branchRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployee_WithDuplicateEmployeeCode_ShouldThrowDataConflictException() {
        // Arrange
        when(employeeRepository.existsByEmployeeCode("EMP001")).thenReturn(true);

        // Act & Assert
        assertThrows(DataConflictException.class, () -> {
            employeeService.createEmployee(testEmployeeRequestDto);
        });
        verify(employeeRepository, times(1)).existsByEmployeeCode("EMP001");
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void createEmployee_WithDuplicateEmail_ShouldThrowDataConflictException() {
        // Arrange
        when(employeeRepository.existsByEmployeeCode("EMP001")).thenReturn(false);
        when(employeeRepository.existsByEmail("john.doe@company.com")).thenReturn(true);

        // Act & Assert
        assertThrows(DataConflictException.class, () -> {
            employeeService.createEmployee(testEmployeeRequestDto);
        });
        verify(employeeRepository, times(1)).existsByEmployeeCode("EMP001");
        verify(employeeRepository, times(1)).existsByEmail("john.doe@company.com");
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void createEmployee_WithNonExistentBranch_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(employeeRepository.existsByEmployeeCode("EMP001")).thenReturn(false);
        when(employeeRepository.existsByEmail("john.doe@company.com")).thenReturn(false);
        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.createEmployee(testEmployeeRequestDto);
        });
        verify(branchRepository, times(1)).findById(1L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void createEmployee_WithInvalidPhoneNumber_ShouldThrowDataConflictException() {
        // Arrange
        testEmployeeRequestDto.setPhoneNumber("081234567"); // Invalid: too short

        // Act & Assert
        assertThrows(DataConflictException.class, () -> {
            employeeService.createEmployee(testEmployeeRequestDto);
        });
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void createEmployee_WithPhoneNumberNotStartingWith08_ShouldThrowDataConflictException() {
        // Arrange
        testEmployeeRequestDto.setPhoneNumber("071234567890"); // Invalid: doesn't start with 08

        // Act & Assert
        assertThrows(DataConflictException.class, () -> {
            employeeService.createEmployee(testEmployeeRequestDto);
        });
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void updateEmployee_WithValidData_ShouldUpdateEmployee() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(branchRepository.findById(1L)).thenReturn(Optional.of(testBranch));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        // Act
        EmployeeResponseDto result = employeeService.updateEmployee(1L, testEmployeeRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("EMP001", result.getEmployeeCode());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void updateEmployee_WithNonExistentEmployee_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateEmployee(1L, testEmployeeRequestDto);
        });
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_WithExistingEmployee_ShouldDeleteEmployee() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        // Act
        employeeService.deleteEmployee(1L);

        // Assert
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).delete(testEmployee);
    }

    @Test
    void deleteEmployee_WithNonExistentEmployee_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.deleteEmployee(1L);
        });
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, never()).delete(any(Employee.class));
    }

    @Test
    void searchEmployeesByName_ShouldReturnMatchingEmployees() {
        // Arrange
        List<Employee> employees = Arrays.asList(testEmployee);
        when(employeeRepository.findByFirstNameContainingIgnoreCase("John")).thenReturn(employees);

        // Act
        List<EmployeeResponseDto> result = employeeService.searchEmployeesByName("John");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(employeeRepository, times(1)).findByFirstNameContainingIgnoreCase("John");
    }

    @Test
    void searchEmployeesByPosition_ShouldReturnMatchingEmployees() {
        // Arrange
        List<Employee> employees = Arrays.asList(testEmployee);
        when(employeeRepository.findByPositionContainingIgnoreCase("Manager")).thenReturn(employees);

        // Act
        List<EmployeeResponseDto> result = employeeService.searchEmployeesByPosition("Manager");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Manager", result.get(0).getPosition());
        verify(employeeRepository, times(1)).findByPositionContainingIgnoreCase("Manager");
    }

    @Test
    void getEmployeesByBranchId_WhenBranchExists_ShouldReturnEmployees() {
        // Arrange
        List<Employee> employees = Arrays.asList(testEmployee);
        when(branchRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.findByBranchId(1L)).thenReturn(employees);

        // Act
        List<EmployeeResponseDto> result = employeeService.getEmployeesByBranchId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getBranchId());
        verify(branchRepository, times(1)).existsById(1L);
        verify(employeeRepository, times(1)).findByBranchId(1L);
    }

    @Test
    void getEmployeesByBranchId_WhenBranchNotExists_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(branchRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeesByBranchId(1L);
        });
        verify(branchRepository, times(1)).existsById(1L);
        verify(employeeRepository, never()).findByBranchId(1L);
    }
}
