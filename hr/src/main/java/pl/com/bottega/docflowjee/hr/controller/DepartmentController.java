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

import javax.transaction.Transactional;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @PostMapping
    @Transactional
    public ResourceCreatedResponse create(@RequestBody CreateDepartmentRequest createDepartmentRequest) {
        var department = new Department();
        department.setName(createDepartmentRequest.getName());
        department = departmentRepository.save(department);
        return new ResourceCreatedResponse(department.getId());
    }

}
