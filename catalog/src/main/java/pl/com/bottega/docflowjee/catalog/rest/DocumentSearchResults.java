package pl.com.bottega.docflowjee.catalog.rest;

import lombok.Data;
import pl.com.bottega.docflowjee.catalog.model.BasicDocumentInfo;

import java.util.List;

@Data
public class DocumentSearchResults {

    private List<BasicDocumentInfo> documentDetails;

    private Long pagesCount, perPage, pageNumber, totalCount;

}
