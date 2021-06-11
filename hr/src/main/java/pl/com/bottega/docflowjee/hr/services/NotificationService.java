package pl.com.bottega.docflowjee.hr.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
@Slf4j
public class NotificationService {

    @TransactionalEventListener(classes = EmployeeCreatedEvent.class)
    @Async
    public void onEmployeeCreated(EmployeeCreatedEvent event) {
        log.info(String.format("Employee created %s", event));
    }

    @Async
    public CompletableFuture<String> getSomething() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getSomethingElse();
        return CompletableFuture.completedFuture("Something");
    }

    @Async
    public CompletableFuture<String> getSomethingElse() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture("Something else");
    }

    @Scheduled(fixedRate = 2000)
    public void doSthPeriodically() {
        log.info("I'm alive");
    }
}
