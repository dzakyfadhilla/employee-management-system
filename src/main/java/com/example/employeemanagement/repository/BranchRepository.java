package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Branch entity
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    
    /**
     * Find branch by code
     * @param code branch code
     * @return Optional<Branch>
     */
    Optional<Branch> findByCode(String code);
    
    /**
     * Find branches by name containing (case insensitive)
     * @param name branch name
     * @return List<Branch>
     */
    List<Branch> findByNameContainingIgnoreCase(String name);
    
    /**
     * Check if branch exists by code
     * @param code branch code
     * @return boolean
     */
    boolean existsByCode(String code);
    
    /**
     * Check if branch exists by code excluding current id
     * @param code branch code
     * @param id branch id to exclude
     * @return boolean
     */
    boolean existsByCodeAndIdNot(String code, Long id);
    
    /**
     * Find branches with employee count
     * @return List<Branch>
     */
    @Query("SELECT b FROM Branch b LEFT JOIN FETCH b.employees")
    List<Branch> findAllWithEmployees();
    
    /**
     * Count employees in a branch
     * @param branchId branch id
     * @return long
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.branch.id = :branchId")
    long countEmployeesByBranchId(Long branchId);
}
