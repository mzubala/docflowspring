package pl.com.bottega.docflowjee.confirmations.adapters.rest;

import lombok.Data;

import java.util.UUID;

@Data
public class ConfirmationRequest {

    private final Long employeeId;
    private final UUID documentId;

}
