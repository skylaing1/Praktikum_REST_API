package org.example.kontokorrent_praktikum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DepartmentController {

    private DepartmentRepository departmentRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentController(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/department")
    public List<Department> getDepartmentList() {
        if (departmentRepository.findAll().isEmpty()) {throw new NotFoundException();}
        return departmentRepository.findAll();
    }

    @PostMapping("/department/create")
    Department newDepartment(@RequestBody Department newDepartment) {
        if (newDepartment.getDepartmentName() == null) {throw new BadRequestException();}
        return departmentRepository.save(newDepartment);
    }

    @DeleteMapping("/department/delete/{id}")
    void deleteDepartment(@PathVariable int id) {
        Department deleteDepartment = new Department();
        deleteDepartment.setId(id);
        if (departmentRepository.findById(id).isEmpty()) {throw new NotFoundException();}
        for (Employee employee : departmentRepository.findById(id).get().getEmployees()) {
            employee.setDepartment(null);
            employeeRepository.save(employee);
        }
        departmentRepository.delete(deleteDepartment);
    }

    @PutMapping("/department/edit/{id}")
    void editDepartment(@PathVariable int id, @RequestBody Department editDepartment) {
        if (editDepartment.getDepartmentName() == null) {throw new BadRequestException();}
        Department department = departmentRepository.findById(id).orElseThrow(NotFoundException::new);
        department.setDepartmentName(editDepartment.getDepartmentName());
        departmentRepository.save(department);
    }

    @PostMapping("/department/{id}/addEmployee/{employeeId}")
    void addEmployee(@PathVariable int id, @PathVariable int employeeId) {
        Department department = departmentRepository.findById(id).orElseThrow(NotFoundException::new);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(NotFoundException::new);
        if (employee.getDepartment() != null) {throw new BadRequestException();}
        employee.setDepartment(department);
        department.getEmployees().add(employee);
        departmentRepository.save(department);
        employeeRepository.save(employee);
    }

    @PostMapping("/department/{id}/removeEmployee/{employeeId}")
    void removeEmployee(@PathVariable int id, @PathVariable int employeeId) {
        Department department = departmentRepository.findById(id).orElseThrow(NotFoundException::new);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(NotFoundException::new);

        employee.setDepartment(null);
        department.getEmployees().remove(employee);
        departmentRepository.save(department);
        employeeRepository.save(employee);
    }
}
