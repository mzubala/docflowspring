package pl.com.bottega.docflowjee.hr.model;

import com.google.common.collect.Sets;
import lombok.Getter;
import pl.com.bottega.docflowjee.hr.model.repository.DepartmentRepository;
import pl.com.bottega.docflowjee.hr.model.repository.EmployeeRepository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @ManyToMany
    @NotEmpty
    private Set<Department> departments;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Employee supervisor;

    Employee() {}

    public Employee(CreateEmployeeCommand cmd, EmployeeRepository repository, DepartmentRepository departmentRepository) {
        this.firstName = cmd.getFirstName();
        this.lastName = cmd.getLastName();
        this.departments = Sets.newHashSet(departmentRepository.findAllById(cmd.getDepartmentIds()));
        if(cmd.getSupervisorId() != null) {
            this.supervisor = repository.findById(cmd.getSupervisorId()).get();
        }
    }

    public void update(UpdateEmployeeCommand cmd, EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.firstName = cmd.getFirstName();
        this.lastName = cmd.getLastName();
        this.departments = Sets.newHashSet(departmentRepository.findAllById(cmd.getDepartmentIds()));
        if(cmd.getSupervisorId() != null) {
            this.supervisor = employeeRepository.findById(cmd.getSupervisorId()).get();
        }
    }

    public EmployeeDetails details() {
        return EmployeeDetails.builder()
            .firstName(firstName)
            .id(id)
            .lastName(lastName)
            .departmentIds(departments.stream().map(Department::getId).collect(Collectors.toList()))
            .supervisorId(supervisor == null ? null : supervisor.getId())
            .supervisorsHierarchy(supervisorsHierarchy())
            .build();
    }

    private List<Long> supervisorsHierarchy() {
        if(supervisor == null) {
            return List.of();
        } else {
            var l = new LinkedList<Long>();
            l.addAll(supervisor.supervisorsHierarchy());
            l.add(supervisor.getId());
            return l;
        }
    }
}
