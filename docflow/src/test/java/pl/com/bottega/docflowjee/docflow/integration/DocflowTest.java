package pl.com.bottega.docflowjee.docflow.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.bottega.docflowjee.docflow.EmployeePosition;
import pl.com.bottega.docflowjee.docflow.adapters.client.EmployeeDetails;
import pl.com.bottega.docflowjee.docflow.adapters.rest.CreateDocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.DocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.PublishDocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.UpdateDocumentRequest;
import pl.com.bottega.docflowjee.docflow.events.DocumentArchivedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentCreatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentPassedToVerification;
import pl.com.bottega.docflowjee.docflow.events.DocumentPublishedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentUpdatedEvent;
import pl.com.bottega.docflowjee.docflow.events.DocumentVerifiedEvent;
import pl.com.bottega.docflowjee.docflow.events.NewDocumentVersionCreatedEvent;

import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static pl.com.bottega.docflowjee.docflow.EmployeePosition.LEAD_QMA;
import static pl.com.bottega.docflowjee.docflow.EmployeePosition.QMA;
import static pl.com.bottega.docflowjee.docflow.EmployeePosition.SENIOR_QMA;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureWireMock(port = 9090)
@ActiveProfiles("integration")
public class DocflowTest {

	@Autowired
	private DocflowClient client;

	@Autowired
	private JmsEventsAssertions jmsEventsAssertions;

	private UUID docId = UUID.randomUUID();
	private Long empId = 1L;
	private Long verifierId = 2L;
	private Long publisherId = 3L;
	private Set<Long> deps = Set.of(1L, 2L, 3L);

	@Before
	public void setup() {
		jmsEventsAssertions.reset();
	}

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void supportsBasicDocumentFlow() {
		// given
		employeeHasPosition(empId, QMA);
		employeeHasPosition(verifierId, SENIOR_QMA);
		employeeHasPosition(publisherId, LEAD_QMA);

		// when
		long ver = 0;
		client.create(docId, new CreateDocumentRequest(empId));
		client.update(docId, new UpdateDocumentRequest(empId, ver++, "test", "test"));
		client.update(docId, new UpdateDocumentRequest(empId, ver++, "test2", "test2"));
		client.passToVerification(docId, new DocumentRequest(empId, ver++));
		client.verify(docId, new DocumentRequest(verifierId, ver++));
		client.publish(docId, new PublishDocumentRequest(publisherId, ver++, deps, false));

		// then
		jmsEventsAssertions.assertEventsWerePublishedInOrder(
				DocumentCreatedEvent.class,
				DocumentUpdatedEvent.class,
				DocumentUpdatedEvent.class,
				DocumentPassedToVerification.class,
				DocumentVerifiedEvent.class,
				DocumentPublishedEvent.class
		);
	}

	@Test
	public void supportsArchiving() {
		// given
		employeeHasPosition(empId, LEAD_QMA);

		// when
		long ver = 0;
		client.create(docId, new CreateDocumentRequest(empId));
		client.update(docId, new UpdateDocumentRequest(empId, ver++, "test", "test"));
		client.archive(docId, new DocumentRequest(empId, ver++));

		// then
		jmsEventsAssertions.assertEventsWerePublishedInOrder(
				DocumentCreatedEvent.class,
				DocumentUpdatedEvent.class,
				DocumentArchivedEvent.class
		);
	}

	@Test
	public void supportsCreatingNewDocumentVersion() {
		// given
		employeeHasPosition(empId, QMA);
		employeeHasPosition(verifierId, SENIOR_QMA);
		employeeHasPosition(publisherId, LEAD_QMA);

		// when
		long ver = 0;
		client.create(docId, new CreateDocumentRequest(empId));
		client.update(docId, new UpdateDocumentRequest(empId, ver++, "test2", "test2"));
		client.passToVerification(docId, new DocumentRequest(empId, ver++));
		client.verify(docId, new DocumentRequest(verifierId, ver++));
		client.publish(docId, new PublishDocumentRequest(publisherId, ver++, deps, false));
		client.createNewVersion(docId, new DocumentRequest(publisherId, ver));

		// then
		jmsEventsAssertions.assertEventWasPublished(
				NewDocumentVersionCreatedEvent.class
		);
	}

	@Test
	public void shouldRespondWithHttp422OnIllegalDocumentOperation() {
		// given
		employeeHasPosition(empId, LEAD_QMA);
		long ver = 0;
		client.create(docId, new CreateDocumentRequest(empId));

		// when
		var resp = client.publish(docId, new PublishDocumentRequest(publisherId, ver++, deps, false));

		//then
		assertThat(resp.getStatusCode()).isEqualTo(UNPROCESSABLE_ENTITY);
	}

	@Test
	public void shouldRespondWithHttp404WhenDocumentIsNotFound() {
		// when
		var resp = client.archive(docId, new DocumentRequest(empId, 1L));

		// then
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void shouldRespondWithHttp409OnOptimisticLockException() {
		// given
		client.create(docId, new CreateDocumentRequest(empId));
		client.update(docId, new UpdateDocumentRequest(empId, 0L, "test", "test"));

		// when
		var resp = client.update(docId, new UpdateDocumentRequest(empId, 0L, "test2", "test2"));

		// then
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
	}

	@Test
	public void shouldRespondWithHttp422WhenPassingInvalidRequests() {
		assertHttp400(() -> client.create(docId, new CreateDocumentRequest()));
		assertHttp400(() -> client.update(docId, new UpdateDocumentRequest()));
		assertHttp400(() -> client.update(docId, new UpdateDocumentRequest()));
		assertHttp400(() -> client.passToVerification(docId, new DocumentRequest()));
		assertHttp400(() -> client.verify(docId, new DocumentRequest()));
		assertHttp400(() -> client.publish(docId, new PublishDocumentRequest()));
		assertHttp400(() -> client.archive(docId, new DocumentRequest()));
		assertHttp400(() -> client.createNewVersion(docId, new DocumentRequest()));
	}

	@Test
	public void shouldRespondWithHttp422WhenHrServiceIsNotAccessible() {
		// given
		gettingEmployeeDetailsFails(empId, INTERNAL_SERVER_ERROR);

		// when
		var resp = client.create(docId, new CreateDocumentRequest(empId));

		// then
		assertThat(resp.getStatusCode()).isEqualTo(UNPROCESSABLE_ENTITY);
	}

	private void assertHttp400(Supplier<ResponseEntity> sup) {
		assertThat(sup.get().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	private void employeeHasPosition(Long employeeId, EmployeePosition position) {
		EmployeeDetails employeeDetails = new EmployeeDetails();
		employeeDetails.position = position;
		try {
			stubFor(get(urlEqualTo("/employees/" + employeeId))
					.willReturn(aResponse().withBody(objectMapper.writeValueAsString(employeeDetails))
							.withHeader("Content-type", "application/json")));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private void gettingEmployeeDetailsFails(Long employeeId, HttpStatus status) {
		stubFor(get(urlEqualTo("/employees/" + employeeId)).willReturn(aResponse().withStatus(status.value())));
	}
}
