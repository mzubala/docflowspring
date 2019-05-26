package pl.com.bottega.docflowjee.hr.controller;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.docflowjee.hr.controller.error.NoSuchEmployeeException;
import pl.com.bottega.docflowjee.hr.controller.request.EmployeeRequest;
import pl.com.bottega.docflowjee.hr.controller.response.EmployeeDetails;
import pl.com.bottega.docflowjee.hr.controller.response.ResourceCreatedResponse;
import pl.com.bottega.docflowjee.hr.model.Employee;
import pl.com.bottega.docflowjee.hr.model.repository.DepartmentRepository;
import pl.com.bottega.docflowjee.hr.model.repository.EmployeeRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @PostMapping
    @Transactional
    public ResourceCreatedResponse create(@Valid @RequestBody EmployeeRequest request) {
        var employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setDepartments(Sets.newHashSet(departmentRepository.findAllById(request.getDepartmentIds())));
        if(request.getSupervisorId() != null) {
            employee.setSupervisor(findEmployeeByIdOrThrow(request.getSupervisorId()));
        }
        employee = employeeRepository.save(employee);
        return new ResourceCreatedResponse(employee.getId());
    }

    @PutMapping("/{id}")
    @Transactional
    public void update(@PathVariable("id") Long id, @Valid @RequestBody EmployeeRequest request) {
        var employee = findEmployeeByIdOrThrow(id);
        Employee supervisor = null;
        if(request.getSupervisorId() != null) {
            supervisor = findEmployeeByIdOrThrow(request.getSupervisorId());
        }
        employee.setSupervisor(supervisor);
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setDepartments(Sets.newHashSet(departmentRepository.findAllById(request.getDepartmentIds())));
        employeeRepository.save(employee);
    }

    @GetMapping("/{id}")
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
