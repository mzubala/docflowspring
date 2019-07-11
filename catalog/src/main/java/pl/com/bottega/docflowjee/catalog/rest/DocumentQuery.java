package pl.com.bottega.docflowjee.catalog.rest;

import lombok.Data;
import pl.com.bottega.docflowjee.catalog.model.DocumentStatus;

@Data
public class DocumentQuery {

    private DocumentStatus status;
    private String phrase;

}
