package pl.com.bottega.docflowjee.hr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.bottega.docflowjee.hr.model.Department;
import pl.com.bottega.docflowjee.hr.model.repository.DepartmentRepository;

import javax.transaction.Transactional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    public Long create(String name) {
        var department = new Department();
        department.setName(name);
        department = departmentRepository.save(department);
        return department.getId();
    }

}
