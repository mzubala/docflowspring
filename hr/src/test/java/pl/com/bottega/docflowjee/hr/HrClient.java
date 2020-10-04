package pl.com.bottega.docflowjee.hr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.com.bottega.docflowjee.hr.controller.request.CreateDepartmentRequest;
import pl.com.bottega.docflowjee.hr.controller.response.ResourceCreatedResponse;
import pl.com.bottega.docflowjee.hr.services.EmployeeDetails;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@Component
class HrClient {

    @Autowired
    private TestRestTemplate restTemplate;

    ResponseEntity<Void> updateEmployee(Long empId, EmployeeRequestExample example) {
        var requestEntity = new RequestEntity<>(example.toRequest(), HttpMethod.PUT, uri("/employees/" + empId));
        return restTemplate.exchange(authenticated(requestEntity), Void.class);
    }

    Long employeeCreated(EmployeeRequestExample employeeRequestExample) {
        var response = createEmployee(employeeRequestExample);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody().getId();
    }

    ResponseEntity<ResourceCreatedResponse> createEmployee(EmployeeRequestExample employeeRequestExample) {
        return createEmployee(employeeRequestExample, ResourceCreatedResponse.class);
    }

    <T> ResponseEntity<T> createEmployee(EmployeeRequestExample employeeRequestExample, Class<T> klass) {
        var requestEntity = new RequestEntity<>(employeeRequestExample.toRequest(), HttpMethod.POST,
                uri("/employees"));
        return restTemplate.exchange(authenticated(requestEntity), klass);
    }

    Long departmentCreated(String name) {
        var request = new CreateDepartmentRequest(name);
        var requestEntity = new RequestEntity<>(request, HttpMethod.POST, uri("/departments"));
        var response = restTemplate.exchange(authenticated(requestEntity), ResourceCreatedResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody().getId();
    }

    EmployeeDetails getEmployeeDetails(Long id) {
        var requestEntity = new RequestEntity<>(HttpMethod.GET, uri("/employees/" + id));
        var response = restTemplate.exchange(requestEntity, EmployeeDetails.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

    private URI uri(String endpoint) {
        return URI.create(endpoint);
    }

    private <T> RequestEntity<T> authenticated(RequestEntity<T> requestEntity) {
        return RequestEntity
                .method(requestEntity.getMethod(), requestEntity.getUrl())
                .header("Authorization", "Bearer 123")
                .body(requestEntity.getBody());
    }
}
