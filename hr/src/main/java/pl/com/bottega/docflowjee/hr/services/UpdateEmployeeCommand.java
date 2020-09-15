package pl.com.bottega.docflowjee.hr.services;

import lombok.Getter;
import pl.com.bottega.docflowjee.hr.model.Position;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Getter
public class UpdateEmployeeCommand {
	private final Long id;
	private final String firstName;
	private final String lastName;
	private final List<Long> departmentIds;
	private final Long supervisorId;
	private final Position position;

	public UpdateEmployeeCommand(Long id, String firstName, String lastName, List<Long> departmentIds, Long supervisorId, Position position) {
		checkNotNull(id);
		checkState(isNotBlank(firstName));
		checkState(isNotBlank(lastName));
		checkNotNull(departmentIds);
		checkState(departmentIds.size() > 0);
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.departmentIds = departmentIds;
		this.supervisorId = supervisorId;
		this.position = position;
	}
}
