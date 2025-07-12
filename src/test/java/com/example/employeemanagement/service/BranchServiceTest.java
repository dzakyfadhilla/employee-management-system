package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.BranchRequestDto;
import com.example.employeemanagement.dto.BranchResponseDto;
import com.example.employeemanagement.entity.Branch;
import com.example.employeemanagement.exception.DataConflictException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BranchService
 */
@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchService branchService;

    private Branch testBranch;
    private BranchRequestDto testBranchRequestDto;

    @BeforeEach
    void setUp() {
        // Setup test data
        testBranch = new Branch();
        testBranch.setId(1L);
        testBranch.setCode("HO");
        testBranch.setName("Head Office");
        testBranch.setAddress("Jakarta");
        testBranch.setPhoneNumber("081234567890");
        testBranch.setCreatedAt(LocalDateTime.now());
        testBranch.setUpdatedAt(LocalDateTime.now());

        testBranchRequestDto = new BranchRequestDto();
        testBranchRequestDto.setCode("HO");
        testBranchRequestDto.setName("Head Office");
        testBranchRequestDto.setAddress("Jakarta");
        testBranchRequestDto.setPhoneNumber("081234567890");
    }

    @Test
    void getAllBranches_ShouldReturnListOfBranches() {
        // Arrange
        List<Branch> branches = Arrays.asList(testBranch);
        when(branchRepository.findAll()).thenReturn(branches);

        // Act
        List<BranchResponseDto> result = branchService.getAllBranches();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("HO", result.get(0).getCode());
        assertEquals("Head Office", result.get(0).getName());
        verify(branchRepository, times(1)).findAll();
    }

    @Test
    void getBranchById_WhenBranchExists_ShouldReturnBranch() {
        // Arrange
        when(branchRepository.findById(1L)).thenReturn(Optional.of(testBranch));

        // Act
        BranchResponseDto result = branchService.getBranchById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("HO", result.getCode());
        assertEquals("Head Office", result.getName());
        verify(branchRepository, times(1)).findById(1L);
    }

    @Test
    void getBranchById_WhenBranchNotExists_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            branchService.getBranchById(1L);
        });
        verify(branchRepository, times(1)).findById(1L);
    }

    @Test
    void getBranchByCode_WhenBranchExists_ShouldReturnBranch() {
        // Arrange
        when(branchRepository.findByCode("HO")).thenReturn(Optional.of(testBranch));

        // Act
        BranchResponseDto result = branchService.getBranchByCode("HO");

        // Assert
        assertNotNull(result);
        assertEquals("HO", result.getCode());
        assertEquals("Head Office", result.getName());
        verify(branchRepository, times(1)).findByCode("HO");
    }

    @Test
    void getBranchByCode_WhenBranchNotExists_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(branchRepository.findByCode("HO")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            branchService.getBranchByCode("HO");
        });
        verify(branchRepository, times(1)).findByCode("HO");
    }

    @Test
    void createBranch_WithValidData_ShouldCreateBranch() {
        // Arrange
        when(branchRepository.existsByCode("HO")).thenReturn(false);
        when(branchRepository.save(any(Branch.class))).thenReturn(testBranch);

        // Act
        BranchResponseDto result = branchService.createBranch(testBranchRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("HO", result.getCode());
        assertEquals("Head Office", result.getName());
        verify(branchRepository, times(1)).existsByCode("HO");
        verify(branchRepository, times(1)).save(any(Branch.class));
    }

    @Test
    void createBranch_WithDuplicateCode_ShouldThrowDataConflictException() {
        // Arrange
        when(branchRepository.existsByCode("HO")).thenReturn(true);

        // Act & Assert
        assertThrows(DataConflictException.class, () -> {
            branchService.createBranch(testBranchRequestDto);
        });
        verify(branchRepository, times(1)).existsByCode("HO");
        verify(branchRepository, never()).save(any(Branch.class));
    }

    @Test
    void updateBranch_WithValidData_ShouldUpdateBranch() {
        // Arrange
        when(branchRepository.findById(1L)).thenReturn(Optional.of(testBranch));
        when(branchRepository.save(any(Branch.class))).thenReturn(testBranch);

        // Act
        BranchResponseDto result = branchService.updateBranch(1L, testBranchRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("HO", result.getCode());
        verify(branchRepository, times(1)).findById(1L);
        verify(branchRepository, times(1)).save(any(Branch.class));
    }

    @Test
    void updateBranch_WithNonExistentBranch_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            branchService.updateBranch(1L, testBranchRequestDto);
        });
        verify(branchRepository, times(1)).findById(1L);
        verify(branchRepository, never()).save(any(Branch.class));
    }

    @Test
    void deleteBranch_WithExistingBranchAndNoEmployees_ShouldDeleteBranch() {
        // Arrange
        when(branchRepository.findById(1L)).thenReturn(Optional.of(testBranch));
        when(branchRepository.countEmployeesByBranchId(1L)).thenReturn(0L);

        // Act
        branchService.deleteBranch(1L);

        // Assert
        verify(branchRepository, times(1)).findById(1L);
        verify(branchRepository, times(1)).countEmployeesByBranchId(1L);
        verify(branchRepository, times(1)).delete(testBranch);
    }

    @Test
    void deleteBranch_WithNonExistentBranch_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            branchService.deleteBranch(1L);
        });
        verify(branchRepository, times(1)).findById(1L);
        verify(branchRepository, never()).delete(any(Branch.class));
    }

    @Test
    void deleteBranch_WithEmployeesAssigned_ShouldThrowDataConflictException() {
        // Arrange
        when(branchRepository.findById(1L)).thenReturn(Optional.of(testBranch));
        when(branchRepository.countEmployeesByBranchId(1L)).thenReturn(5L);

        // Act & Assert
        assertThrows(DataConflictException.class, () -> {
            branchService.deleteBranch(1L);
        });
        verify(branchRepository, times(1)).findById(1L);
        verify(branchRepository, times(1)).countEmployeesByBranchId(1L);
        verify(branchRepository, never()).delete(any(Branch.class));
    }

    @Test
    void searchBranchesByName_ShouldReturnMatchingBranches() {
        // Arrange
        List<Branch> branches = Arrays.asList(testBranch);
        when(branchRepository.findByNameContainingIgnoreCase("Head")).thenReturn(branches);

        // Act
        List<BranchResponseDto> result = branchService.searchBranchesByName("Head");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Head Office", result.get(0).getName());
        verify(branchRepository, times(1)).findByNameContainingIgnoreCase("Head");
    }
}
