package com.otica.oticaapi.service.people;

import java.util.List;

import com.otica.oticaapi.repository.address.AddressRepository;
import com.otica.oticaapi.service.address.AddressService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otica.oticaapi.service.exceptions.NotFoundException;
import com.otica.oticaapi.model.people.Employee;
import com.otica.oticaapi.repository.people.EmployeeRepository;

@Service
@Log4j2
public class EmployeeService{
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressService addressService;


    
    public Employee searchId(Employee employee) {
        log.info("Solicitou busca po ID do Funcionario, ID digitado: " + employee.getId());
        if (!employeeRepository.existsById(employee.getId())){
            throw new NotFoundException("Esse Funcionario nao existe!");
        } else {
            log.info("Funcionario encontrado");
            return employeeRepository.findById(employee.getId()).get();
        }
    }

    public Employee searchCpf(Employee employee){
        log.info("Solicitou Busca por CPF do Funcionario, CPF digitado: " + employee.getCpf());
        if (!employeeRepository.existsByCpf(employee.getCpf())){
            throw new NotFoundException("Esse Funcionario nao existe!");
        } else {
            log.info("Funcionario encontrado");
            return employeeRepository.findByCpf(employee.getCpf()).get();
        }
    }

    public List<Employee> searchNames(Employee employee){
        log.info("Solicitou busca por nome do Funcionario, busca digitada: " + employee.getName());
        return employeeRepository.findByNameContaining(employee.getName());
    }

    
    public List<Employee> list() {
        log.info("Solicitou todos os Funcionarios");
        return employeeRepository.findAll();
    }

    
    public ResponseEntity<Employee> save(Employee employee) {

        log.info("Solicitou cadastrar novo Funcionario");
        if (employeeRepository.existsByCpf(employee.getCpf())) {
            throw new NotFoundException("Cpf ja cadastrado!");
        } else {
            String cep = employee.getAddress().getCep();
            employee.setAddress(addressService.addressCep(cep));
            if (!addressRepository.existsById(cep)){
                addressRepository.save(employee.getAddress());
            }
            log.info("Funcionario cadastrado com sucesso!");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(employeeRepository.save(employee));
        }
    }

    
    public ResponseEntity<Employee> alteration(Employee employee) {

        log.info("Solicitou alterar o Funcionario com o ID: " + employee.getId());
        if(!employeeRepository.existsById(employee.getId())) {
            throw new NotFoundException("Esse Funcionario nao existe!");
        }
        Employee employeeAlteration = employeeRepository.findById(employee.getId()).get();
        if (employeeRepository.existsByCpf(employee.getCpf()) && !employeeAlteration.getCpf().equals(employee.getCpf())){
            throw new NotFoundException("Esse cpf ja esta cadastrado em um Funcionário ativo");
        } else {
            log.info("Funcionario com o ID: " + employee.getId() + ", alterado com sucesso!");
            employee.setAddress(addressService.addressCep(employee.getAddress().getCep()));
            if (!addressRepository.existsById(employee.getAddress().getCep())){
                addressRepository.save(employee.getAddress());
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(employeeRepository.save(employee));
        }
        
    }

   
    public void delete(Employee employee) {

        log.info("Solicitou excluir Funcionario com o ID: " + employee.getId());
        if(!employeeRepository.existsById(employee.getId())) {
            throw new NotFoundException("Esse Funcionario nao existe!");
        } else {
            log.info("Funcionario com o ID: " + employee.getId() + ", excluido com sucesso!");
            employeeRepository.deleteById(employee.getId());
            ResponseEntity.noContent().build();
        }

    }
    
}
