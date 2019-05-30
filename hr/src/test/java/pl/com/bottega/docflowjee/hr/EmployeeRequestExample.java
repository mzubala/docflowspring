package pl.com.bottega.docflowjee.hr;

import lombok.Builder;
import lombok.Getter;
import pl.com.bottega.docflowjee.hr.controller.request.EmployeeRequest;
import pl.com.bottega.docflowjee.hr.model.Position;

import java.util.List;

import static pl.com.bottega.docflowjee.hr.model.Position.EMPLOYEE;

@Builder
@Getter
public class EmployeeRequestExample {
    @Builder.Default
    private String firstName = "John";
    @Builder.Default
    private String lastName = "Doe";
    @Builder.Default
    private List<Long> departmentIds = List.of(1L);
    @Builder.Default
    private Long supervisorId;
    @Builder.Default
    private Position position = EMPLOYEE;

    public EmployeeRequest toRequest() {
        return new EmployeeRequest(firstName, lastName, departmentIds, supervisorId, position);
    }
}
