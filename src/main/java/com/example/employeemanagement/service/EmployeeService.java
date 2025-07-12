package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;
import com.example.employeemanagement.entity.Branch;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.DataConflictException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.BranchRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Employee operations
 */
@Service
@Transactional
public class EmployeeService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private BranchRepository branchRepository;
    
    /**
     * Get all employees
     * @return List<EmployeeResponseDto>
     */
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getAllEmployees() {
        logger.info("Retrieving all employees");
        List<Employee> employees = employeeRepository.findAllWithBranch();
        return employees.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get employee by id
     * @param id employee id
     * @return EmployeeResponseDto
     */
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeById(Long id) {
        logger.info("Retrieving employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return convertToResponseDto(employee);
    }
    
    /**
     * Get employee by employee code
     * @param employeeCode employee code
     * @return EmployeeResponseDto
     */
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeByCode(String employeeCode) {
        logger.info("Retrieving employee with code: {}", employeeCode);
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        return convertToResponseDto(employee);
    }
    
    /**
     * Get employees by branch id
     * @param branchId branch id
     * @return List<EmployeeResponseDto>
     */
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByBranchId(Long branchId) {
        logger.info("Retrieving employees for branch id: {}", branchId);
        
        // Verify branch exists
        if (!branchRepository.existsById(branchId)) {
            throw new ResourceNotFoundException("Branch not found with id: " + branchId);
        }
        
        List<Employee> employees = employeeRepository.findByBranchId(branchId);
        return employees.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Create a new employee
     * @param employeeRequestDto employee request data
     * @return EmployeeResponseDto
     */
    public EmployeeResponseDto createEmployee(EmployeeRequestDto employeeRequestDto) {
        logger.info("Creating new employee with code: {}", employeeRequestDto.getEmployeeCode());
        
        // Validate phone number format
        validatePhoneNumber(employeeRequestDto.getPhoneNumber());
        
        // Check if employee with same code already exists
        if (employeeRepository.existsByEmployeeCode(employeeRequestDto.getEmployeeCode())) {
            throw new DataConflictException("Employee with code '" + employeeRequestDto.getEmployeeCode() + "' already exists");
        }
        
        // Check if employee with same email already exists
        if (employeeRequestDto.getEmail() != null && 
            employeeRepository.existsByEmail(employeeRequestDto.getEmail())) {
            throw new DataConflictException("Employee with email '" + employeeRequestDto.getEmail() + "' already exists");
        }
        
        // Verify branch exists
        Branch branch = branchRepository.findById(employeeRequestDto.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + employeeRequestDto.getBranchId()));
        
        Employee employee = convertToEntity(employeeRequestDto, branch);
        Employee savedEmployee = employeeRepository.save(employee);
        logger.info("Employee created successfully with id: {}", savedEmployee.getId());
        
        return convertToResponseDto(savedEmployee);
    }
    
    /**
     * Update an existing employee
     * @param id employee id
     * @param employeeRequestDto employee request data
     * @return EmployeeResponseDto
     */
    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto employeeRequestDto) {
        logger.info("Updating employee with id: {}", id);
        
        // Validate phone number format
        validatePhoneNumber(employeeRequestDto.getPhoneNumber());
        
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        
        // Check if employee code is being changed and if new code already exists
        if (!existingEmployee.getEmployeeCode().equals(employeeRequestDto.getEmployeeCode()) && 
            employeeRepository.existsByEmployeeCodeAndIdNot(employeeRequestDto.getEmployeeCode(), id)) {
            throw new DataConflictException("Employee with code '" + employeeRequestDto.getEmployeeCode() + "' already exists");
        }
        
        // Check if email is being changed and if new email already exists
        if (employeeRequestDto.getEmail() != null && 
            !employeeRequestDto.getEmail().equals(existingEmployee.getEmail()) &&
            employeeRepository.existsByEmailAndIdNot(employeeRequestDto.getEmail(), id)) {
            throw new DataConflictException("Employee with email '" + employeeRequestDto.getEmail() + "' already exists");
        }
        
        // Verify branch exists
        Branch branch = branchRepository.findById(employeeRequestDto.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + employeeRequestDto.getBranchId()));
        
        // Update employee fields
        existingEmployee.setEmployeeCode(employeeRequestDto.getEmployeeCode());
        existingEmployee.setFirstName(employeeRequestDto.getFirstName());
        existingEmployee.setLastName(employeeRequestDto.getLastName());
        existingEmployee.setEmail(employeeRequestDto.getEmail());
        existingEmployee.setPhoneNumber(employeeRequestDto.getPhoneNumber());
        existingEmployee.setHireDate(employeeRequestDto.getHireDate());
        existingEmployee.setPosition(employeeRequestDto.getPosition());
        existingEmployee.setAddress(employeeRequestDto.getAddress());
        existingEmployee.setBranch(branch);
        
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        logger.info("Employee updated successfully with id: {}", updatedEmployee.getId());
        
        return convertToResponseDto(updatedEmployee);
    }
    
    /**
     * Delete an employee
     * @param id employee id
     */
    public void deleteEmployee(Long id) {
        logger.info("Deleting employee with id: {}", id);
        
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        
        employeeRepository.delete(employee);
        logger.info("Employee deleted successfully with id: {}", id);
    }
    
    /**
     * Search employees by name
     * @param name employee name
     * @return List<EmployeeResponseDto>
     */
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> searchEmployeesByName(String name) {
        logger.info("Searching employees by name: {}", name);
        List<Employee> employees = employeeRepository.findByFirstNameContainingIgnoreCase(name);
        return employees.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Search employees by position
     * @param position employee position
     * @return List<EmployeeResponseDto>
     */
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> searchEmployeesByPosition(String position) {
        logger.info("Searching employees by position: {}", position);
        List<Employee> employees = employeeRepository.findByPositionContainingIgnoreCase(position);
        return employees.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Validate phone number format
     * @param phoneNumber phone number to validate
     * @throws DataConflictException if phone number format is invalid
     */
    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new DataConflictException("Phone number is required");
        }
        
        // Remove spaces and other characters, keep only digits
        String cleanPhoneNumber = phoneNumber.replaceAll("[^0-9]", "");
        
        // Check if phone number has exactly 12 digits
        if (cleanPhoneNumber.length() != 12) {
            throw new DataConflictException("Phone number must be exactly 12 digits long");
        }
        
        // Check if phone number starts with "08"
        if (!cleanPhoneNumber.startsWith("08")) {
            throw new DataConflictException("Phone number must start with '08'");
        }
    }
    
    /**
     * Convert Employee entity to EmployeeResponseDto
     * @param employee Employee entity
     * @return EmployeeResponseDto
     */
    private EmployeeResponseDto convertToResponseDto(Employee employee) {
        return new EmployeeResponseDto(
            employee.getId(),
            employee.getEmployeeCode(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getEmail(),
            employee.getPhoneNumber(),
            employee.getHireDate(),
            employee.getPosition(),
            employee.getAddress(),
            employee.getCreatedAt(),
            employee.getUpdatedAt(),
            employee.getBranch().getId(),
            employee.getBranch().getCode(),
            employee.getBranch().getName()
        );
    }
    
    /**
     * Convert EmployeeRequestDto to Employee entity
     * @param employeeRequestDto EmployeeRequestDto
     * @param branch Branch entity
     * @return Employee entity
     */
    private Employee convertToEntity(EmployeeRequestDto employeeRequestDto, Branch branch) {
        return new Employee(
            employeeRequestDto.getEmployeeCode(),
            employeeRequestDto.getFirstName(),
            employeeRequestDto.getLastName(),
            employeeRequestDto.getEmail(),
            employeeRequestDto.getPhoneNumber(),
            employeeRequestDto.getHireDate(),
            employeeRequestDto.getPosition(),
            employeeRequestDto.getAddress(),
            branch
        );
    }
}
