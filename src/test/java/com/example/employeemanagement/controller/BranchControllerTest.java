package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.BranchRequestDto;
import com.example.employeemanagement.dto.BranchResponseDto;
import com.example.employeemanagement.service.BranchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for BranchController
 */
@WebMvcTest(BranchController.class)
class BranchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BranchService branchService;

    @Autowired
    private ObjectMapper objectMapper;

    private BranchResponseDto branchResponseDto;
    private BranchRequestDto branchRequestDto;

    @BeforeEach
    void setUp() {
        branchResponseDto = new BranchResponseDto();
        branchResponseDto.setId(1L);
        branchResponseDto.setCode("HO");
        branchResponseDto.setName("Head Office");
        branchResponseDto.setAddress("Jakarta");
        branchResponseDto.setPhoneNumber("081234567890");
        branchResponseDto.setCreatedAt(LocalDateTime.now());
        branchResponseDto.setUpdatedAt(LocalDateTime.now());
        branchResponseDto.setEmployeeCount(0);

        branchRequestDto = new BranchRequestDto();
        branchRequestDto.setCode("HO");
        branchRequestDto.setName("Head Office");
        branchRequestDto.setAddress("Jakarta");
        branchRequestDto.setPhoneNumber("081234567890");
    }

    @Test
    void getAllBranches_ShouldReturnListOfBranches() throws Exception {
        // Arrange
        List<BranchResponseDto> branches = Arrays.asList(branchResponseDto);
        when(branchService.getAllBranches()).thenReturn(branches);

        // Act & Assert
        mockMvc.perform(get("/api/branches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("HO"))
                .andExpect(jsonPath("$[0].name").value("Head Office"));

        verify(branchService, times(1)).getAllBranches();
    }

    @Test
    void getBranchById_WhenBranchExists_ShouldReturnBranch() throws Exception {
        // Arrange
        when(branchService.getBranchById(1L)).thenReturn(branchResponseDto);

        // Act & Assert
        mockMvc.perform(get("/api/branches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("HO"))
                .andExpect(jsonPath("$.name").value("Head Office"));

        verify(branchService, times(1)).getBranchById(1L);
    }

    @Test
    void createBranch_WithValidData_ShouldCreateBranch() throws Exception {
        // Arrange
        when(branchService.createBranch(any(BranchRequestDto.class))).thenReturn(branchResponseDto);

        // Act & Assert
        mockMvc.perform(post("/api/branches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(branchRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("HO"))
                .andExpect(jsonPath("$.name").value("Head Office"));

        verify(branchService, times(1)).createBranch(any(BranchRequestDto.class));
    }

    @Test
    void updateBranch_WithValidData_ShouldUpdateBranch() throws Exception {
        // Arrange
        when(branchService.updateBranch(eq(1L), any(BranchRequestDto.class))).thenReturn(branchResponseDto);

        // Act & Assert
        mockMvc.perform(put("/api/branches/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(branchRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("HO"))
                .andExpect(jsonPath("$.name").value("Head Office"));

        verify(branchService, times(1)).updateBranch(eq(1L), any(BranchRequestDto.class));
    }

    @Test
    void deleteBranch_ShouldDeleteBranch() throws Exception {
        // Arrange
        doNothing().when(branchService).deleteBranch(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/branches/1"))
                .andExpect(status().isNoContent());

        verify(branchService, times(1)).deleteBranch(1L);
    }

    @Test
    void searchBranches_ShouldReturnMatchingBranches() throws Exception {
        // Arrange
        List<BranchResponseDto> branches = Arrays.asList(branchResponseDto);
        when(branchService.searchBranchesByName("Head")).thenReturn(branches);

        // Act & Assert
        mockMvc.perform(get("/api/branches/search")
                        .param("name", "Head"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Head Office"));

        verify(branchService, times(1)).searchBranchesByName("Head");
    }
}
