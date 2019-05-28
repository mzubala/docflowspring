package pl.com.bottega.docflowjee.eventsourcing.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import cz.jirutka.spring.embedmongo.EmbeddedMongoBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import pl.com.bottega.eventsourcing.EventPublisher;

import java.io.IOException;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {

    @Bean(destroyMethod="close")
    public Mongo mongo() throws IOException {
        return new EmbeddedMongoBuilder()
            .version("2.6.0")
            .bindIp("127.0.0.1")
            .port(12345)
            .build();
    }

    @Bean
    public MongoTemplate mongoTemplate(Mongo mongo) {
        return new MongoTemplate(new MongoClient(mongo.getAddress()), "eventstoretest");
    }

    @Bean
    public EventPublisher eventPublisher() {
        return mock(EventPublisher.class);
    }

    @Bean
    public MongoEventStore mongoEventStore(EventPublisher eventPublisher) {
        return new MongoEventStore(eventPublisher);
    }

}