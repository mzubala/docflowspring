package pl.com.bottega.docflowjee.hr.model;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class UpdateEmployeeCommand {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final Long supervisorId;
    @NotEmpty
    private final List<Long> departmentIds;

    public UpdateEmployeeCommand(Long id, String firstName, String lastName, Long supervisorId, @NotEmpty List<Long> departmentIds) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.supervisorId = supervisorId;
        this.departmentIds = departmentIds;
    }

    public Long getId() {
        return id;
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
