package pl.com.bottega.docflowjee.docflow.adapters.client;

import pl.com.bottega.docflowjee.docflow.EmployeePosition;
import pl.com.bottega.docflowjee.docflow.HrFacade;

public class RestClientHrFacade implements HrFacade {
    private HrClient hrClient;

    public RestClientHrFacade(HrClient hrClient) {
        this.hrClient = hrClient;
    }

    @Override
    public EmployeePosition getEmployeePosition(Long employeeId) {
        var details = hrClient.getEmployeeDetails(employeeId);
        return details.position;
    }
}
