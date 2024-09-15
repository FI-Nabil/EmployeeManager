package com.finabil.employeemanager.controller;

import com.finabil.employeemanager.model.Employee;
import com.finabil.employeemanager.repository.EmployeeRepository;
import com.finabil.employeemanager.service.EmployeeService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;

@Controller
public class EmployeeDataController {
    private final EmployeeRepository employeeRepository;
    @Value("${file.upload.url}")
    String path;
    private final EmployeeService employeeService;

    public EmployeeDataController(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/saveEmployee")
    public String addEmployee(@ModelAttribute Employee employee, @RequestParam MultipartFile photo, RedirectAttributes redirectAttributes) {

        try {
            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();

            File resizedFile = new File(path + "/" + fileName);
            Thumbnails.of(photo.getInputStream())
                    .size(50, 50)
                    .toFile(resizedFile);
            employee.setPhotoUrl(fileName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        employeeService.saveEmployeeInDB(employee);

        redirectAttributes.addFlashAttribute("success", "Employee added successfully");
        return "redirect:/AddEmployee";
    }

    @GetMapping("/searchEmployee")
    public String searchEmployee(@RequestParam String name,
                                 @RequestParam String dateOfBirth,
                                 @RequestParam String email,
                                 @RequestParam String mobile,
                                 Model model) {
        List<Employee> employees = employeeService.searchEmployees(name, dateOfBirth, email, mobile);

        // Add search results to the model
        model.addAttribute("employees", employees);

        // Return view with search results
        return "searchEmployee";
    }

    @PostMapping("/deleteEmployee")
    public String deleteEmployee(@RequestParam("id") Integer id, Model model) {
        employeeService.deleteEmployee(id);
        model.addAttribute("message", "Employee deleted successfully!");
        return "redirect:/Employees";
    }

    @GetMapping("/EditEmployee")
    public String editEmployee(@RequestParam Integer id, Model model) {
        if(employeeRepository.findById(id).isPresent()) {
            Employee employee = employeeRepository.findById(id).get();
            model.addAttribute("originalEmployee", employee);
            model.addAttribute("employee", new Employee());
            return "EditEmployee";
        }
        return "redirect:/";
    }

    @PostMapping("/UpdateEmployee")
    public String updateEmployee(@ModelAttribute Employee employee) {
        if(employeeRepository.findById(employee.getId()).isPresent()) {
            Employee originalEmployee = employeeRepository.findById(employee.getId()).get();
            if(employee.getEmail() != null && !employee.getEmail().isEmpty()) {
                originalEmployee.setEmail(employee.getEmail());
            }
            if(employee.getMobile() != null && !employee.getMobile().isEmpty()) {
                originalEmployee.setMobile(employee.getMobile());
            }
            if(employee.getPhotoUrl() != null && !employee.getPhotoUrl().isEmpty()) {
                originalEmployee.setPhotoUrl(employee.getPhotoUrl());
            }
            if(employee.getDateOfBirth() != null && !employee.getDateOfBirth().isEmpty()) {
                originalEmployee.setDateOfBirth(employee.getDateOfBirth());
            }
            if(employee.getFullName() != null && !employee.getFullName().isEmpty()) {
                originalEmployee.setFullName(employee.getFullName());
            }
            employeeRepository.save(originalEmployee);
        }
        return "redirect:Employees";
    }

}
