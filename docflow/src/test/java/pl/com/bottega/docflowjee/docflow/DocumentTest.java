package pl.com.bottega.docflowjee.docflow;

import io.vavr.collection.List;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;
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

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static pl.com.bottega.docflowjee.docflow.AggregateRootAssertions.assertThatAggregate;
import static pl.com.bottega.docflowjee.docflow.DocumentBuilder.newDocument;

public class DocumentTest {

    private UUID id = UUID.randomUUID();
    private Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    private Long employeeId = 1L;
    private DocumentBuilder documentBuilder = new DocumentBuilder(id, clock, employeeId);
    private Set<Long> departmentIds = Sets.newSet(1L, 2L);
    private Integer firstVersion = 1;
    private Integer secondVersion = 1;

    @Test
    public void createsDocument() {
        //when
        Document document = new Document(new CreateDocumentCommand(id, employeeId), clock);

        // then
        assertThatAggregate(document).emittedExactly(new DocumentCreatedEvent(id, clock.instant(), employeeId));
    }

    @Test
    public void updatesDocument() {
        //given
        Document document = newDocument(id, clock, employeeId);
        String title = "test title";
        String content = "test content";

        //when
        document.update(new UpdateDocumentCommand(id, employeeId, title, content));

        // then
        assertThatAggregate(document).emittedExactly(new DocumentUpdatedEvent(id, clock.instant(), title, content, firstVersion));
    }

    @Test
    public void sendsDocumentToVerification() {
        // given
        Document document = documentBuilder.withTitleAndContent("test", "test").build();

        // when
        document.passToVerification(new PassToVerificationCommand(id, employeeId));

        assertThatAggregate(document).emittedExactly(new DocumentPassedToVerification(id, clock.instant(), firstVersion));
    }

    @Test
    public void updatesDocumentSentToVerification() {
        // given
        Document document = documentBuilder.passedToVerification().build();

        // when
        document.update(new UpdateDocumentCommand(id, employeeId, "new test", "new test"));

        // then
        assertThatAggregate(document).emittedExactly(
            new DocumentUpdatedEvent(id, clock.instant(), "new test", "new test", firstVersion)
        );
    }

    @Test
    public void verifiesDocument() {
        // given
        Document document = documentBuilder.passedToVerification().build();

        // when
        document.verify(new VerifyDocumentCommand(id, employeeId));

        // then
        assertThatAggregate(document).emittedExactly(new DocumentVerifiedEvent(id, clock.instant(), firstVersion));
    }

    @Test
    public void updatesVerifiedDocument() {
        // given
        Document document = documentBuilder.verified().build();

        // when
        document.update(new UpdateDocumentCommand(id, employeeId, "new test", "new test"));

        // then
        assertThatAggregate(document).emittedExactly(
            new DocumentUpdatedEvent(id, clock.instant(), "new test", "new test", firstVersion)
        );
    }

    @Test
    public void rejectsDocument() {
        // given
        String reason = "test reason";
        Document document = documentBuilder.passedToVerification().build();

        // when
        document.reject(new RejectDocumentCommand(id, employeeId, reason));

        // then
        assertThatAggregate(document).emittedExactly(new DocumentRejectedEvent(id, clock.instant(), reason, firstVersion));
    }

    @Test
    public void publishesDocumentForTheFirstTime() {
        // given
        Document document = documentBuilder.verified().build();

        // when
        document.publish(new PublishDocumentCommand(id, employeeId, departmentIds));

        // then
        assertThatAggregate(document).emittedExactly(new DocumentPublishedEvent(id, clock.instant(), departmentIds, firstVersion));

    }

    @Test
    public void publishesDocumentForMoreDepartments() {
        // given
        Document document = documentBuilder.publishedFor(departmentIds).build();

        // when
        Set<Long> moreDepartmentIds = new HashSet<>(departmentIds);
        moreDepartmentIds.add(4L);
        document.publish(new PublishDocumentCommand(id, employeeId, moreDepartmentIds));

        // then
        assertThatAggregate(document).emittedExactly(new DocumentPublishedEvent(id, clock.instant(), Sets.newSet(4L), firstVersion));
    }

    @Test
    public void cannotPublishUnverifiedDocument() {
        // given
        List<Document> docs = List.of(
            documentBuilder.draft().build(),
            documentBuilder.passedToVerification().build()
        );

        // then
        docs.forEach((doc) ->
            assertThatThrownBy(() -> doc.publish(new PublishDocumentCommand(id, employeeId, departmentIds)))
                .isInstanceOf(IllegalDocumentOperationException.class)
        );
    }

    @Test
    public void cannotEditPublishedDocument() {
        // given
        Document document = documentBuilder.published().build();

        //then
        assertThatThrownBy(() -> document.update(new UpdateDocumentCommand(id, employeeId, "test", "test")))
            .isInstanceOf(IllegalDocumentOperationException.class);
    }

    @Test
    public void createsNewDocumentVersion() {
        // given
        Document document = documentBuilder.publishedFor(departmentIds).build();

        // when
        document.createNewVersion(new CreateNewDocumentVersionCommand(id, employeeId));

        // then
        assertThatAggregate(document).emittedExactly(new NewDocumentVersionCreatedEvent(id, clock.instant(), secondVersion));

    }

    @Test
    public void archivesDocuments() {
        // given
        List<Document> docs = List.of(
            documentBuilder.withTitleAndContent("test", "test").build(),
            documentBuilder.passedToVerification().build(),
            documentBuilder.verified().build(),
            documentBuilder.publishedFor(departmentIds).build()
        );

        // when
        docs.forEach((doc) -> doc.archive(new ArchiveDocumentCommand(id, employeeId)));

        //then
        docs.forEach((doc) -> {
            assertThatAggregate(doc).emittedExactly(new DocumentArchivedEvent(id, clock.instant(), firstVersion));
        });
    }

    @Test
    public void cannotDoAnythingWithArchivedDocument() {
        //given
        Document document = documentBuilder.archived().build();
        List<Runnable> actions = List.of(
            () -> document.archive(new ArchiveDocumentCommand(id, employeeId)),
            () -> document.update(new UpdateDocumentCommand(id, employeeId, "test", "test")),
            () -> document.publish(new PublishDocumentCommand(id, employeeId, departmentIds)),
            () -> document.verify(new VerifyDocumentCommand(id, employeeId)),
            () -> document.passToVerification(new PassToVerificationCommand(id, employeeId)),
            () -> document.createNewVersion(new CreateNewDocumentVersionCommand(id, employeeId)),
            () -> document.reject(new RejectDocumentCommand(id, employeeId, "test"))
        );

        // then
        actions.forEach((action) -> assertThatThrownBy(action::run).isInstanceOf(IllegalDocumentOperationException.class));
    }

    @Test
    public void publishesDocumentForDepartmentsFromPreviousVersionPublication() {
        // given
        Document document = documentBuilder.publishedFor(departmentIds).build();
        document.createNewVersion(new CreateNewDocumentVersionCommand(id, employeeId));
        document.markChangesCommited();

        // when
        document.publish(new PublishDocumentCommand(id, employeeId, Sets.newSet(3L), true));

        // then
        assertThatAggregate(document).emittedExactly(
            new DocumentPublishedEvent(id, clock.instant(), Sets.newSet(1L, 2L, 3L), secondVersion)
        );
    }

}
