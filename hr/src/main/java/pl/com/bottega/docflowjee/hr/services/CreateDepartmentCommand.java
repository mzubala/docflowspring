package pl.com.bottega.docflowjee.hr.services;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class CreateDepartmentCommand {

	private final String name;

	public CreateDepartmentCommand(String name) {
		checkNotNull(name);
		this.name = name;
	}
}
