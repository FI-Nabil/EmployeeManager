package com.finabil.employeemanager.repository;

import com.finabil.employeemanager.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Page<Employee> findAll(Pageable pageable);
    @Query("SELECT e FROM Employee e WHERE " +
            "(LOWER(e.fullName) LIKE LOWER(CONCAT('%', :fullName, '%')) OR :fullName IS NULL) AND " +
            "(e.dateOfBirth = :dateOfBirth OR :dateOfBirth IS NULL) AND " +
            "(LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%')) OR :email IS NULL) AND " +
            "(e.mobile LIKE CONCAT('%', :mobile, '%') OR :mobile IS NULL)")
    List<Employee> findEmployees(@Param("fullName") String fullName,
                                 @Param("dateOfBirth") String dateOfBirth,
                                 @Param("email") String email,
                                 @Param("mobile") String mobile);
    void deleteById(Integer id);
}
