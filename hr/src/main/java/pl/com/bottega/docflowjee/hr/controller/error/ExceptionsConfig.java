package pl.com.bottega.docflowjee.hr.controller.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionsConfig extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var errors = new ValidationErrors(ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getCode()))
                .collect(Collectors.toList())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrors> handle(ConstraintViolationException ex) {
        var errors = new ValidationErrors(ex.getConstraintViolations().stream().map(
                e -> new ValidationError(e.getPropertyPath().toString(), e.getMessage())
        ).collect(Collectors.toList()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchEmployeeException.class)
    public ResponseEntity<ValidationErrors> handle(NoSuchEmployeeException ex) {
        var errors = new ValidationErrors(List.of(new ValidationError("id", ex.getMessage())));
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

}