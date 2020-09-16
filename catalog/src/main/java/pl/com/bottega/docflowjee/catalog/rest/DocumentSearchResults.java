package pl.com.bottega.docflowjee.catalog.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import pl.com.bottega.docflowjee.catalog.model.BasicDocumentInfo;

import java.util.List;

@Data
@Wither
@AllArgsConstructor
@NoArgsConstructor
public class DocumentSearchResults {

    private List<BasicDocumentInfo> documentDetails;

    private Long pagesCount, perPage, pageNumber, totalCount;

}
