package pl.com.bottega.docflowjee.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.docflowjee.hr.controller.request.EmployeeRequest;
import pl.com.bottega.docflowjee.hr.model.CreateEmployeeCommand;
import pl.com.bottega.docflowjee.hr.model.EmployeeDetails;
import pl.com.bottega.docflowjee.hr.controller.response.ResourceCreatedResponse;
import pl.com.bottega.docflowjee.hr.service.EmployeeService;
import pl.com.bottega.docflowjee.hr.model.UpdateEmployeeCommand;

import javax.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @PostMapping
    public ResourceCreatedResponse create(@Valid @RequestBody EmployeeRequest request) {
        CreateEmployeeCommand cmd = new CreateEmployeeCommand(request.getFirstName(), request.getLastName(), request.getSupervisorId(), request.getDepartmentIds());
        var id = service.create(cmd);
        return new ResourceCreatedResponse(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody EmployeeRequest request) {
        service.update(new UpdateEmployeeCommand(id, request.getFirstName(), request.getLastName(), request.getSupervisorId(), request.getDepartmentIds()));
    }

    @GetMapping("/{id}")
    public EmployeeDetails get(@PathVariable("id") Long id) {
        return service.get(id);
    }


}
