package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Employee entity
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    /**
     * Find employee by employee code
     * @param employeeCode employee code
     * @return Optional<Employee>
     */
    Optional<Employee> findByEmployeeCode(String employeeCode);
    
    /**
     * Find employee by email
     * @param email employee email
     * @return Optional<Employee>
     */
    Optional<Employee> findByEmail(String email);
    
    /**
     * Find employees by branch id
     * @param branchId branch id
     * @return List<Employee>
     */
    List<Employee> findByBranchId(Long branchId);
    
    /**
     * Find employees by branch code
     * @param branchCode branch code
     * @return List<Employee>
     */
    @Query("SELECT e FROM Employee e WHERE e.branch.code = :branchCode")
    List<Employee> findByBranchCode(@Param("branchCode") String branchCode);
    
    /**
     * Find employees by first name or last name containing (case insensitive)
     * @param firstName first name to search
     * @param lastName last name to search
     * @return List<Employee>
     */
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    
    /**
     * Find employees by first name containing (case insensitive)
     * @param firstName first name to search
     * @return List<Employee>
     */
    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Employee> findByFirstNameContainingIgnoreCase(@Param("name") String firstName);
    
    /**
     * Find employees by position containing (case insensitive)
     * @param position position to search
     * @return List<Employee>
     */
    List<Employee> findByPositionContainingIgnoreCase(String position);
    
    /**
     * Check if employee exists by employee code
     * @param employeeCode employee code
     * @return boolean
     */
    boolean existsByEmployeeCode(String employeeCode);
    
    /**
     * Check if employee exists by employee code excluding current id
     * @param employeeCode employee code
     * @param id employee id to exclude
     * @return boolean
     */
    boolean existsByEmployeeCodeAndIdNot(String employeeCode, Long id);
    
    /**
     * Check if employee exists by email
     * @param email employee email
     * @return boolean
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if employee exists by email excluding current id
     * @param email employee email
     * @param id employee id to exclude
     * @return boolean
     */
    boolean existsByEmailAndIdNot(String email, Long id);
    
    /**
     * Find all employees with branch information
     * @return List<Employee>
     */
    @Query("SELECT e FROM Employee e JOIN FETCH e.branch")
    List<Employee> findAllWithBranch();
    
    /**
     * Count employees by branch
     * @param branchId branch id
     * @return long
     */
    long countByBranchId(Long branchId);
}
