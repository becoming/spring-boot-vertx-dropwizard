package fr.miage.filestore.dropwizard.api.resources;

import com.codahale.metrics.annotation.Timed;
import fr.miage.filestore.dropwizard.person.Person;
import fr.miage.filestore.dropwizard.person.PersonDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    private PersonDAO dao;

    public PersonResource(PersonDAO dao) {this.dao = dao;}

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Person find(@PathParam("id") LongParam id) {
        return dao.findById(id.get()).orElseThrow(NotFoundException::new);
    }

    @POST
    @Timed
    @UnitOfWork
    public String create(Person person, @Context HttpServletRequest httpServletRequest) {
        return httpServletRequest.getPathInfo() + "/" + dao.create(person).getId();
    }
}
