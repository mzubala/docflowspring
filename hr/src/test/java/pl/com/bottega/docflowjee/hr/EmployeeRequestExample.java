package pl.com.bottega.docflowjee.hr;

import lombok.Builder;
import lombok.Getter;
import pl.com.bottega.docflowjee.hr.controller.request.EmployeeRequest;

import java.util.List;

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

    public EmployeeRequest toRequest() {
        return new EmployeeRequest(firstName, lastName, departmentIds, supervisorId);
    }
}
