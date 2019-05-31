package pl.com.bottega.docflowjee.catalog.repository;

import pl.com.bottega.docflowjee.catalog.model.DocumentStatus;

public class CatalogQuery {

    public DocumentStatus status;

    public String phrase;

    public String sortBy;

    public SortOrder sortOrder;

    public Integer page;

    public Integer perPage;
}
