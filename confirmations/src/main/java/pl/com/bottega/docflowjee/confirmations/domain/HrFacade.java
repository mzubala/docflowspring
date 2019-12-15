package pl.com.bottega.docflowjee.confirmations.domain;

import reactor.core.publisher.Mono;

import java.util.Set;

public interface HrFacade {

    Mono<EmployeesInDepartments> getEmployeesInDepartment(Set<Long> departmentIds);

}

