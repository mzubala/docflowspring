package pl.com.bottega.docflowjee.docflow;

import pl.com.bottega.docflowjee.docflow.commands.ArchiveDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateNewDocumentVersionCommand;
import pl.com.bottega.docflowjee.docflow.commands.PassToVerificationCommand;
import pl.com.bottega.docflowjee.docflow.commands.PublishDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.RejectDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.UpdateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.VerifyDocumentCommand;
import pl.com.bottega.docflowjee.docflow.events.DocumentArchivedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentCreatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentPassedToVerification;
import pl.com.bottega.docflowjee.docflow.events.DocumentPublishedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentRejectedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentUpdatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentVerifiedEvent;
import pl.com.bottega.docflowjee.docflow.events.NewDocumentVersionCreatedEvent;
import pl.com.bottega.eventsourcing.AggregateRoot;

import java.time.Clock;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.ARCHIVE;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.CREATE_NEW_VERSION;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.PASS_TO_VERIFICATION;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.PUBLISH;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.REJECT;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.UPDATE;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.VERIFY;
import static pl.com.bottega.docflowjee.docflow.DocumentStatus.ARCHIVED;
import static pl.com.bottega.docflowjee.docflow.DocumentStatus.DRAFT;
import static pl.com.bottega.docflowjee.docflow.DocumentStatus.PUBLISHED;
import static pl.com.bottega.docflowjee.docflow.DocumentStatus.VERIFIED;
import static pl.com.bottega.docflowjee.docflow.DocumentStatus.WAITING_VERIFICATION;

public class Document extends AggregateRoot {

    public Document(CreateDocumentCommand cmd, Clock clock) {
        this.clock = clock;
        applyChange(new DocumentCreatedEvent(cmd.documentId, clock.instant(), cmd.employeeId));
    }
    
    public void update(UpdateDocumentCommand cmd) {
        status.ensureOpPermitted(UPDATE);
        applyChange(new DocumentUpdatedEvent(id, cmd.employeeId, clock.instant(), cmd.title, cmd.content, version));
    }

    public void passToVerification(PassToVerificationCommand cmd) {
        status.ensureOpPermitted(PASS_TO_VERIFICATION);
        if(isEmpty(title)) {
            throw new IllegalDocumentOperationException("title cannot be empty when passing to verification");
        }
        if (isEmpty(content)) {
            throw new IllegalDocumentOperationException("content cannot be empty when passing to verification");
        }
        applyChange(new DocumentPassedToVerification(id, clock.instant(), version));
    }

    public void verify(VerifyDocumentCommand cmd) {
        status.ensureOpPermitted(VERIFY);
        if(this.editors.contains(cmd.getEmployeeId())) {
            throw new IllegalDocumentOperationException("Editors cannot verify document");
        }
        applyChange(new DocumentVerifiedEvent(id, clock.instant(), version));
    }

    public void reject(RejectDocumentCommand cmd) {
        status.ensureOpPermitted(REJECT);
        applyChange(new DocumentRejectedEvent(id, clock.instant(), cmd.reason, version));
    }

    public void publish(PublishDocumentCommand cmd) {
        status.ensureOpPermitted(PUBLISH);
        applyChange(new DocumentPublishedEvent(id, clock.instant(), newDepartments(cmd), version));
    }

    public void createNewVersion(CreateNewDocumentVersionCommand cmd) {
        status.ensureOpPermitted(CREATE_NEW_VERSION);
        applyChange(new NewDocumentVersionCreatedEvent(id, clock.instant(), version + 1));
    }

    public void archive(ArchiveDocumentCommand cmd) {
        status.ensureOpPermitted(ARCHIVE);
        applyChange(new DocumentArchivedEvent(id, clock.instant(), version));
    }

    private Clock clock;
    private DocumentStatus status;
    private Integer version;
    private Set<Long> previousVersionPublishedFor = new HashSet<>();
    private Set<Long> editors = new HashSet<>();
    private Set<Long> publishedFor = new HashSet<>();
    private String title;
    private String content;

    private void apply(DocumentCreatedEvent event) {
        this.id = event.getAggregateId();
        this.status = DRAFT;
        this.version = 1;
        this.editors.add(event.getEmployeeId());
    }

    private void apply(DocumentUpdatedEvent event) {
        this.status = DRAFT;
        this.title = event.getTitle();
        this.content = event.getContent();
        this.editors.add(event.getEmployeeId());
    }

    private void apply(DocumentArchivedEvent event) {
        this.status = ARCHIVED;
    }

    private void apply(DocumentPassedToVerification event) {
        this.status = WAITING_VERIFICATION;
    }

    private void apply(DocumentPublishedEvent event) {
        this.publishedFor.addAll(event.getDepartmentIds());
        this.status = PUBLISHED;
    }

    private void apply(DocumentRejectedEvent event) {
        this.status = DRAFT;
    }

    private void apply(DocumentVerifiedEvent event) {
        this.status = VERIFIED;
    }

    private void apply(NewDocumentVersionCreatedEvent event) {
        this.status = DRAFT;
        this.version = event.getVersion();
        this.previousVersionPublishedFor = publishedFor;
        this.publishedFor = new HashSet<>();
    }

    private Set<Long> newDepartments(PublishDocumentCommand cmd) {
        Set<Long> newDepartments = new HashSet<>(cmd.departmentIds);
        if(cmd.includeDepartmentsFromPreviousVersion) {
            newDepartments.addAll(previousVersionPublishedFor);
        }
        newDepartments.removeAll(publishedFor);
        return newDepartments;
    }

}
