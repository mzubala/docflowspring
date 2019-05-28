package pl.com.bottega.docflowjee.hr.model.repository;

import org.springframework.data.repository.CrudRepository;
import pl.com.bottega.docflowjee.hr.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
