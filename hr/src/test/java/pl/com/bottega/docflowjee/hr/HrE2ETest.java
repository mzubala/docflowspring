package pl.com.bottega.docflowjee.hr;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.bottega.docflowjee.hr.controller.error.ValidationErrors;
import pl.com.bottega.docflowjee.hr.services.EmployeeDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class HrE2ETest {

    @Autowired
    private DbCleaner dbCleaner;

    @Autowired
    private HrClient hrClient;

    @Before
    public void setup() {
        dbCleaner.cleanDb();
    }

    @Test
    public void createsEmployee() {
        // given
        var hrDepId = hrClient.departmentCreated("hr");

        // when
        var employeeRequestExample = EmployeeRequestExample.builder().departmentIds(List.of(hrDepId)).build();
        var createResponse = hrClient.createEmployee(employeeRequestExample);

        // then
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().getId()).isNotNull();

        // when
        EmployeeDetails employeeDetailsFetched = hrClient.getEmployeeDetails(createResponse.getBody().getId());

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
        var response = hrClient.updateEmployee(1L, EmployeeRequestExample.builder().build());

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void createsEmployeeWithSupervisor() {
        // given
        var hrDepId = hrClient.departmentCreated("hr");
        var departmentIds = List.of(hrDepId);
        var supervisor1Id = hrClient.employeeCreated(EmployeeRequestExample.builder().departmentIds(departmentIds).build());
        var supervisor2Id = hrClient.employeeCreated(EmployeeRequestExample.builder().departmentIds(departmentIds).supervisorId(supervisor1Id).build());

        // when
        var employeeId = hrClient.employeeCreated(EmployeeRequestExample.builder().supervisorId(supervisor2Id).departmentIds(departmentIds).build());
        var employeeDetails = hrClient.getEmployeeDetails(employeeId);

        // then
        assertThat(employeeDetails.getSupervisorId()).isEqualTo(supervisor2Id);
        assertThat(employeeDetails.getSupervisorsHierarchy()).containsExactly(supervisor1Id, supervisor2Id);
    }

    @Test
    public void updatesEmployee() {
        // given
        var hrDepId = hrClient.departmentCreated("hr");
        var itDepId = hrClient.departmentCreated("it");
        Long supId = hrClient.employeeCreated(EmployeeRequestExample.builder().departmentIds(List.of(hrDepId)).build());
        Long empId = hrClient.employeeCreated(EmployeeRequestExample.builder().departmentIds(List.of(hrDepId)).build());

        // when
        var employeeRequestExample = EmployeeRequestExample.builder()
            .firstName("Jimi")
            .lastName("Choo")
            .departmentIds(List.of(itDepId))
            .supervisorId(supId)
            .build();
        var updateResponse = hrClient.updateEmployee(empId, employeeRequestExample);

        // then
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        var employeeDetailsFetched = hrClient.getEmployeeDetails(empId);
        assertThat(employeeDetailsFetched.getFirstName()).isEqualTo(employeeRequestExample.getFirstName());
        assertThat(employeeDetailsFetched.getLastName()).isEqualTo(employeeRequestExample.getLastName());
        assertThat(employeeDetailsFetched.getDepartmentIds()).containsAll(employeeRequestExample.getDepartmentIds());
        assertThat(employeeDetailsFetched.getSupervisorId()).isEqualTo(supId);
        assertThat(employeeDetailsFetched.getSupervisorsHierarchy()).containsExactly(supId);
    }

    @Test
    public void validatesCreateEmployeeRequest() {
        // given
        var invalidRequest = EmployeeRequestExample.builder().firstName(null).build();

        // when
        var response = hrClient.createEmployee(invalidRequest, ValidationErrors.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().size()).isEqualTo(1);
    }

    @Test
    public void validatesUpdateEmployeeRequest() {
        // given
        var hrDepId = hrClient.departmentCreated("hr");
        var empId = hrClient.employeeCreated(EmployeeRequestExample.builder().departmentIds(List.of(hrDepId)).build());
        var invalidRequest = EmployeeRequestExample.builder().firstName(null).build();

        // when
        var response = hrClient.createEmployee(invalidRequest, ValidationErrors.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().size()).isEqualTo(1);
    }
}
