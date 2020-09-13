package pl.com.bottega.docflowjee.docflow.integration;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import pl.com.bottega.docflowjee.docflow.adapters.rest.CreateDocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.DocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.PublishDocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.RejectDocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.UpdateDocumentRequest;

import java.util.UUID;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.OK;

public class DocflowClient {

    private TestRestTemplate restTemplate;

    public DocflowClient(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void create(UUID id, CreateDocumentRequest request) {
        var response = this.restTemplate.postForEntity("/documents/{id}", request, Void.class, id.toString());
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    public void update(UUID id, UpdateDocumentRequest request) {
        this.restTemplate.put("/documents/{id}", request, id.toString());
    }

    public void passToVerification(UUID id, DocumentRequest request) {
        var response = this.restTemplate.exchange("/documents/{id}/verification", PUT, new HttpEntity<>(request), Void.class, id.toString());
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    public void verify(UUID id, DocumentRequest request) {
		var response = this.restTemplate.exchange("/documents/{id}/verification/positive", PUT, new HttpEntity<>(request), Void.class, id.toString());
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    public void reject(UUID id, RejectDocumentRequest request) {
		var response = this.restTemplate.exchange("/documents/{id}/verification/negative", PUT, new HttpEntity<>(request), Void.class, id.toString());
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    public void publish(UUID id, PublishDocumentRequest request) {
		var response = this.restTemplate.exchange("/documents/{id}/publication", PUT, new HttpEntity<>(request), Void.class, id.toString());
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    public void createNewVersion(UUID id, DocumentRequest request) {
		var response = this.restTemplate.exchange("/documents/{id}/new-version", PUT, new HttpEntity<>(request), Void.class, id.toString());
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    public void archive(UUID id, DocumentRequest request) {
		var response = this.restTemplate.exchange("/documents/{id}/archivisation", PUT, new HttpEntity<>(request), Void.class, id.toString());
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }
}
