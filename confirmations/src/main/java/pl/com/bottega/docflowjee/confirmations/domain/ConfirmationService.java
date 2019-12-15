package pl.com.bottega.docflowjee.confirmations.domain;

import lombok.AllArgsConstructor;
import pl.com.bottega.docflowjee.docflow.events.DocumentPublishedEvent;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class ConfirmationService {

    private final HrFacade hrFacade;
    private final ConfirmationRepository confirmationRepository;

    public Mono<Void> createConfirmations(DocumentPublishedEvent event) {
        return hrFacade.getEmployeesInDepartment(event.getDepartmentIds()).map(
            employeesInDepartments -> createConfirmations(event.getAggregateId(), employeesInDepartments)
        ).flatMap(this::saveConfirmations);
    }

    private List<Confirmation> createConfirmations(UUID documentId, EmployeesInDepartments employeesInDepartments) {
        return employeesInDepartments.getEmployeeIds().stream()
            .map(employeeId -> new Confirmation(documentId, employeeId))
            .collect(toList());
    }

    private Mono<Void> saveConfirmations(List<Confirmation> confirmations) {
        return confirmationRepository.save(confirmations);
    }

    public Mono<Void> confirm(UUID documentId, Long employeeId) {
        return confirmationRepository
            .findFor(documentId, employeeId)
            .map(Confirmation::confirm)
            .flatMap(confirmation -> confirmationRepository.save(confirmation));
    }
}
