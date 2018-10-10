package pl.com.bottega.docflowjee.hr.rest;

import pl.com.bottega.docflowjee.hr.dao.EmployeeDao;
import pl.com.bottega.docflowjee.hr.model.Employee;
import pl.com.bottega.docflowjee.hr.service.EmployeesService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/employees")
public class EmployeesResource {

    @Inject
    private EmployeesService employeesService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id) {
        return Response.ok(employeesService.find(id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(Employee employee) {
        Employee created = employeesService.save(employee);
        return Response.ok(created).build();
    }

}
