package com.otica.oticaapi.service.people;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otica.oticaapi.service.exceptions.NotFoudException;
import com.otica.oticaapi.model.people.Employee;
import com.otica.oticaapi.repository.people.EmployeeRepository;

@Service
public class EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    
    public Employee searchId(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new NotFoudException("Esse Funcionario nao existe!"));
    }

    public Employee searchCpf(String cpf){
        return employeeRepository.findByCpf(cpf).orElseThrow(() -> new NotFoudException("Cpf nao encontrado!"));
    }

    public Employee searchName(String name){
        return employeeRepository.findByName(name).orElseThrow(() -> new NotFoudException("Esse Funcionario nao existe!"));
    }

    public List<Employee> searchNames(String name){
        return employeeRepository.findByNameContaining(name);
    }

    
    public List<Employee> list() {
        return employeeRepository.findAll();
    }

    
    public Employee save(Employee employee) {
        if (employeeRepository.existsByCpf(employee.getCpf()))
            throw new NotFoudException("Cpf ja cadastrado!");
        else return employeeRepository.save(employee);
    }

    
    public ResponseEntity<Employee> alteration(Employee employee) {
        if(!employeeRepository.existsById(employee.getId()))
            throw new NotFoudException("Esse Funcionario nao existe!");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(employeeRepository.save(employee));
        
    }

   
    public void delete(Long id) {
        if(!employeeRepository.existsById(id))
            throw new NotFoudException("Esse Funcionario nao existe!");
        employeeRepository.deleteById(id);
        ResponseEntity.noContent().build();
    }
    
}
