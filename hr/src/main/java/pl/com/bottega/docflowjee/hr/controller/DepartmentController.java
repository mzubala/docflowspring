package pl.com.bottega.docflowjee.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.docflowjee.hr.controller.request.CreateDepartmentRequest;
import pl.com.bottega.docflowjee.hr.controller.response.ResourceCreatedResponse;
import pl.com.bottega.docflowjee.hr.model.Department;
import pl.com.bottega.docflowjee.hr.model.repository.DepartmentRepository;
import pl.com.bottega.docflowjee.hr.services.CreateDepartmentCommand;
import pl.com.bottega.docflowjee.hr.services.DepartmentService;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public ResourceCreatedResponse create(@RequestBody CreateDepartmentRequest createDepartmentRequest) {
        var command = new CreateDepartmentCommand(createDepartmentRequest.getName());
        var id = departmentService.create(command);
        return new ResourceCreatedResponse(id);
    }

}
