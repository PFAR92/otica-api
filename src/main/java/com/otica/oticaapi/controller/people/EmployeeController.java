package com.otica.oticaapi.controller.people;

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

    @GetMapping("/{id}")
    public Employee searchId(@PathVariable Long id){
        return employeeService.searchId(id);
    }

    @GetMapping(value = "/cpf")
    public Employee searchCpf(@RequestParam(value = "cpf") String cpf){
        return employeeService.searchCpf(cpf);
    }

    @GetMapping(value = "/name")
    public Employee searchName(@RequestParam(value = "name")String name){
        return employeeService.searchName(name);
    }

    @GetMapping(value = "/names")
    public List<Employee> searchNames(@RequestParam(value = "name")String name){
        return employeeService.searchNames(name);
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

    @DeleteMapping(value = "/{id}")
    public void delete (@PathVariable Long id){
        employeeService.delete(id);
    }
}
