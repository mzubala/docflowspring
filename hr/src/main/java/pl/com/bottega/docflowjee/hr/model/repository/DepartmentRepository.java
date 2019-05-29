package pl.com.bottega.docflowjee.hr.model.repository;

import org.springframework.data.repository.Repository;
import pl.com.bottega.docflowjee.hr.model.Department;

public interface DepartmentRepository extends Repository<Department, Long> {

    Iterable<Department> findAllById(Iterable<Long> ids);

    Department save(Department dep);

}
