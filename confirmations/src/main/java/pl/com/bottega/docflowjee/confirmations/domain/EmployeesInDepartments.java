package pl.com.bottega.docflowjee.confirmations.domain;

import lombok.Data;

import java.util.List;

@Data
public class EmployeesInDepartments {

    private List<Long> employeeIds;

}
