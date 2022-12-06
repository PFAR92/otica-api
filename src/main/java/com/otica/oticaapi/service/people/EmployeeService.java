package com.otica.oticaapi.service.people;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger();

    
    public Employee searchId(Employee employee) {
        logger.info("Solicitou busca po ID do Funcionario, ID digitado: " + employee.getId());
        if (!employeeRepository.existsById(employee.getId())){
            throw new NotFoudException("Esse Funcionario nao existe!");
        } else {
            logger.info("Funcionario encontrado");
            return employeeRepository.findById(employee.getId()).get();
        }
    }

    public Employee searchCpf(Employee employee){
        logger.info("Solicitou Busca por CPF do Funcionario, CPF digitado: " + employee.getCpf());
        if (!employeeRepository.existsByCpf(employee.getCpf())){
            throw new NotFoudException("Esse Funcionario nao existe!");
        } else {
            logger.info("Funcionario encontrado");
            return employeeRepository.findByCpf(employee.getCpf()).get();
        }
    }

    public List<Employee> searchNames(Employee employee){
        logger.info("Solicitou busca por nome do Funcionario, busca digitada: " + employee.getName());
        return employeeRepository.findByNameContaining(employee.getName());
    }

    
    public List<Employee> list() {
        logger.info("Solicitou todos os Funcionarios");
        return employeeRepository.findAll();
    }

    
    public Employee save(Employee employee) {

        logger.info("Solicitou cadastrar novo Funcionario");
        if (employeeRepository.existsByCpf(employee.getCpf())) {
            throw new NotFoudException("Cpf ja cadastrado!");
        } else {
            logger.info("Funcionario cadastrado com sucesso");
            return employeeRepository.save(employee);
        }
    }

    
    public ResponseEntity<Employee> alteration(Employee employee) {

        logger.info("Solicitou alterar o Funcionario com o ID: " + employee.getId());
        if(!employeeRepository.existsById(employee.getId())) {
            throw new NotFoudException("Esse Funcionario nao existe!");
        } else {
            logger.info("Funcionario com o ID: " + employee.getId() + ", alterado com sucesso!");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(employeeRepository.save(employee));
        }
        
    }

   
    public void delete(Employee employee) {

        logger.info("Solicitou excluir Funcionario com o ID: " + employee.getId());
        if(!employeeRepository.existsById(employee.getId())) {
            throw new NotFoudException("Esse Funcionario nao existe!");
        } else {
            logger.info("Funcionario com o ID: " + employee.getId() + ", excluido com sucesso!");
            employeeRepository.deleteById(employee.getId());
            ResponseEntity.noContent().build();
        }

    }
    
}
