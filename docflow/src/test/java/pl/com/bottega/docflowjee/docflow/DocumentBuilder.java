package pl.com.bottega.docflowjee.docflow;

import pl.com.bottega.docflowjee.docflow.commands.CreateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.PassToVerificationCommand;
import pl.com.bottega.docflowjee.docflow.commands.PublishDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.UpdateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.VerifyDocumentCommand;

import javax.print.Doc;
import java.time.Clock;
import java.util.Set;
import java.util.UUID;

public class DocumentBuilder {

    private UUID id;
    private Clock clock;
    private Long employeeId;
    private String title;
    private String content;
    private boolean passedToVerification;
    private boolean verified;
    private Set<Long> departmentIds;

    public static Document newDocument(UUID id, Clock clock, Long employeeId) {
        return new DocumentBuilder(id, clock, employeeId).build();
    }

    public DocumentBuilder(UUID id, Clock clock, Long employeeId) {
        this.id = id;
        this.clock = clock;
        this.employeeId = employeeId;
    }

    public DocumentBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public DocumentBuilder clock(Clock clock) {
        this.clock = clock;
        return this;
    }

    public Document build() {
        Document document = new Document(new CreateDocumentCommand(id, employeeId), clock);
        if(title != null || content != null) {
            document.update(new UpdateDocumentCommand(id, employeeId, title, content));
        }
        if(passedToVerification) {
            document.passToVerification(new PassToVerificationCommand(id, employeeId));
        }
        if(verified) {
            document.verify(new VerifyDocumentCommand(id, employeeId));
        }
        if(departmentIds != null) {
            document.publish(new PublishDocumentCommand(id, employeeId, departmentIds));
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
}
