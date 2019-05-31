package pl.com.bottega.docflowjee.confirmations.domain;

import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public interface HrFacade {

    Mono<EmployeesInDepartments> getEmployeesInDepartment(Set<Long> departmentIds);

}

