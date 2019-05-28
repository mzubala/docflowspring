package pl.com.bottega.docflowjee.docflow.integration;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import pl.com.bottega.docflowjee.docflow.adapters.rest.CreateDocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.DocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.PublishDocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.RejectDocumentRequest;
import pl.com.bottega.docflowjee.docflow.adapters.rest.UpdateDocumentRequest;

import java.util.UUID;

import static org.springframework.http.HttpMethod.PUT;

public class DocflowClient {

    private TestRestTemplate restTemplate;

    public DocflowClient(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Void> create(UUID id, CreateDocumentRequest request) {
        return this.restTemplate.postForEntity("/documents/{id}", request, Void.class, id.toString());
    }

    public ResponseEntity<Object> update(UUID id, UpdateDocumentRequest request) {
        return this.restTemplate.exchange("/documents/{id}", PUT,
            new HttpEntity<>(request), ParameterizedTypeReference.forType(Void.class), id.toString());
    }

    public ResponseEntity<Void> passToVerification(UUID id, DocumentRequest request) {
        return this.restTemplate.postForEntity("/documents/{id}/verification", request, Void.class, id.toString());
    }

    public ResponseEntity<Void> verify(UUID id, DocumentRequest request) {
        return this.restTemplate.postForEntity("/documents/{id}/verification/positive", request, Void.class, id.toString());
    }

    public ResponseEntity<Void> reject(UUID id, RejectDocumentRequest request) {
        return this.restTemplate.postForEntity("/documents/{id}/verification/negative", request, Void.class, id.toString());
    }

    public ResponseEntity<Void> publish(UUID id, PublishDocumentRequest request) {
        return this.restTemplate.postForEntity("/documents/{id}/publication", request, Void.class, id.toString());
    }

    public ResponseEntity<Void> createNewVersion(UUID id, DocumentRequest request) {
        return this.restTemplate.postForEntity("/documents/{id}/new-version", request, Void.class, id.toString());
    }

    public ResponseEntity<Void> archive(UUID id, DocumentRequest request) {
        return this.restTemplate.postForEntity("/documents/{id}/archivisation", request, Void.class, id.toString());
    }
}
