package pl.com.bottega.docflowjee.docflow.adapters.rest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.com.bottega.docflowjee.docflow.IllegalDocumentOperationException;
import pl.com.bottega.eventsourcing.AggregateNotFoundException;
import pl.com.bottega.eventsourcing.ConcurrencyException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ControllerAdvice
public class ExceptionMappers {

    @ExceptionHandler(IllegalDocumentOperationException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public void handleIllegalDocumentOperation() { }

    @ExceptionHandler(AggregateNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public void handleAggregateNotFoundException() {

    }

    @ExceptionHandler(ConcurrencyException.class)
    @ResponseStatus(CONFLICT)
    public void handleConcurrencyException() {

    }
}