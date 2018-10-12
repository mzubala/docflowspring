package pl.com.bottega.docflowjee.docflow;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import static pl.com.bottega.docflowjee.docflow.DocumentOperation.ARCHIVE;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.CREATE_NEW_VERSION;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.PASS_TO_VERIFICATION;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.PUBLISH;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.UPDATE;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.VERIFY;

enum DocumentStatus {

    DRAFT(UPDATE, PASS_TO_VERIFICATION, ARCHIVE),
    WAITING_VERIFICATION(UPDATE, VERIFY, ARCHIVE),
    VERIFIED(UPDATE, PUBLISH, ARCHIVE),
    PUBLISHED(ARCHIVE, CREATE_NEW_VERSION),
    ARCHIVED;

    private Set<DocumentOperation> allowedOperations;

    DocumentStatus(DocumentOperation... allowedOperations) {
        this.allowedOperations = HashSet.of(allowedOperations);
    }

    void ensureOpPermitted(DocumentOperation op) {
        if(!allowedOperations.contains(op)) {
            throw new IllegalDocumentOperationException(String.format("Cannot %s in %s state", op.name(), name()));
        }
    }
}

enum DocumentOperation {
    UPDATE, PASS_TO_VERIFICATION, VERIFY, PUBLISH, ARCHIVE, CREATE_NEW_VERSION
}
