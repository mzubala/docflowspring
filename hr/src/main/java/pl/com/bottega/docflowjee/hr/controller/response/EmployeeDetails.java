package pl.com.bottega.docflowjee.hr.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.bottega.docflowjee.hr.model.Position;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetails {

    private Long id;
    private String firstName;
    private String lastName;
    private Long supervisorId;
    private Position position;
    private Iterable<Long> supervisorsHierarchy;
    private Iterable<Long> departmentIds;

}
