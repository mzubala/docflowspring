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
import pl.com.bottega.docflowjee.hr.services.CreateEmployeeCommand;
import pl.com.bottega.docflowjee.hr.services.EmployeeDetails;
import pl.com.bottega.docflowjee.hr.controller.response.ResourceCreatedResponse;
import pl.com.bottega.docflowjee.hr.model.Employee;
import pl.com.bottega.docflowjee.hr.model.repository.DepartmentRepository;
import pl.com.bottega.docflowjee.hr.model.repository.EmployeeRepository;
import pl.com.bottega.docflowjee.hr.services.EmployeeService;
import pl.com.bottega.docflowjee.hr.services.UpdateEmployeeCommand;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResourceCreatedResponse create(@Valid @RequestBody EmployeeRequest request) {
        var employee = new CreateEmployeeCommand(
        		request.getFirstName(), request.getLastName(), request.getDepartmentIds(), request.getSupervisorId()
		);
        var id = employeeService.create(employee);
        return new ResourceCreatedResponse(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public void update(@PathVariable("id") Long id, @Valid @RequestBody EmployeeRequest request) {
    	employeeService.update(new UpdateEmployeeCommand(
    			id, request.getFirstName(), request.getLastName(), request.getDepartmentIds(),
				request.getSupervisorId()
		));
    }

    @GetMapping("/{id}")
    public EmployeeDetails get(@PathVariable("id") Long id) {
		return employeeService.get(id);
    }
}
