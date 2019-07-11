package pl.com.bottega.docflowjee.confirmations.adapters.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.docflowjee.confirmations.domain.ConfirmationService;
import reactor.core.publisher.Mono;

@RestController
public class ConfirmationsResource {

    @Autowired
    private ConfirmationService confirmationService;

    @PutMapping("/confirmations")
    public Mono<Void> confirm(@RequestBody ConfirmationRequest request) {
        return Mono.empty();
    }

}
