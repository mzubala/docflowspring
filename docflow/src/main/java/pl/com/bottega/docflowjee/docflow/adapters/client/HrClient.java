package pl.com.bottega.docflowjee.docflow.adapters.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "hr", url = "${service.hr.url}")
public interface HrClient {

    @RequestMapping(method = RequestMethod.GET, value = "/employees/{id}")
    EmployeeDetails getEmployeeDetails(@PathVariable("id") Long employeeId);

}
