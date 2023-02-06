package com.otica.oticaapi.service.people;

import java.util.List;

import com.otica.oticaapi.repository.address.AddressRepository;
import com.otica.oticaapi.service.address.AddressCepConsult;
import com.otica.oticaapi.service.address.AddressService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otica.oticaapi.service.exceptions.CustonException;
import com.otica.oticaapi.model.people.Employee;
import com.otica.oticaapi.repository.people.EmployeeRepository;

@Service
@Log4j2
@AllArgsConstructor
public class EmployeeService{

    private EmployeeRepository employeeRepository;
    private AddressService addressService;


    
    public Employee searchId(Employee employee) {
        log.info("Solicitou busca po ID do Funcionario, ID digitado: " + employee.getId());
        existsEmployee(employee.getId());
        log.info("Funcionario encontrado");
        return employeeRepository.findById(employee.getId()).get();
    }

    public Employee searchCpf(Employee employee){
        log.info("Solicitou Busca por CPF do Funcionario, CPF digitado: " + employee.getCpf());
        existsEmployee(employee.getCpf());
        log.info("Funcionario encontrado");
        return employeeRepository.findByCpf(employee.getCpf()).get();

    }

    public List<Employee> searchNames(Employee employee){
        log.info("Solicitou busca por nome do Funcionario, busca digitada: " + employee.getName());
        return employeeRepository.findByNameContaining(employee.getName());
    }
    
    public List<Employee> list() {
        log.info("Solicitou todos os Funcionarios");
        return employeeRepository.findAll();
    }

    public Employee save(Employee employee) {

        log.info("Solicitou cadastrar novo Funcionario");
        employeeCannotBeRegistered(employee);

        employee.setAddress(addressService.addressCep(employee.getAddress().getCep()));
        addressService.thisAddressDoesNotExist(employee.getAddress());
        log.info("Funcionario cadastrado com sucesso!");

        return employeeRepository.save(employee);

    }

    public Employee alteration(Employee employee) {

        log.info("Solicitou alterar o Funcionario com o ID: " + employee.getId());
        existsEmployee(employee.getId());
        employeeCannotBeRegistered(employee);
        log.info("Funcionario com o ID: " + employee.getId() + ", alterado com sucesso!");
        employee.setAddress(addressService.addressCep(employee.getAddress().getCep()));
        addressService.thisAddressDoesNotExist(employee.getAddress());

        return employeeRepository.save(employee);
    }

    public void delete(Employee employee) {

        log.info("Solicitou excluir Funcionario com o ID: " + employee.getId());
        existsEmployee(employee.getId());
        log.info("Funcionario com o ID: " + employee.getId() + ", excluido com sucesso!");
        employeeRepository.deleteById(employee.getId());
    }






    public void existsEmployee (Long id){
        if (!employeeRepository.existsById(id)){
            throw new CustonException("Esse funcionario nao existe");
        }
    }

    public void existsEmployee (String cpf){
        if (!employeeRepository.existsByCpf(cpf)){
            throw new CustonException("Esse funcionario nao existe");
        }
    }

    public void employeeCannotBeRegistered (Employee employee){
        if (employee.getId() == null){
            if (employeeRepository.existsByCpf(employee.getCpf())){
                throw new CustonException("Ja existe um funcionario com esse cpf cadastrado");
            }
        } else {
            Employee employeeAlteration = employeeRepository.findById(employee.getId()).get();
            if (employeeRepository.existsByCpf(employee.getCpf()) && !employee.getCpf().equals(employeeAlteration.getCpf())){
                throw new CustonException("Ja existe um funcionario cadastrado com esse cpf");
            }
        }
    }
    
}
