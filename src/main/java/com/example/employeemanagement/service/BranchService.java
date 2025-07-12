package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.BranchRequestDto;
import com.example.employeemanagement.dto.BranchResponseDto;
import com.example.employeemanagement.entity.Branch;
import com.example.employeemanagement.exception.DataConflictException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.BranchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Branch operations
 */
@Service
@Transactional
public class BranchService {
    
    private static final Logger logger = LoggerFactory.getLogger(BranchService.class);
    
    @Autowired
    private BranchRepository branchRepository;
    
    /**
     * Get all branches
     * @return List<BranchResponseDto>
     */
    @Transactional(readOnly = true)
    public List<BranchResponseDto> getAllBranches() {
        logger.info("Retrieving all branches");
        List<Branch> branches = branchRepository.findAll();
        return branches.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get branch by id
     * @param id branch id
     * @return BranchResponseDto
     */
    @Transactional(readOnly = true)
    public BranchResponseDto getBranchById(Long id) {
        logger.info("Retrieving branch with id: {}", id);
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
        return convertToResponseDto(branch);
    }
    
    /**
     * Get branch by code
     * @param code branch code
     * @return BranchResponseDto
     */
    @Transactional(readOnly = true)
    public BranchResponseDto getBranchByCode(String code) {
        logger.info("Retrieving branch with code: {}", code);
        Branch branch = branchRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with code: " + code));
        return convertToResponseDto(branch);
    }
    
    /**
     * Create a new branch
     * @param branchRequestDto branch request data
     * @return BranchResponseDto
     */
    public BranchResponseDto createBranch(BranchRequestDto branchRequestDto) {
        logger.info("Creating new branch with code: {}", branchRequestDto.getCode());
        
        // Check if branch with same code already exists
        if (branchRepository.existsByCode(branchRequestDto.getCode())) {
            throw new DataConflictException("Branch with code '" + branchRequestDto.getCode() + "' already exists");
        }
        
        Branch branch = convertToEntity(branchRequestDto);
        Branch savedBranch = branchRepository.save(branch);
        logger.info("Branch created successfully with id: {}", savedBranch.getId());
        
        return convertToResponseDto(savedBranch);
    }
    
    /**
     * Update an existing branch
     * @param id branch id
     * @param branchRequestDto branch request data
     * @return BranchResponseDto
     */
    public BranchResponseDto updateBranch(Long id, BranchRequestDto branchRequestDto) {
        logger.info("Updating branch with id: {}", id);
        
        Branch existingBranch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
        
        // Check if branch code is being changed and if new code already exists
        if (!existingBranch.getCode().equals(branchRequestDto.getCode()) && 
            branchRepository.existsByCodeAndIdNot(branchRequestDto.getCode(), id)) {
            throw new DataConflictException("Branch with code '" + branchRequestDto.getCode() + "' already exists");
        }
        
        // Update branch fields
        existingBranch.setCode(branchRequestDto.getCode());
        existingBranch.setName(branchRequestDto.getName());
        existingBranch.setAddress(branchRequestDto.getAddress());
        existingBranch.setPhoneNumber(branchRequestDto.getPhoneNumber());
        
        Branch updatedBranch = branchRepository.save(existingBranch);
        logger.info("Branch updated successfully with id: {}", updatedBranch.getId());
        
        return convertToResponseDto(updatedBranch);
    }
    
    /**
     * Delete a branch
     * @param id branch id
     */
    public void deleteBranch(Long id) {
        logger.info("Deleting branch with id: {}", id);
        
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
        
        // Check if branch has employees
        long employeeCount = branchRepository.countEmployeesByBranchId(id);
        if (employeeCount > 0) {
            throw new DataConflictException("Cannot delete branch with " + employeeCount + " employees. Please reassign employees first.");
        }
        
        branchRepository.delete(branch);
        logger.info("Branch deleted successfully with id: {}", id);
    }
    
    /**
     * Search branches by name
     * @param name branch name
     * @return List<BranchResponseDto>
     */
    @Transactional(readOnly = true)
    public List<BranchResponseDto> searchBranchesByName(String name) {
        logger.info("Searching branches by name: {}", name);
        List<Branch> branches = branchRepository.findByNameContainingIgnoreCase(name);
        return branches.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert Branch entity to BranchResponseDto
     * @param branch Branch entity
     * @return BranchResponseDto
     */
    private BranchResponseDto convertToResponseDto(Branch branch) {
        int employeeCount = branch.getEmployees() != null ? branch.getEmployees().size() : 0;
        // If employees not loaded, count from repository
        if (branch.getEmployees() == null) {
            employeeCount = (int) branchRepository.countEmployeesByBranchId(branch.getId());
        }
        
        return new BranchResponseDto(
            branch.getId(),
            branch.getCode(),
            branch.getName(),
            branch.getAddress(),
            branch.getPhoneNumber(),
            branch.getCreatedAt(),
            branch.getUpdatedAt(),
            employeeCount
        );
    }
    
    /**
     * Convert BranchRequestDto to Branch entity
     * @param branchRequestDto BranchRequestDto
     * @return Branch entity
     */
    private Branch convertToEntity(BranchRequestDto branchRequestDto) {
        return new Branch(
            branchRequestDto.getCode(),
            branchRequestDto.getName(),
            branchRequestDto.getAddress(),
            branchRequestDto.getPhoneNumber()
        );
    }
}
