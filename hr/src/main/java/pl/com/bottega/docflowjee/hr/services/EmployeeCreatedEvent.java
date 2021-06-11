package pl.com.bottega.docflowjee.hr.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCreatedEvent {
    private Long employeeId;
}
