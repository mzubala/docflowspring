package pl.com.bottega.docflowjee.hr.controller.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ValidationErrors {
    List<ValidationError> errors;
}


@AllArgsConstructor
@Data
@NoArgsConstructor
class ValidationError {
    String property;
    String error;
}