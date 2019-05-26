package pl.com.bottega.docflowjee.hr.controller.error;

import lombok.Getter;

@Getter
public class NoSuchEmployeeException extends RuntimeException {
    private Long id;

    public NoSuchEmployeeException(Long id) {
        super(String.format("Employee with id=%s not found", id));
        this.id = id;
    }
}
