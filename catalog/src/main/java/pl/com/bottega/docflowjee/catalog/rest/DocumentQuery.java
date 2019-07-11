package pl.com.bottega.docflowjee.catalog.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import pl.com.bottega.docflowjee.catalog.model.DocumentStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Wither
public class DocumentQuery {

    private DocumentStatus status;
    private String phrase;
    private Long page = 1L, perPage = 25L;

}
