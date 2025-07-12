package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.BranchRequestDto;
import com.example.employeemanagement.dto.BranchResponseDto;
import com.example.employeemanagement.service.BranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST Controller for Branch operations
 */
@RestController
@RequestMapping("/api/branches")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BranchController {
    
    private static final Logger logger = LoggerFactory.getLogger(BranchController.class);
    
    @Autowired
    private BranchService branchService;
    
    /**
     * Get all branches
     * @return List<BranchResponseDto>
     */
    @GetMapping
    public ResponseEntity<List<BranchResponseDto>> getAllBranches() {
        logger.info("REST request to get all branches");
        List<BranchResponseDto> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }
    
    /**
     * Get branch by id
     * @param id branch id
     * @return BranchResponseDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<BranchResponseDto> getBranchById(@PathVariable Long id) {
        logger.info("REST request to get branch by id: {}", id);
        BranchResponseDto branch = branchService.getBranchById(id);
        return ResponseEntity.ok(branch);
    }
    
    /**
     * Get branch by code
     * @param code branch code
     * @return BranchResponseDto
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<BranchResponseDto> getBranchByCode(@PathVariable String code) {
        logger.info("REST request to get branch by code: {}", code);
        BranchResponseDto branch = branchService.getBranchByCode(code);
        return ResponseEntity.ok(branch);
    }
    
    /**
     * Create a new branch
     * @param branchRequestDto branch request data
     * @return BranchResponseDto
     */
    @PostMapping
    public ResponseEntity<BranchResponseDto> createBranch(@Valid @RequestBody BranchRequestDto branchRequestDto) {
        logger.info("REST request to create branch: {}", branchRequestDto.getCode());
        BranchResponseDto createdBranch = branchService.createBranch(branchRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBranch);
    }
    
    /**
     * Update an existing branch
     * @param id branch id
     * @param branchRequestDto branch request data
     * @return BranchResponseDto
     */
    @PutMapping("/{id}")
    public ResponseEntity<BranchResponseDto> updateBranch(@PathVariable Long id, 
                                                         @Valid @RequestBody BranchRequestDto branchRequestDto) {
        logger.info("REST request to update branch with id: {}", id);
        BranchResponseDto updatedBranch = branchService.updateBranch(id, branchRequestDto);
        return ResponseEntity.ok(updatedBranch);
    }
    
    /**
     * Delete a branch
     * @param id branch id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        logger.info("REST request to delete branch with id: {}", id);
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Search branches by name
     * @param name branch name
     * @return List<BranchResponseDto>
     */
    @GetMapping("/search")
    public ResponseEntity<List<BranchResponseDto>> searchBranchesByName(@RequestParam String name) {
        logger.info("REST request to search branches by name: {}", name);
        List<BranchResponseDto> branches = branchService.searchBranchesByName(name);
        return ResponseEntity.ok(branches);
    }
}
