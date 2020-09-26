package pl.com.bottega.docflowjee.confirmations.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.com.bottega.docflowjee.confirmations.adapters.db.MongoConfirmationRepository;
import pl.com.bottega.docflowjee.confirmations.adapters.rest.ConfirmationRequest;
import pl.com.bottega.docflowjee.confirmations.domain.ConfirmationRepository;
import pl.com.bottega.docflowjee.confirmations.domain.EmployeesInDepartments;
import pl.com.bottega.docflowjee.docflow.events.DocumentPublishedEvent;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureWireMock(port = 9191)
@AutoConfigureWebClient
public class ConfirmationTest {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private MongoConfirmationRepository mongoConfirmationRepository;

    @Autowired
    private ConfirmationRepository confirmationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    private UUID documentId = UUID.randomUUID();
    private Set<Long> departmentIds = Set.of(1L, 2L, 3L);
    private Integer version = 100;
    private List<Long> employees = List.of(1L, 2L, 3L, 4L, 5L, 6L);

    @Test
    public void createsConfirmationsWhenDocumentIsPublished() throws Exception {
        // given
        var event = new DocumentPublishedEvent(documentId, Instant.now(), departmentIds, version);
        stubFor(get("/employee-departments")
            .willReturn(aResponse()
                .withHeader("Content-type", "application/json")
                .withBody(objectMapper.writeValueAsString(new EmployeesInDepartments(employees)))
            ));

        // when
        jmsTemplate.convertAndSend("docflow/DocumentPublishedEvent", event);

        // then
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(mongoConfirmationRepository.count().block()).isEqualTo(6);
        });
    }

    @Test
    public void confirmsDocument() throws JsonProcessingException {
        // given
        var event = new DocumentPublishedEvent(documentId, Instant.now(), departmentIds, version);
        stubFor(get("/employee-departments")
            .willReturn(aResponse()
                .withHeader("Content-type", "application/json")
                .withBody(objectMapper.writeValueAsString(new EmployeesInDepartments(employees)))
            ));
        jmsTemplate.convertAndSend("docflow/DocumentPublishedEvent", event);
        await().untilAsserted(() -> {
            assertThat(mongoConfirmationRepository.count().block()).isGreaterThanOrEqualTo(6);
        });

        // when
        webTestClient.put()
            .uri("/confirmations")
            .body(Mono.just(new ConfirmationRequest(employees.get(0), documentId)), ConfirmationRequest.class)
            .exchange()
            .expectStatus()
            .is2xxSuccessful();

        // then
        var confirmation = confirmationRepository.findFor(documentId, employees.get(0));
        assertThat(confirmation.block().isConfirmed()).isTrue();
    }

}
