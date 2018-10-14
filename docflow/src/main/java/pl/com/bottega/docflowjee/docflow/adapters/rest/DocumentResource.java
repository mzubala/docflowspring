package pl.com.bottega.docflowjee.docflow.adapters.rest;

import pl.com.bottega.docflowjee.docflow.DocumentPreparation;
import pl.com.bottega.docflowjee.docflow.DocumentPublication;
import pl.com.bottega.docflowjee.docflow.DocumentVerification;
import pl.com.bottega.docflowjee.docflow.commands.ArchiveDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.CreateNewDocumentVersionCommand;
import pl.com.bottega.docflowjee.docflow.commands.PassToVerificationCommand;
import pl.com.bottega.docflowjee.docflow.commands.PublishDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.RejectDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.UpdateDocumentCommand;
import pl.com.bottega.docflowjee.docflow.commands.VerifyDocumentCommand;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/documents")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DocumentResource {

    @Inject
    private DocumentPreparation documentPreparation;
    @Inject
    private DocumentVerification documentVerification;
    @Inject
    private  DocumentPublication documentPublication;

    @PUT
    public Response create(CreateDocumentCommand command) {
        documentPreparation.create(command);
        return Response.ok().build();
    }

    @PUT
    @Path("/update")
    public Response update(UpdateDocumentCommand command) {
        documentPreparation.update(command);
        return Response.ok().build();
    }

    @PUT
    @Path("/verification/pass")
    public Response passToVerification(PassToVerificationCommand command) {
        documentVerification.passToVerification(command);
        return Response.ok().build();
    }

    @PUT
    @Path("/verification/positive")
    public Response verify(VerifyDocumentCommand command) {
        documentVerification.verify(command);
        return Response.ok().build();
    }

    @PUT
    @Path("/verification/rejection")
    public Response reject(RejectDocumentCommand command) {
        documentVerification.reject(command);
        return Response.ok().build();
    }

    @PUT
    @Path("/publication")
    public Response publish(PublishDocumentCommand command) {
        documentPublication.publish(command);
        return Response.ok().build();
    }

    @PUT
    @Path("/newVersion")
    public Response newVersion(CreateNewDocumentVersionCommand command) {
        documentPreparation.createNewVersion(command);
        return Response.ok().build();
    }

    @PUT
    @Path("/archive")
    public Response archive(ArchiveDocumentCommand command) {
        documentPreparation.archive(command);
        return Response.ok().build();
    }
}
