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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static pl.com.bottega.docflowjee.docflow.AggregateRootAssertions.assertThatAggregate;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.ARCHIVE;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.CREATE;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.CREATE_NEW_VERSION;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.PASS_TO_VERIFICATION;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.PUBLISH;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.REJECT;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.UPDATE;
import static pl.com.bottega.docflowjee.docflow.DocumentOperation.VERIFY;

public class DocumentTest {

    private UUID id = UUID.randomUUID();
    private Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    private Long editorId = 1L;
    private Long otherEditorId = 2L;
    private Long verifierId = 3L;
    private Long publisherId = 4L;
    private EmployeePermissionsPolicy employeePermissionsPolicy = mock(EmployeePermissionsPolicy.class);
    private DocumentBuilder documentBuilder = new DocumentBuilder().id(id).clock(clock).editorId(editorId)
        .verifierId(verifierId).publisherId(publisherId).employeePermissionsPolicy(employeePermissionsPolicy);
    private Set<Long> departmentIds = Sets.newSet(1L, 2L);
    private Integer firstVersion = 1;
    private Integer secondVersion = 2;
    private Long aggregateVersion = 1L;

    @Test
    public void createsDocument() {
        //when
        Document document = new Document(new CreateDocumentCommand(id, editorId), clock, employeePermissionsPolicy);

        // then
        assertThatAggregate(document).emittedExactly(new DocumentCreatedEvent(id, clock.instant(), editorId));
    }

    @Test
    public void updatesDocument() {
        //given
        Document document = documentBuilder.build();
        String title = "test title";
        String content = "test content";

        //when
        document.update(new UpdateDocumentCommand(id, editorId, title, content, aggregateVersion));

        // then
        assertThatAggregate(document).emittedExactly(new DocumentUpdatedEvent(id, editorId, clock.instant(), title, content, firstVersion));
    }

    @Test
    public void doesNotCreateDocumentIfEmployeeIsNotAllowed() {
        // given
        doThrow(IllegalDocumentOperationException.class).when(employeePermissionsPolicy).checkPermission(editorId, CREATE);

        // then
        assertThatThrownBy(() -> new Document(new CreateDocumentCommand(id, editorId), clock, employeePermissionsPolicy)).isInstanceOf(IllegalDocumentOperationException.class);
    }

    @Test
    public void doesNotUpdateDocumentTitleAndContentAreUnchanged() {
        //given
        Document document = documentBuilder.withTitleAndContent("test", "test").build();

        //when
        document.update(new UpdateDocumentCommand(id, editorId, "test", "test", aggregateVersion));

        //then
        assertThatAggregate(document).emittedNoEvents();
    }

    @Test
    public void doesNotUpdateDocumentIfEmployeeIsNotAllowed() {
        // given
        doThrow(IllegalDocumentOperationException.class).when(employeePermissionsPolicy).checkPermission(editorId, UPDATE);
        Document document = documentBuilder.build();

        // then
        assertThatThrownBy(() -> document.update(new UpdateDocumentCommand(id, editorId, "test", "test", aggregateVersion))).isInstanceOf(IllegalDocumentOperationException.class);
    }

    @Test
    public void sendsDocumentToVerification() {
        // given
        Document document = documentBuilder.withTitleAndContent("test", "test").build();

        // when
        document.passToVerification(new PassToVerificationCommand(id, editorId, aggregateVersion));

        assertThatAggregate(document).emittedExactly(new DocumentPassedToVerification(id, clock.instant(), firstVersion));
    }

    @Test
    public void doesNotSendDocumentToVerificationIfEmployeeIsNotAllowed() {
        // given
        Document document = documentBuilder.withTitleAndContent("test", "test").build();
        doThrow(IllegalDocumentOperationException.class).when(employeePermissionsPolicy).checkPermission(editorId, PASS_TO_VERIFICATION);

        // when
        assertThatThrownBy(() ->
            document.passToVerification(new PassToVerificationCommand(id, editorId, aggregateVersion))
        ).isInstanceOf(IllegalDocumentOperationException.class);
    }

    @Test
    public void doesNothingWhenTryingToSendToVerificationIfItWasAlreadyDone() {
        // given
        Document document = documentBuilder.passedToVerification().build();

        // when
        document.passToVerification(new PassToVerificationCommand(id, editorId, aggregateVersion));

        // then
        assertThatAggregate(document).emittedNoEvents();
    }

    @Test
    public void cannotSendToVerificationWhenDocumentHasEmptyContentOrTitle() {
        // given
        List<Document> docs = List.of(
            documentBuilder.withTitleAndContent("", "test").build(),
            documentBuilder.withTitleAndContent("", null).build()
        );

        //then
        docs.forEach((doc) -> assertThatThrownBy(() ->
            doc.passToVerification(new PassToVerificationCommand(id, editorId, aggregateVersion)))
            .isInstanceOf(IllegalDocumentOperationException.class)
        );
    }

    @Test
    public void updatesDocumentSentToVerification() {
        // given
        Document document = documentBuilder.passedToVerification().build();

        // when
        document.update(new UpdateDocumentCommand(id, editorId, "new test", "new test", aggregateVersion));

        // then
        assertThatAggregate(document).emittedExactly(
            new DocumentUpdatedEvent(id, editorId, clock.instant(), "new test", "new test", firstVersion)
        );
    }

    @Test
    public void verifiesDocument() {
        // given
        Document document = documentBuilder.passedToVerification().build();

        // when
        document.verify(new VerifyDocumentCommand(id, verifierId, aggregateVersion));

        // then
        assertThatAggregate(document).emittedExactly(new DocumentVerifiedEvent(id, clock.instant(), firstVersion));
    }

    @Test
    public void doesNotVerifyDocumentIfEmployeeIsNotAllowed() {
        // given
        Document document = documentBuilder.passedToVerification().build();
        doThrow(IllegalDocumentOperationException.class).when(employeePermissionsPolicy).checkPermission(verifierId, VERIFY);

        // when
        assertThatThrownBy(() ->
            document.verify(new VerifyDocumentCommand(id, verifierId, aggregateVersion))
        ).isInstanceOf(IllegalDocumentOperationException.class);
    }

    @Test
    public void doesNothingWhenTryingToVerifyIfItWasAlreadyDone() {
        // given
        Document document = documentBuilder.verified().build();

        // when
        document.verify(new VerifyDocumentCommand(id, editorId, aggregateVersion));

        // then
        assertThatAggregate(document).emittedNoEvents();
    }

    @Test
    public void updatesVerifiedDocument() {
        // given
        Document document = documentBuilder.verified().build();

        // when
        document.update(new UpdateDocumentCommand(id, editorId, "new test", "new test", aggregateVersion));

        // then
        assertThatAggregate(document).emittedExactly(
            new DocumentUpdatedEvent(id, editorId, clock.instant(), "new test", "new test", firstVersion)
        );
    }

    @Test
    public void rejectsDocument() {
        // given
        String reason = "test reason";
        Document document = documentBuilder.passedToVerification().build();

        // when
        document.reject(new RejectDocumentCommand(id, editorId, reason, aggregateVersion));

        // then
        assertThatAggregate(document).emittedExactly(new DocumentRejectedEvent(id, clock.instant(), reason, firstVersion));
    }

    @Test
    public void doesNotRejectDocumentIfEmployeeIsNotAllowed() {
        // given
        Document document = documentBuilder.passedToVerification().build();
        doThrow(IllegalDocumentOperationException.class).when(employeePermissionsPolicy).checkPermission(editorId, REJECT);
        String reason = "test reason";

        // when
        assertThatThrownBy(() ->
            document.reject(new RejectDocumentCommand(id, editorId, reason, aggregateVersion))
        ).isInstanceOf(IllegalDocumentOperationException.class);
    }

    @Test
    public void doesNotRejectWhenEmployeeIsNotAllowed() {
        // given
        Document document = documentBuilder.passedToVerification().build();
        document.reject(new RejectDocumentCommand(id, verifierId, "test", aggregateVersion));
        document.markChangesCommited();

        // when
        document.reject(new RejectDocumentCommand(id, verifierId, "test", aggregateVersion));

        // then
        assertThatAggregate(document).emittedNoEvents();
    }

    @Test
    public void publishesDocumentForTheFirstTime() {
        // given
        Document document = documentBuilder.verified().build();

        // when
        document.publish(new PublishDocumentCommand(id, editorId, departmentIds, aggregateVersion));

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
        document.publish(new PublishDocumentCommand(id, editorId, moreDepartmentIds, aggregateVersion));

        // then
        assertThatAggregate(document).emittedExactly(new DocumentPublishedEvent(id, clock.instant(), Sets.newSet(4L), firstVersion));
    }

    @Test
    public void doesNotPublishIfEmployeeIsNotAllowed() {
        // given
        Document document = documentBuilder.verified().build();
        doThrow(IllegalDocumentOperationException.class).when(employeePermissionsPolicy).checkPermission(editorId, PUBLISH);

        // then
        assertThatThrownBy(() ->
            document.publish(new PublishDocumentCommand(id, editorId, departmentIds, aggregateVersion))
        ).isInstanceOf(IllegalDocumentOperationException.class);
    }

    @Test
    public void doesNothingWhenTryingToPublishForAlreadyPublishedDepratments() {
        // given
        Document document = documentBuilder.publishedFor(departmentIds).build();

        // when
        document.publish(new PublishDocumentCommand(id, editorId, departmentIds, aggregateVersion));

        // then
        assertThatAggregate(document).emittedNoEvents();
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
            assertThatThrownBy(() -> doc.publish(new PublishDocumentCommand(id, editorId, departmentIds, aggregateVersion)))
                .isInstanceOf(IllegalDocumentOperationException.class)
        );
    }

    @Test
    public void cannotEditPublishedDocument() {
        // given
        Document document = documentBuilder.published().build();

        //then
        assertThatThrownBy(() -> document.update(new UpdateDocumentCommand(id, editorId, "test", "test", aggregateVersion)))
            .isInstanceOf(IllegalDocumentOperationException.class);
    }

    @Test
    public void createsNewDocumentVersion() {
        // given
        Document document = documentBuilder.publishedFor(departmentIds).build();

        // when
        document.createNewVersion(new CreateNewDocumentVersionCommand(id, editorId, aggregateVersion));

        // then
        assertThatAggregate(document).emittedExactly(new NewDocumentVersionCreatedEvent(id, clock.instant(), secondVersion));
    }

    @Test
    public void doesNotCreateNewVersionIfEmployeeIsNotAllowed() {
        // given
        Document document = documentBuilder.published().build();
        doThrow(IllegalDocumentOperationException.class).when(employeePermissionsPolicy).checkPermission(editorId, CREATE_NEW_VERSION);

        // then
        assertThatThrownBy(() ->
            document.createNewVersion(new CreateNewDocumentVersionCommand(id, editorId, aggregateVersion))
        ).isInstanceOf(IllegalDocumentOperationException.class);
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
        docs.forEach((doc) -> doc.archive(new ArchiveDocumentCommand(id, editorId, aggregateVersion)));

        //then
        docs.forEach((doc) -> {
            assertThatAggregate(doc).emittedExactly(new DocumentArchivedEvent(id, clock.instant(), firstVersion));
        });
    }

    @Test
    public void doesNotArchiveIfEmployeeIsNotAllowed() {
        // given
        Document document = documentBuilder.published().build();
        doThrow(IllegalDocumentOperationException.class).when(employeePermissionsPolicy).checkPermission(editorId, ARCHIVE);

        // then
        assertThatThrownBy(() ->
            document.archive(new ArchiveDocumentCommand(id, editorId, aggregateVersion))
        ).isInstanceOf(IllegalDocumentOperationException.class);
    }

    @Test
    public void cannotDoAnythingWithArchivedDocument() {
        //given
        Document document = documentBuilder.archived().build();
        List<Runnable> actions = List.of(
            () -> document.update(new UpdateDocumentCommand(id, editorId, "test", "test", aggregateVersion)),
            () -> document.publish(new PublishDocumentCommand(id, editorId, departmentIds, aggregateVersion)),
            () -> document.verify(new VerifyDocumentCommand(id, editorId, aggregateVersion)),
            () -> document.passToVerification(new PassToVerificationCommand(id, editorId, aggregateVersion)),
            () -> document.createNewVersion(new CreateNewDocumentVersionCommand(id, editorId, aggregateVersion)),
            () -> document.reject(new RejectDocumentCommand(id, editorId, "test", aggregateVersion))
        );

        // then
        actions.forEach((action) -> assertThatThrownBy(action::run).isInstanceOf(IllegalDocumentOperationException.class));
    }

    @Test
    public void publishesDocumentForDepartmentsFromPreviousVersionPublication() {
        // given
        Document document = documentBuilder.publishedFor(departmentIds).build();
        document.createNewVersion(new CreateNewDocumentVersionCommand(id, editorId, aggregateVersion));
        document.update(new UpdateDocumentCommand(id, editorId, "test", "test", aggregateVersion));
        document.passToVerification(new PassToVerificationCommand(id, editorId, aggregateVersion));
        document.verify(new VerifyDocumentCommand(id, verifierId, aggregateVersion));
        document.markChangesCommited();

        // when
        document.publish(new PublishDocumentCommand(id, editorId, Sets.newSet(3L), true, aggregateVersion));

        // then
        assertThatAggregate(document).emittedExactly(
            new DocumentPublishedEvent(id, clock.instant(), Sets.newSet(1L, 2L, 3L), secondVersion)
        );
    }

    @Test
    public void editorsCannotVerifyDocument() {
        // given
        Document document = documentBuilder.build();
        document.update(new UpdateDocumentCommand(id, editorId, "title", "content", aggregateVersion));
        document.update(new UpdateDocumentCommand(id, otherEditorId, "title2", "content2", aggregateVersion));
        document.passToVerification(new PassToVerificationCommand(id, editorId, aggregateVersion));

        // then
        assertThatThrownBy(() -> document.verify(new VerifyDocumentCommand(id, editorId, aggregateVersion)))
            .isInstanceOf(IllegalDocumentOperationException.class);
        assertThatThrownBy(() -> document.verify(new VerifyDocumentCommand(id, otherEditorId, aggregateVersion)))
            .isInstanceOf(IllegalDocumentOperationException.class);

    }

}
