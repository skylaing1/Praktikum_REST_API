package org.example.kontokorrent_praktikum;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "department_table")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Department_Name", nullable = false)
    private String departmentName;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setEmployees(ArrayList<Object> objects) {

    }


    public Collection<Employee> getEmployees() {
        return employees;
    }
}
