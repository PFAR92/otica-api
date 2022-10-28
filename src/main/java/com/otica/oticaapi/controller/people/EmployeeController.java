package com.otica.oticaapi.controller.people;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otica.oticaapi.model.people.Employee;
import com.otica.oticaapi.repository.people.EmployeeRepository;


@RestController
@RequestMapping("/people/employee")
public class EmployeeController {
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Employee>> searchId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(employeeRepository.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> list(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(employeeRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Employee> save(@RequestBody Employee employee){
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeRepository.save(employee));
    }

    @PutMapping
    public ResponseEntity<Employee> alteration(@RequestBody Employee employee){
        if(employeeRepository.findById(employee.getId()).isPresent())
            return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(employeeRepository.save(employee));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Employee employee){
        try {
            employeeRepository.deleteById(employee.getId());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        
    }
}
