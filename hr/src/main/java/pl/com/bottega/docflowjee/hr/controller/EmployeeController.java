package pl.com.bottega.docflowjee.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import pl.com.bottega.docflowjee.hr.controller.request.EmployeeRequest;
import pl.com.bottega.docflowjee.hr.controller.response.ResourceCreatedResponse;
import pl.com.bottega.docflowjee.hr.services.CreateEmployeeCommand;
import pl.com.bottega.docflowjee.hr.services.EmployeeDetails;
import pl.com.bottega.docflowjee.hr.services.EmployeeService;
import pl.com.bottega.docflowjee.hr.services.UpdateEmployeeCommand;

import javax.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping
	public ResourceCreatedResponse create(@Valid @RequestBody EmployeeRequest request, OAuth2Authentication auth) {
		var employee = new CreateEmployeeCommand(
				request.getFirstName(), request.getLastName(), request.getDepartmentIds(), request.getSupervisorId(),
				request.getPosition()
		);
		var id = employeeService.create(employee);
		return new ResourceCreatedResponse(id);
	}

	@PutMapping("/{id}")
	public void update(@PathVariable("id") Long id, @Valid @RequestBody EmployeeRequest request) {
		employeeService.update(new UpdateEmployeeCommand(
				id, request.getFirstName(), request.getLastName(), request.getDepartmentIds(),
				request.getSupervisorId(), request.getPosition()
		));
	}

	@GetMapping("/{id}")
	public EmployeeDetails get(@PathVariable("id") Long id) {
		return employeeService.get(id);
	}
}
