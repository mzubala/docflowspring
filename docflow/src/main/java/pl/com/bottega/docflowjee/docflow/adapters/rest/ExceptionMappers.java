package pl.com.bottega.docflowjee.docflow.adapters.rest;

import pl.com.bottega.docflowjee.docflow.IllegalDocumentOperationException;
import pl.com.bottega.eventsourcing.ConcurrencyException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

public class ExceptionMappers {

    @Provider
    public static class ConcurrencyExceptionMapper implements ExceptionMapper<ConcurrencyException> {

        @Override
        public Response toResponse(pl.com.bottega.eventsourcing.ConcurrencyException exception) {
            return null;
        }
    }

    @Provider
    public static class IllegalDocumentOperationExceptionMapper implements ExceptionMapper<IllegalDocumentOperationException> {

        @Override
        public Response toResponse(IllegalDocumentOperationException exception) {
            return null;
        }
    }

    private static class Error {
        public String error;

        public Error(Exception ex) {
            this.error = ex.getMessage();
        }
    }

}
