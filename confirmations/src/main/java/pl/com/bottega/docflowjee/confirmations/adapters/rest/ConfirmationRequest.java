package pl.com.bottega.docflowjee.confirmations.adapters.rest;

import lombok.Data;

import java.util.UUID;

@Data
public class ConfirmationRequest {

    private Long employeeId;
    private UUID documentId;

}
