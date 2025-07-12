package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;
import com.example.employeemanagement.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST Controller for Employee operations
 */
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmployeeController {
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    
    @Autowired
    private EmployeeService employeeService;
    
    /**
     * Get all employees
     * @return List<EmployeeResponseDto>
     */
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees() {
        logger.info("REST request to get all employees");
        List<EmployeeResponseDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    
    /**
     * Get employee by id
     * @param id employee id
     * @return EmployeeResponseDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {
        logger.info("REST request to get employee by id: {}", id);
        EmployeeResponseDto employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }
    
    /**
     * Get employee by employee code
     * @param code employee code
     * @return EmployeeResponseDto
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeByCode(@PathVariable String code) {
        logger.info("REST request to get employee by code: {}", code);
        EmployeeResponseDto employee = employeeService.getEmployeeByCode(code);
        return ResponseEntity.ok(employee);
    }
    
    /**
     * Get employees by branch id
     * @param branchId branch id
     * @return List<EmployeeResponseDto>
     */
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<EmployeeResponseDto>> getEmployeesByBranchId(@PathVariable Long branchId) {
        logger.info("REST request to get employees by branch id: {}", branchId);
        List<EmployeeResponseDto> employees = employeeService.getEmployeesByBranchId(branchId);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * Create a new employee
     * @param employeeRequestDto employee request data
     * @return EmployeeResponseDto
     */
    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeRequestDto employeeRequestDto) {
        logger.info("REST request to create employee: {}", employeeRequestDto.getEmployeeCode());
        EmployeeResponseDto createdEmployee = employeeService.createEmployee(employeeRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }
    
    /**
     * Update an existing employee
     * @param id employee id
     * @param employeeRequestDto employee request data
     * @return EmployeeResponseDto
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable Long id, 
                                                             @Valid @RequestBody EmployeeRequestDto employeeRequestDto) {
        logger.info("REST request to update employee with id: {}", id);
        EmployeeResponseDto updatedEmployee = employeeService.updateEmployee(id, employeeRequestDto);
        return ResponseEntity.ok(updatedEmployee);
    }
    
    /**
     * Delete an employee
     * @param id employee id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        logger.info("REST request to delete employee with id: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Search employees by name
     * @param name employee name
     * @return List<EmployeeResponseDto>
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<EmployeeResponseDto>> searchEmployeesByName(@RequestParam String name) {
        logger.info("REST request to search employees by name: {}", name);
        List<EmployeeResponseDto> employees = employeeService.searchEmployeesByName(name);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * Search employees by position
     * @param position employee position
     * @return List<EmployeeResponseDto>
     */
    @GetMapping("/search/position")
    public ResponseEntity<List<EmployeeResponseDto>> searchEmployeesByPosition(@RequestParam String position) {
        logger.info("REST request to search employees by position: {}", position);
        List<EmployeeResponseDto> employees = employeeService.searchEmployeesByPosition(position);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * Search employees by name (general search endpoint)
     * @param name employee name
     * @return List<EmployeeResponseDto>
     */
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponseDto>> searchEmployees(@RequestParam String name) {
        logger.info("REST request to search employees by name: {}", name);
        List<EmployeeResponseDto> employees = employeeService.searchEmployeesByName(name);
        return ResponseEntity.ok(employees);
    }
}
