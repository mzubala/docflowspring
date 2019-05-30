package pl.com.bottega.docflowjee.docflow;

import org.apache.commons.lang3.StringUtils;
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
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.CREATE;
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

    Document() {}

    public Document(CreateDocumentCommand cmd, Clock clock, EmployeePermissionsPolicy employeePermissionsPolicy) {
        this.clock = clock;
        this.employeePermissionsPolicy = employeePermissionsPolicy;
        employeePermissionsPolicy.checkPermission(cmd.getEmployeeId(), CREATE);
        applyChange(new DocumentCreatedEvent(cmd.getDocumentId(), clock.instant(), cmd.getEmployeeId()));
    }

    public void update(UpdateDocumentCommand cmd) {
        status.ensureOpPermitted(UPDATE);
        employeePermissionsPolicy.checkPermission(cmd.getEmployeeId(), UPDATE);
        if(StringUtils.equals(title, cmd.getTitle()) && StringUtils.equals(content, cmd.getContent())) {
            return;
        }
        applyChange(new DocumentUpdatedEvent(id, cmd.getEmployeeId(), clock.instant(), cmd.getTitle(), cmd.getContent(), version));
    }

    public void passToVerification(PassToVerificationCommand cmd) {
        if(status == WAITING_VERIFICATION) {
            return;
        }
        status.ensureOpPermitted(PASS_TO_VERIFICATION);
        employeePermissionsPolicy.checkPermission(cmd.getEmployeeId(), PASS_TO_VERIFICATION);
        if(isEmpty(title)) {
            throw new IllegalDocumentOperationException("title cannot be empty when passing to verification");
        }
        if (isEmpty(content)) {
            throw new IllegalDocumentOperationException("content cannot be empty when passing to verification");
        }
        applyChange(new DocumentPassedToVerification(id, clock.instant(), version));
    }

    public void verify(VerifyDocumentCommand cmd) {
        if(status == VERIFIED) {
            return;
        }
        status.ensureOpPermitted(VERIFY);
        employeePermissionsPolicy.checkPermission(cmd.getEmployeeId(), VERIFY);
        if(this.editors.contains(cmd.getEmployeeId())) {
            throw new IllegalDocumentOperationException("Editors cannot verify document");
        }
        applyChange(new DocumentVerifiedEvent(id, clock.instant(), version));
    }

    public void reject(RejectDocumentCommand cmd) {
        if(status == DRAFT) {
            return;
        }
        employeePermissionsPolicy.checkPermission(cmd.getEmployeeId(), REJECT);
        status.ensureOpPermitted(REJECT);
        applyChange(new DocumentRejectedEvent(id, clock.instant(), cmd.getReason(), version));
    }

    public void publish(PublishDocumentCommand cmd) {
        status.ensureOpPermitted(PUBLISH);
        employeePermissionsPolicy.checkPermission(cmd.getEmployeeId(), PUBLISH);
        Set<Long> newDepartments = newDepartments(cmd);
        if(newDepartments.isEmpty()) {
            return;
        }
        applyChange(new DocumentPublishedEvent(id, clock.instant(), newDepartments, version));
    }

    public void createNewVersion(CreateNewDocumentVersionCommand cmd) {
        status.ensureOpPermitted(CREATE_NEW_VERSION);
        employeePermissionsPolicy.checkPermission(cmd.getEmployeeId(), CREATE_NEW_VERSION);
        applyChange(new NewDocumentVersionCreatedEvent(id, clock.instant(), version + 1));
    }

    public void archive(ArchiveDocumentCommand cmd) {
        employeePermissionsPolicy.checkPermission(cmd.getEmployeeId(), ARCHIVE);
        if(status == ARCHIVED) {
            return;
        }
        status.ensureOpPermitted(ARCHIVE);
        applyChange(new DocumentArchivedEvent(id, clock.instant(), version));
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    private Clock clock;
    private EmployeePermissionsPolicy employeePermissionsPolicy;
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
        Set<Long> newDepartments = new HashSet<>(cmd.getDepartmentIds());
        if(cmd.isIncludeDepartmentsFromPreviousVersion()) {
            newDepartments.addAll(previousVersionPublishedFor);
        }
        newDepartments.removeAll(publishedFor);
        return newDepartments;
    }

}
