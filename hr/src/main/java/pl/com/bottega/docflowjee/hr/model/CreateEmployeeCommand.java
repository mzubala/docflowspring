package pl.com.bottega.docflowjee.hr.model;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CreateEmployeeCommand {
    private final String firstName;
    private final String lastName;
    private final Long supervisorId;
    @NotEmpty
    private final List<Long> departmentIds;

    public CreateEmployeeCommand(String firstName, String lastName, Long supervisorId, @NotEmpty List<Long> departmentIds) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.supervisorId = supervisorId;
        this.departmentIds = departmentIds;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getSupervisorId() {
        return supervisorId;
    }

    public List<Long> getDepartmentIds() {
        return departmentIds;
    }
}
