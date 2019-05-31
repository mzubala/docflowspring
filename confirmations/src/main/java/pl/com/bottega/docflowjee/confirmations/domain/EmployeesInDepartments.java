package pl.com.bottega.docflowjee.confirmations.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeesInDepartments {

    private List<Long> employeeIds;

}
