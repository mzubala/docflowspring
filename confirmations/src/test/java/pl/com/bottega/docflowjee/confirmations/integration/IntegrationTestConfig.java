package pl.com.bottega.docflowjee.confirmations.integration;

import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.web.reactive.server.WebTestClient;

@Configuration
public class IntegrationTestConfig {

    @Bean
    public WebTestClient webTestClient(Environment env) {
        var builder = WebTestClient.bindToServer();
        builder = builder.baseUrl(new LocalHostUriTemplateHandler(env).getRootUri());
        return builder.build();
    }

}
