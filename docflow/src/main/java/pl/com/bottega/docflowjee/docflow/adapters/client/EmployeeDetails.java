package pl.com.bottega.docflowjee.docflow.adapters.client;

import pl.com.bottega.docflowjee.docflow.EmployeePosition;

public class EmployeeDetails {

    public Long id;
    public String firstName;
    public String lastName;
    public Long supervisorId;
    public EmployeePosition position;
    public Iterable<Long> supervisorsHierarchy;
    public Iterable<Long> departmentIds;

}
