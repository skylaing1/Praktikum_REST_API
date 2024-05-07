package org.example.kontokorrent_praktikum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employee")
    public List<Employee> getEmployeeList() {
        if (employeeRepository.findAll().isEmpty()) {throw new NotFoundException();}
        return employeeRepository.findAll();
    }

    @PostMapping("/employee/create")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        if (newEmployee.getAge() == null || newEmployee.getFirstName() == null || newEmployee.getLastName() == null) {throw new BadRequestException();}
        return employeeRepository.save(newEmployee);
    }

    @DeleteMapping("/employee/delete/{id}")
    void deleteEmployee(@PathVariable int id) {
        Employee deleteEmployee = new Employee();
        deleteEmployee.setId(id);
        if (employeeRepository.findById(id).isEmpty()) {throw new NotFoundException();}
        if (employeeRepository.findById(id).get().getSubordinates() != null) {
            for (Employee subordinate : employeeRepository.findById(id).get().getSubordinates()) {
                subordinate.setSupervisor(null);
                employeeRepository.save(subordinate);
            }
        }
        employeeRepository.delete(deleteEmployee);
    }

    @PostMapping("/employee/edit/{id}")
    void editEmployee(@PathVariable int id, @RequestBody Employee editEmployee) {
        Employee employee = employeeRepository.findById(id).orElseThrow(NotFoundException::new);
        if (editEmployee.getAge() == null || editEmployee.getFirstName() == null || editEmployee.getLastName() == null) {
            throw new BadRequestException();
        }
        employee.setFirstName(editEmployee.getFirstName());
        employee.setLastName(editEmployee.getLastName());
        employee.setAge(editEmployee.getAge());
        employeeRepository.save(employee);
        }

    @PostMapping("/employee/{id}/addSubordinate/{subordinateId}")
    void addSubordinate(@PathVariable int id, @PathVariable int subordinateId) {
        Employee employee = employeeRepository.findById(id).orElseThrow(NotFoundException::new);
        Employee subordinate = employeeRepository.findById(subordinateId).orElseThrow(NotFoundException::new);
        if (subordinate.getSupervisor() != null) {throw new BadRequestException();}
        subordinate.setSupervisor(employee);
        employee.getSubordinates().add(subordinate);
        employeeRepository.save(employee);
        employeeRepository.save(subordinate);
    }

    @PostMapping("/employee/{id}/removeSubordinate/{subordinateId}")
    void removeSubordinate(@PathVariable int id, @PathVariable int subordinateId) {
        Employee employee = employeeRepository.findById(id).orElseThrow(NotFoundException::new);
        Employee subordinate = employeeRepository.findById(subordinateId).orElseThrow(NotFoundException::new);
        if (employee.getSubordinates() == null || !employee.getSubordinates().contains(subordinate)) {
            throw new BadRequestException();
        }
        subordinate.setSupervisor(null);
        employee.getSubordinates().remove(subordinate);
        employeeRepository.save(subordinate);
        employeeRepository.save(employee);
    }

    @GetMapping("/employee/{id}/subordinates")
    public List<Employee> getSubordinates(@PathVariable int id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(NotFoundException::new);
        if (employee.getSubordinates() == null) {
            throw new NotFoundException();
        }
        return employee.getSubordinates();
    }


}