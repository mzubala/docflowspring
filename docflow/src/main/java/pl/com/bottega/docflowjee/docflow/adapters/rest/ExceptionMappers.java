package pl.com.bottega.docflowjee.docflow.adapters.rest;

import pl.com.bottega.docflowjee.docflow.IllegalDocumentOperationException;
import pl.com.bottega.eventsourcing.AggregateNotFoundException;
import pl.com.bottega.eventsourcing.ConcurrencyException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

public class ExceptionMappers {

    @Provider
    public static class ConcurrencyExceptionMapper implements ExceptionMapper<ConcurrencyException> {

        @Override
        public Response toResponse(pl.com.bottega.eventsourcing.ConcurrencyException exception) {
            return buildResponse(Response.Status.CONFLICT, exception);
        }
    }

    @Provider
    public static class IllegalDocumentOperationExceptionMapper implements ExceptionMapper<IllegalDocumentOperationException> {

        @Override
        public Response toResponse(IllegalDocumentOperationException exception) {
            return buildResponse(422, exception);
        }
    }

    @Provider
    public static class AggregateNotFoundExceptionMapper implements ExceptionMapper<AggregateNotFoundException> {


        @Override
        public Response toResponse(AggregateNotFoundException exception) {
            return buildResponse(Response.Status.NOT_FOUND, exception);
        }
    }

    @Provider
    public static class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

        @Override
        public Response toResponse(ConstraintViolationException exception) {
            return Response.status(422).entity(new ValidationErrors(exception.getConstraintViolations())).build();
        }
    }

    private static Response buildResponse(Response.Status status, Exception exception) {
        return buildResponse(status.getStatusCode(), exception);
    }

    private static Response buildResponse(int code, Exception exception) {
        return Response.status(code).entity(new Error(exception)).build();
    }

    private static class Error {
        public String error;

        public Error(Exception ex) {
            this.error = ex.getMessage();
        }
    }

    private static class ValidationErrors {

        public Set<ValidationError> errors;

        public ValidationErrors(Set<ConstraintViolation<?>> violations) {
            Map<String, Set<String>> errorsMap = violations.stream().collect(
                groupingBy(
                    (violation) -> violation.getPropertyPath().toString(), mapping(ConstraintViolation::getMessage, toSet())
                )
            );
            this.errors = errorsMap.entrySet().stream()
                .map((entry) -> new ValidationError(entry.getKey(), entry.getValue()))
                .collect(toSet());
        }

    }

    private static class ValidationError {
        public Set<String> messages;
        public String field;

        public ValidationError(String field, Set<String> messages) {
            this.messages = messages;
            this.field = field;
        }
    }

}
