package pl.com.bottega.docflowjee.confirmations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class ConfirmationsApp {

    public static void main(String[] args) {
        SpringApplication.run(ConfirmationsApp.class, args);
    }

}
