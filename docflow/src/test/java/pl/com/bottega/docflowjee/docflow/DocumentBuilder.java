package pl.com.bottega.docflowjee.docflow;

import org.mockito.internal.util.collections.Sets;
import pl.com.bottega.docflowjee.docflow.commands.ArchiveDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.PassToVerificationCommand;
import pl.com.bottega.docflowjee.docflow.commands.PublishDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.UpdateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.VerifyDocumentCommand;

import java.time.Clock;
import java.util.Set;
import java.util.UUID;

public class DocumentBuilder {

    private UUID id;
    private Clock clock;
    private Long editorId;
    private Long verifierId = 3L;
    private Long publisherId = 4L;
    private String title;
    private String content;
    private boolean passedToVerification;
    private boolean verified;
    private Set<Long> departmentIds;
    private boolean archived;
    private Long aggregateVersion = 1L;
    private EmployeePermissionsPolicy employeePermissionsPolicy;

    public DocumentBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public DocumentBuilder editorId(Long id) {
        this.editorId = id;
        return this;
    }

    public DocumentBuilder verifierId(Long id) {
        this.verifierId = id;
        return this;
    }

    public DocumentBuilder publisherId(Long id) {
        this.publisherId = id;
        return this;
    }

    public DocumentBuilder clock(Clock clock) {
        this.clock = clock;
        return this;
    }

    public Document build() {
        Document document = new Document(new CreateDocumentCommand(id, editorId), clock, employeePermissionsPolicy);
        if(title != null || content != null) {
            document.update(new UpdateDocumentCommand(id, editorId, title, content, aggregateVersion));
        }
        if(passedToVerification) {
            document.passToVerification(new PassToVerificationCommand(id, editorId, aggregateVersion));
        }
        if(verified) {
            document.verify(new VerifyDocumentCommand(id, verifierId, aggregateVersion));
        }
        if(departmentIds != null) {
            document.publish(new PublishDocumentCommand(id, publisherId, departmentIds, aggregateVersion));
        }
        if(archived) {
            document.archive(new ArchiveDocumentCommand(id, editorId, aggregateVersion));
        }
        document.markChangesCommited();
        return document;
    }

    public DocumentBuilder withTitleAndContent(String title, String content) {
        this.title = title;
        this.content = content;
        return this;
    }

    public DocumentBuilder passedToVerification() {
        passedToVerification = true;
        if(title == null) {
            title = "test";
        }
        if(content == null) {
            content = "test";
        }
        return this;
    }

    public DocumentBuilder verified() {
        passedToVerification();
        verified = true;
        return this;
    }

    public DocumentBuilder publishedFor(Set<Long> departmentIds) {
        verified();
        this.departmentIds = departmentIds;
        return this;
    }

    public DocumentBuilder draft() {
        return withTitleAndContent("any", "any");
    }

    public DocumentBuilder published() {
        return publishedFor(Sets.newSet(1L));
    }

    public DocumentBuilder archived() {
        archived = true;
        return this;
    }

    public DocumentBuilder employeePermissionsPolicy(EmployeePermissionsPolicy employeePermissionsPolicy) {
        this.employeePermissionsPolicy = employeePermissionsPolicy;
        return this;
    }
}
