package com.otica.oticaapi.controller.people;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.otica.oticaapi.model.people.Employee;
import com.otica.oticaapi.service.people.EmployeeService;


@RestController
@RequestMapping("/people/employee")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;

    @GetMapping(value = "/id")
    @ResponseStatus(HttpStatus.OK)
    public Employee searchId(@RequestBody Employee employee){
        return employeeService.searchId(employee);
    }

    @GetMapping(value = "/cpf")
    @ResponseStatus(HttpStatus.OK)
    public Employee searchCpf(@RequestBody Employee employee){
        return employeeService.searchCpf(employee);
    }

    @GetMapping(value = "/names")
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> searchNames(@RequestBody Employee employee){
        return employeeService.searchNames(employee);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> list(){
        return employeeService.list();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee save (@RequestBody @Valid Employee employee){
        return employeeService.save(employee);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Employee alteration(@RequestBody @Valid Employee employee){
        return employeeService.alteration(employee);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@RequestBody Employee employee){
        employeeService.delete(employee);
    }
}
