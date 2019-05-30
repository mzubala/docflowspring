package pl.com.bottega.docflowjee.docflow.adapters.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.com.bottega.docflowjee.docflow.EmployeePosition;

@FeignClient(name = "hr", url = "${service.hr.url}", fallback = FallbackHrClient.class)
public interface HrClient {

    @RequestMapping(method = RequestMethod.GET, value = "/employees/{id}")
    EmployeeDetails getEmployeeDetails(@PathVariable("id") Long employeeId);

}

@Component
class FallbackHrClient implements HrClient {

    private EmployeeDetails fallbackDetails = new EmployeeDetails();

    public FallbackHrClient() {
        fallbackDetails.position = EmployeePosition.UNKNOWN;
    }

    @Override
    public EmployeeDetails getEmployeeDetails(Long employeeId) {
        return fallbackDetails;
    }
}