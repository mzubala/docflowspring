package pl.com.bottega.docflowjee.confirmations.adapters.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import pl.com.bottega.docflowjee.confirmations.domain.EmployeesInDepartments;
import pl.com.bottega.docflowjee.confirmations.domain.HrFacade;
import reactor.core.publisher.Mono;

import java.util.Set;

public class HrClient implements HrFacade {

    private final WebClient webClient;

    @Value("${service.hr.url}")
    private String hrUrl;

    public HrClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<EmployeesInDepartments> getEmployeesInDepartment(Set<Long> departmentIds) {
        return webClient.get().uri(hrUrl)
            .accept(MediaType.APPLICATION_JSON).retrieve()
            .bodyToMono(EmployeesInDepartments.class);
    }
}
