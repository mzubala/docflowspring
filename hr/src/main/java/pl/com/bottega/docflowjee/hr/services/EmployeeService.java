package pl.com.bottega.docflowjee.hr.services;

import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.com.bottega.docflowjee.hr.controller.error.NoSuchEmployeeException;
import pl.com.bottega.docflowjee.hr.controller.response.ResourceCreatedResponse;
import pl.com.bottega.docflowjee.hr.model.Department;
import pl.com.bottega.docflowjee.hr.model.Employee;
import pl.com.bottega.docflowjee.hr.model.repository.DepartmentRepository;
import pl.com.bottega.docflowjee.hr.model.repository.EmployeeRepository;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private NotificationService notificationService;

	@SneakyThrows
	public Long create(CreateEmployeeCommand command) {
		var employee = new Employee();
		employee.setFirstName(command.getFirstName());
		employee.setLastName(command.getLastName());
		employee.setDepartments(Sets.newHashSet(departmentRepository.findAllById(command.getDepartmentIds())));
		if(command.getSupervisorId() != null) {
			employee.setSupervisor(findEmployeeByIdOrThrow(command.getSupervisorId()));
		}
		employee = employeeRepository.save(employee);
		var somethingInTheFuture = notificationService.getSomething();
		var somethingElseInTheFuture = notificationService.getSomethingElse();
		somethingInTheFuture
				.thenCombine(somethingElseInTheFuture, (s1, s2) -> s1 + s2)
				.thenAccept(s -> log.info(s)).get();
		return employee.getId();
	}

	public void update(UpdateEmployeeCommand command) {
		var employee = findEmployeeByIdOrThrow(command.getId());
		Employee supervisor = null;
		if(command.getSupervisorId() != null) {
			supervisor = findEmployeeByIdOrThrow(command.getSupervisorId());
		}
		employee.setSupervisor(supervisor);
		employee.setFirstName(command.getFirstName());
		employee.setLastName(command.getLastName());
		employee.setDepartments(Sets.newHashSet(departmentRepository.findAllById(command.getDepartmentIds())));
		employeeRepository.save(employee);
	}

	public EmployeeDetails get(Long employeeId) {
		var employee = findEmployeeByIdOrThrow(employeeId);
		var supervisor = employee.getSupervisor();
		return EmployeeDetails.builder()
				.firstName(employee.getFirstName())
				.id(employee.getId())
				.lastName(employee.getLastName())
				.departmentIds(employee.getDepartments().stream().map(Department::getId).collect(Collectors.toList()))
				.supervisorId(supervisor == null ? null : supervisor.getId())
				.supervisorsHierarchy(supervisorsHierarchy(supervisor))
				.build();

	}

	private Employee findEmployeeByIdOrThrow(Long id) {
		return employeeRepository.findById(id).orElseThrow(() -> new NoSuchEmployeeException(id));
	}

	private List<Long> supervisorsHierarchy(Employee supervisor) {
		if(supervisor == null) {
			return List.of();
		} else {
			var l = new LinkedList<Long>();
			l.addAll(supervisorsHierarchy(supervisor.getSupervisor()));
			l.add(supervisor.getId());
			return l;
		}
	}
}
