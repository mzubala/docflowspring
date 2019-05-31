package pl.com.bottega.docflowjee.confirmations.domain;

import lombok.Value;

import java.util.UUID;

@Value
public class Confirmation {

    private final UUID documentId;
    private final Long employeeId;
    private final Long confirmingEmployeeId = null;
    private final boolean confirmed = false;

    public Confirmation(UUID documentId, Long employeeId) {
        this.documentId = documentId;
        this.employeeId = employeeId;
    }
}
