package com.finabil.employeemanager.controller;

import com.finabil.employeemanager.model.Employee;
import com.finabil.employeemanager.repository.EmployeeRepository;
import com.finabil.employeemanager.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeService employeeService) {
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String getHomePage(){
        return "HomePage";
    }

    @GetMapping("/Employees")
    public String employees(Model model, @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "1") int size){
        model.addAttribute("employeeList", employeeService.getAllEmployee(page, size));
        model.addAttribute("currentPage", page);

        return "Employees";
    }

    @GetMapping("/AddEmployee")
    public String addEmployee(Model model){
        model.addAttribute("employee", new Employee());
        return "AddEmployee";
    }



}
