package pl.com.bottega.docflowjee.eventsourcing.mongo;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import pl.com.bottega.eventsourcing.EventPublisher;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static org.mockito.Mockito.mock;

@Configuration
@DataMongoTest
@Component
public class TestConfig {

    @Bean
    public EventPublisher eventPublisher() {
        return mock(EventPublisher.class);
    }

    @Bean
    public MongoEventStore mongoEventStore(EventPublisher eventPublisher) {
        return new MongoEventStore(eventPublisher);
    }

    @PostConstruct
    public void startMongo() throws IOException {
        MongodStarter starter = MongodStarter.getDefaultInstance();

        String bindIp = "localhost";
        int port = 12345;
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(bindIp, port, Network.localhostIsIPv6()))
                .build();

        MongodExecutable mongodExecutable = null;
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
    }

}