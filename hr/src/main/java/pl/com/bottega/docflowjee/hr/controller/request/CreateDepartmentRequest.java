package pl.com.bottega.docflowjee.hr.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDepartmentRequest {

    @NotEmpty
    private String name;

}
