package pl.com.bottega.docflowjee.docflow.adapters.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "hr", url = "${service.hr.url}")
public interface HrClient {

    EmployeeDetails getEmployeeDetails(Long employeeId);

}
