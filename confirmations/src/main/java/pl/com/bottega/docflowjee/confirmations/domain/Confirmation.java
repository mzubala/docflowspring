package pl.com.bottega.docflowjee.confirmations.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.UUID;

@Value
@AllArgsConstructor
@Wither
public class Confirmation {

    private final UUID documentId;
    private final Long employeeId;
    private final Long confirmingEmployeeId;
    private final boolean confirmed;

    public Confirmation(UUID documentId, Long employeeId) {
        this(documentId, employeeId, null, false);
    }

    public Confirmation confirm() {
        if(confirmed) {
            throw new IllegalStateException();
        }
        return withConfirmed(true);
    }
}
