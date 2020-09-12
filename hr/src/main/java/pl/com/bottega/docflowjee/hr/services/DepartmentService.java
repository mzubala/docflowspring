package pl.com.bottega.docflowjee.hr.services;

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
	public Long create(CreateDepartmentCommand createDepartmentCommand) {
		var department = new Department();
		department.setName(createDepartmentCommand.getName());
		departmentRepository.save(department);
		return department.getId();
	}
}
