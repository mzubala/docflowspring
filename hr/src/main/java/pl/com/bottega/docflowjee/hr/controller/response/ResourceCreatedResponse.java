package pl.com.bottega.docflowjee.hr.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceCreatedResponse {

    @NotNull
    private Long id;

}
