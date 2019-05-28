package pl.com.bottega.docflowjee.docflow.integration;

import org.springframework.boot.test.web.client.TestRestTemplate;
import pl.com.bottega.docflowjee.docflow.adapters.rest.CreateDocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.DocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.PublishDocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.RejectDocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.UpdateDocumentRequest;

import java.util.UUID;

import static org.assertj.core.api.Java6Assertions.assertThat;
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
        var response = this.restTemplate.postForEntity("/documents/{id}/verification", request, Void.class, id.toString());
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    public void verify(UUID id, DocumentRequest request) {
        var response = this.restTemplate.postForEntity("/documents/{id}/verification/positive", request, Void.class, id.toString());
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    public void reject(UUID id, RejectDocumentRequest request) {
        var response = this.restTemplate.postForEntity("/documents/{id}/verification/negative", request, Void.class, id.toString());
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    public void publish(UUID id, PublishDocumentRequest request) {
        var response = this.restTemplate.postForEntity("/documents/{id}/publication", request, Void.class, id.toString());
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    public void createNewVersion(UUID id, DocumentRequest request) {
        var response = this.restTemplate.postForEntity("/documents/{id}/new-version", request, Void.class, id.toString());
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    public void archive(UUID id, DocumentRequest request) {
        var response = this.restTemplate.postForEntity("/documents/{id}/archivisation", request, Void.class, id.toString());
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }
}
