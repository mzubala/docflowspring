package pl.com.bottega.docflowjee.confirmations.adapters.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.web.reactive.function.client.WebClient;
import pl.com.bottega.docflowjee.confirmations.adapters.client.HrClient;
import pl.com.bottega.docflowjee.confirmations.adapters.db.MongoConfirmationRepository;
import pl.com.bottega.docflowjee.confirmations.adapters.db.SpringDataConfirmationRepository;
import pl.com.bottega.docflowjee.confirmations.domain.ConfirmationRepository;
import pl.com.bottega.docflowjee.confirmations.domain.ConfirmationService;
import pl.com.bottega.docflowjee.confirmations.domain.HrFacade;

@Configuration
public class ConfirmationsConfig {

    @Bean
    public ConfirmationRepository confirmationRepository(MongoConfirmationRepository mongoConfirmationRepository) {
        return new SpringDataConfirmationRepository(mongoConfirmationRepository);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public HrFacade hrFacade(WebClient webClient) {
        return new HrClient(webClient);
    }

    @Bean
    public ConfirmationService confirmationService(HrFacade hrFacade, ConfirmationRepository repository) {
        return new ConfirmationService(hrFacade, repository);
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        var mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        converter.setObjectMapper(mapper);
        return converter;
    }

}
