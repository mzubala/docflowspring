package pl.com.bottega.docflowjee.hr.service;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import pl.com.bottega.docflowjee.hr.controller.error.NoSuchEmployeeException;
import pl.com.bottega.docflowjee.hr.model.Employee;
import pl.com.bottega.docflowjee.hr.model.repository.DepartmentRepository;
import pl.com.bottega.docflowjee.hr.model.repository.EmployeeRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    public Long create(String firstName, String lastName, Long supervisorId, @NotEmpty List<Long> departmentIds) {
        var employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setDepartments(Sets.newHashSet(departmentRepository.findAllById(departmentIds)));
        if(supervisorId != null) {
            employee.setSupervisor(findEmployeeByIdOrThrow(supervisorId));
        }
        employee = employeeRepository.save(employee);
        return employee.getId();
    }

    @Transactional
    public void update(Long id, String firstName, String lastName, Long supervisorId, @NotEmpty List<Long> departmentIds) {
        var employee = findEmployeeByIdOrThrow(id);
        Employee supervisor = null;
        if (supervisorId != null) {
            supervisor = findEmployeeByIdOrThrow(supervisorId);
        }
        employee.setSupervisor(supervisor);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setDepartments(Sets.newHashSet(departmentRepository.findAllById(departmentIds)));
        employeeRepository.save(employee);
    }

    public EmployeeDetails get(@PathVariable("id") Long id) {
        var employee = findEmployeeByIdOrThrow(id);
        var supervisor = employee.getSupervisor();
        return EmployeeDetails.builder()
            .firstName(employee.getFirstName())
            .id(employee.getId())
            .lastName(employee.getLastName())
            .departmentIds(employee.getDepartments().stream().map(d -> d.getId()).collect(Collectors.toList()))
            .supervisorId(supervisor == null ? null : supervisor.getId())
            .supervisorsHierarchy(supervisorsHierarchy(supervisor))
            .build();
    }

    private List<Long> supervisorsHierarchy(Employee supervisor) {
        if(supervisor == null) {
            return List.of();
        } else {
            var l = new LinkedList<Long>();
            l.addAll(supervisorsHierarchy(supervisor.getSupervisor()));
            l.add(supervisor.getId());
            return l;
        }
    }

    private Employee findEmployeeByIdOrThrow(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new NoSuchEmployeeException(id));
    }
}
