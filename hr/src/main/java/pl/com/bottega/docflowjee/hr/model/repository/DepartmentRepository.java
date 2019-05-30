package pl.com.bottega.docflowjee.hr.model.repository;

import org.springframework.data.repository.CrudRepository;
import pl.com.bottega.docflowjee.hr.model.Department;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
}
