package pl.com.bottega.docflowjee.docflow;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import static pl.com.bottega.docflowjee.docflow.DocumentOperation.ARCHIVE;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.CREATE_NEW_VERSION;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.PASS_TO_VERIFICATION;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.PUBLISH;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.REJECT;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.UPDATE;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.VERIFY;
import static pl.com.bottega.docflowjee.docflow.EmployeePosition.LEAD_QMA;
import static pl.com.bottega.docflowjee.docflow.EmployeePosition.QMA;
import static pl.com.bottega.docflowjee.docflow.EmployeePosition.SENIOR_QMA;

enum DocumentStatus {

    DRAFT(UPDATE, PASS_TO_VERIFICATION, ARCHIVE),
    WAITING_VERIFICATION(UPDATE, VERIFY, ARCHIVE, REJECT),
    VERIFIED(UPDATE, PUBLISH, ARCHIVE),
    PUBLISHED(ARCHIVE, CREATE_NEW_VERSION, PUBLISH),
    ARCHIVED;

    private Set<DocumentOperation> allowedOperations;

    DocumentStatus(DocumentOperation... allowedOperations) {
        this.allowedOperations = HashSet.of(allowedOperations);
    }

    void ensureOpPermitted(DocumentOperation op) {
        if (!allowedOperations.contains(op)) {
            throw new IllegalDocumentOperationException(String.format("Cannot %s in %s state", op.name(), name()));
        }
    }
}

enum DocumentOperation {

    CREATE(QMA, LEAD_QMA, SENIOR_QMA),
    UPDATE(QMA, LEAD_QMA, SENIOR_QMA),
    PASS_TO_VERIFICATION(QMA, LEAD_QMA, SENIOR_QMA),
    REJECT(LEAD_QMA, SENIOR_QMA),
    VERIFY(LEAD_QMA, SENIOR_QMA),
    PUBLISH(LEAD_QMA),
    ARCHIVE(LEAD_QMA),
    CREATE_NEW_VERSION(LEAD_QMA);

    private Set<EmployeePosition> allowedPositions;

    DocumentOperation(EmployeePosition... allowedPositions) {
        this.allowedPositions = HashSet.of(allowedPositions);
    }

    void ensureOpAllowedFor(EmployeePosition position) {
        if (!allowedPositions.contains(position)) {
            throw new IllegalDocumentOperationException(String.format("Cannot %s in %s position", name(), position.name()));
        }
    }
}
