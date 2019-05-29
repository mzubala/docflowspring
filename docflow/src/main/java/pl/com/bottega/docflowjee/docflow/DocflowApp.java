package pl.com.bottega.docflowjee.docflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "pl.com.bottega.docflowjee")
@EnableMongoRepositories(basePackages = "pl.com.bottega.docflowjee")
public class DocflowApp {

    public static void main(String[] args) {
        SpringApplication.run(DocflowApp.class, args);
    }

}
