package pl.com.bottega.docflowjee.docflow;

public interface EmployeePermissionsPolicy {

    void checkPermission(Long employeeId, DocumentOperation operation);

}
