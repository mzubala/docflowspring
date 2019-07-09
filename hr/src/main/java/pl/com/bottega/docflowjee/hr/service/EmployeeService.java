package pl.com.bottega.docflowjee.hr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import pl.com.bottega.docflowjee.hr.controller.error.NoSuchEmployeeException;
import pl.com.bottega.docflowjee.hr.model.CreateEmployeeCommand;
import pl.com.bottega.docflowjee.hr.model.Employee;
import pl.com.bottega.docflowjee.hr.model.EmployeeDetails;
import pl.com.bottega.docflowjee.hr.model.UpdateEmployeeCommand;
import pl.com.bottega.docflowjee.hr.model.repository.DepartmentRepository;
import pl.com.bottega.docflowjee.hr.model.repository.EmployeeRepository;

import javax.transaction.Transactional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    public Long create(CreateEmployeeCommand employeeCommand) {
        var employee = new Employee(employeeCommand, employeeRepository, departmentRepository);
        employee = employeeRepository.save(employee);
        return employee.getId();
    }

    @Transactional
    public void update(UpdateEmployeeCommand updateEmployeeCommand) {
        var employee = findEmployeeByIdOrThrow(updateEmployeeCommand.getId());
        employee.update(updateEmployeeCommand, employeeRepository, departmentRepository);
        employeeRepository.save(employee);
    }

    public EmployeeDetails get(@PathVariable("id") Long id) {
        return findEmployeeByIdOrThrow(id).details();
    }

    private Employee findEmployeeByIdOrThrow(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new NoSuchEmployeeException(id));
    }
}
