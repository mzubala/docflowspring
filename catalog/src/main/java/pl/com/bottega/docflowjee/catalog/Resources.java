package pl.com.bottega.docflowjee.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.inject.Produces;

public class Resources {

    @Produces
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

}
