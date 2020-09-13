package pl.com.bottega.docflowjee.hr;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.bottega.docflowjee.hr.controller.error.ValidationErrors;
import pl.com.bottega.docflowjee.hr.controller.request.CreateDepartmentRequest;
import pl.com.bottega.docflowjee.hr.services.EmployeeDetails;
import pl.com.bottega.docflowjee.hr.controller.response.ResourceCreatedResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@RunWith(SpringRunner.class)
public class HrE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DbCleaner dbCleaner;

    @Before
    public void setup() {
        dbCleaner.cleanDb();
    }

    @Test
    public void createsEmployee() {
        // given
        var hrDepId = departmentCreated("hr");

        // when
        var employeeRequestExample = EmployeeRequestExample.builder().departmentIds(List.of(hrDepId)).build();
        var createResponse = createEmployee(employeeRequestExample);

        // then
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().getId()).isNotNull();

        // when
        EmployeeDetails employeeDetailsFetched = getEmployeeDetails(createResponse.getBody().getId());

        // then
        assertThat(employeeDetailsFetched.getFirstName()).isEqualTo(employeeRequestExample.getFirstName());
        assertThat(employeeDetailsFetched.getLastName()).isEqualTo(employeeRequestExample.getLastName());
        assertThat(employeeDetailsFetched.getDepartmentIds()).containsAll(employeeRequestExample.getDepartmentIds());
        assertThat(employeeDetailsFetched.getSupervisorId()).isNull();
        assertThat(employeeDetailsFetched.getSupervisorsHierarchy()).isEmpty();
    }

    @Test
    public void returns404WhenTryingToUpdateNonExistingEmployee() {
        // when
        var employeeRequest = EmployeeRequestExample.builder().build();
        var response = updateEmployee(1L, EmployeeRequestExample.builder().build());

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void createsEmployeeWithSupervisor() {
        // given
        var hrDepId = departmentCreated("hr");
        var departmentIds = List.of(hrDepId);
        var supervisor1Id = employeeCreated(EmployeeRequestExample.builder().departmentIds(departmentIds).build());
        var supervisor2Id = employeeCreated(EmployeeRequestExample.builder().departmentIds(departmentIds).supervisorId(supervisor1Id).build());

        // when
        var employeeId = employeeCreated(EmployeeRequestExample.builder().supervisorId(supervisor2Id).departmentIds(departmentIds).build());
        var employeeDetails = getEmployeeDetails(employeeId);

        // then
        assertThat(employeeDetails.getSupervisorId()).isEqualTo(supervisor2Id);
        assertThat(employeeDetails.getSupervisorsHierarchy()).containsExactly(supervisor1Id, supervisor2Id);
    }

    @Test
    public void updatesEmployee() {
        // given
        var hrDepId = departmentCreated("hr");
        var itDepId = departmentCreated("it");
        Long supId = employeeCreated(EmployeeRequestExample.builder().departmentIds(List.of(hrDepId)).build());
        Long empId = employeeCreated(EmployeeRequestExample.builder().departmentIds(List.of(hrDepId)).build());

        // when
        var employeeRequestExample = EmployeeRequestExample.builder()
            .firstName("Jimi")
            .lastName("Choo")
            .departmentIds(List.of(itDepId))
            .supervisorId(supId)
            .build();
        var updateResponse = updateEmployee(empId, employeeRequestExample);

        // then
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        var employeeDetailsFetched = getEmployeeDetails(empId);
        assertThat(employeeDetailsFetched.getFirstName()).isEqualTo(employeeRequestExample.getFirstName());
        assertThat(employeeDetailsFetched.getLastName()).isEqualTo(employeeRequestExample.getLastName());
        assertThat(employeeDetailsFetched.getDepartmentIds()).containsAll(employeeRequestExample.getDepartmentIds());
        assertThat(employeeDetailsFetched.getSupervisorId()).isEqualTo(supId);
        assertThat(employeeDetailsFetched.getSupervisorsHierarchy()).containsExactly(supId);
    }

    private ResponseEntity<Void> updateEmployee(Long empId, EmployeeRequestExample example) {
        return restTemplate.exchange("/employees/" + empId, HttpMethod.PUT, new HttpEntity<>(example.toRequest()), Void.class);
    }

    @Test
    public void validatesCreateEmployeeRequest() {
        // given
        var invalidRequest = EmployeeRequestExample.builder().firstName(null).build();

        // when
        var response = createEmployee(invalidRequest, ValidationErrors.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().size()).isEqualTo(1);
    }

    @Test
    public void validatesUpdateEmployeeRequest() {
        // given
        var hrDepId = departmentCreated("hr");
        var empId = employeeCreated(EmployeeRequestExample.builder().departmentIds(List.of(hrDepId)).build());
        var invalidRequest = EmployeeRequestExample.builder().firstName(null).build();

        // when
        var response = createEmployee(invalidRequest, ValidationErrors.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().size()).isEqualTo(1);
    }

    private Long employeeCreated(EmployeeRequestExample employeeRequestExample) {
        var response = createEmployee(employeeRequestExample);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody().getId();
    }

    private ResponseEntity<ResourceCreatedResponse> createEmployee(EmployeeRequestExample employeeRequestExample) {
        return createEmployee(employeeRequestExample, ResourceCreatedResponse.class);
    }

    private <T> ResponseEntity<T> createEmployee(EmployeeRequestExample employeeRequestExample, Class<T> klass) {
        return restTemplate.postForEntity("/employees", employeeRequestExample.toRequest(), klass);
    }

    private Long departmentCreated(String name) {
        var request = new CreateDepartmentRequest(name);
        var response = restTemplate.postForEntity("/departments", request, ResourceCreatedResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody().getId();
    }

    private EmployeeDetails getEmployeeDetails(Long id) {
        var response = restTemplate.getForEntity("/employees/" + id, EmployeeDetails.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

}
