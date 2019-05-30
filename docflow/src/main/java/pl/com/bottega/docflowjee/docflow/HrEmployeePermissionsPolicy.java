package pl.com.bottega.docflowjee.docflow;

public class HrEmployeePermissionsPolicy implements EmployeePermissionsPolicy {

    private final HrFacade hrFacade;

    public HrEmployeePermissionsPolicy(HrFacade hrFacade) {
        this.hrFacade = hrFacade;
    }

    @Override
    public void checkPermission(Long employeeId, DocumentOperation operation) {
        EmployeePosition employeePosition = hrFacade.getEmployeePosition(employeeId);
        operation.ensureOpAllowedFor(employeePosition);
    }
}