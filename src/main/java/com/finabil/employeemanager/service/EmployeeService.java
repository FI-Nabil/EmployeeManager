package com.finabil.employeemanager.service;

import com.finabil.employeemanager.model.Employee;
import com.finabil.employeemanager.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void saveEmployeeInDB(Employee employee) {
        employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployee(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        return  employeePage.stream()
                .map(e -> new Employee(e.getId(), e.getPhotoUrl(), e.getFullName(), e.getEmail(), e.getMobile(), e.getDateOfBirth()))
                .toList();
    }

    public List<Employee> searchEmployees(String fullName, String dateOfBirth, String email, String mobile) {
        // Use the repository's custom method to search employees
        return employeeRepository.findEmployees(fullName, dateOfBirth, email, mobile);
    }

    public void deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
    }
}

