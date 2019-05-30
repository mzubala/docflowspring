package pl.com.bottega.docflowjee.docflow.integration;

import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@Configuration
public class IntegrationTestConfig {

    @Bean
    public TestRestTemplate restTemplate(Environment env) {
        var builder = new RestTemplateBuilder();
        builder = builder.uriTemplateHandler(new LocalHostUriTemplateHandler(env));
        return new TestRestTemplate(builder);
    }

    @Bean
    public DocflowClient docflowClient(TestRestTemplate restTemplate) {
        return new DocflowClient(restTemplate);
    }

}
