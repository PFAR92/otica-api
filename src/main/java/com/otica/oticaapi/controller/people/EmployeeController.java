package com.otica.oticaapi.controller.people;

import org.springframework.http.ResponseEntity;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otica.oticaapi.model.people.Employee;
import com.otica.oticaapi.service.people.EmployeeService;


@RestController
@RequestMapping("/people/employee")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{id}")
    public Employee searchId(@PathVariable Long id){
        return employeeService.searchId(id);
    }

    @GetMapping
    public List<Employee> list(){
        return employeeService.list();
    }

    @PostMapping
    public Employee save (@RequestBody @Valid Employee employee){
        return employeeService.save(employee);
    }

    @PutMapping
    public ResponseEntity<Employee> alteration(@RequestBody @Valid Employee employee){
        return employeeService.alteration(employee);
    }

    @DeleteMapping
    public void delete (@RequestBody Employee employee){
        employeeService.delete(employee);        
    }
}
