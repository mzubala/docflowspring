package pl.com.bottega.docflowjee.docflow.adapters.configuration;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.List;

@Configuration
@EnableOAuth2Client
public class OAuth2ClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), resourceDetails());
    }

    private OAuth2ProtectedResourceDetails resourceDetails() {
        var details = new ClientCredentialsResourceDetails();
        details.setAccessTokenUri("http://localhost:6060/oauth/token");
        details.setClientId("docflow");
        details.setClientSecret("docflow");
        details.setGrantType("client_credentials");
        details.setScope(List.of("docflow"));
        return details;
    }

}
