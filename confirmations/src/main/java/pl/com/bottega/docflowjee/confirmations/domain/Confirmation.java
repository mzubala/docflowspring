package pl.com.bottega.docflowjee.confirmations.domain;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.UUID;

@Value
@Wither
@AllArgsConstructor
public class Confirmation {

    private final UUID documentId;
    private final Long employeeId;
    private final Long confirmingEmployeeId;
    private final boolean confirmed;

    public Confirmation(UUID documentId, Long employeeId) {
        this.documentId = documentId;
        this.employeeId = employeeId;
        this.confirmed = false;
        this.confirmingEmployeeId = null;
    }

    public Confirmation confirm() {
        return withConfirmed(true);
    }
}
