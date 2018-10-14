package pl.com.bottega.docflowjee.docflow.adapters.rest;

import pl.com.bottega.docflowjee.docflow.IllegalDocumentOperationException;
import pl.com.bottega.eventsourcing.AggregateNotFoundException;
import pl.com.bottega.eventsourcing.ConcurrencyException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

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

}
