package tech.becoming.frameworks.filestore.dropwizard.api.resources;

import tech.becoming.frameworks.filestore.dropwizard.neighbourhood.NeighbourhoodService;
import tech.becoming.frameworks.filestore.dropwizard.neighbourhood.NeighbourhoodServiceException;
import tech.becoming.frameworks.filestore.dropwizard.neighbourhood.Neighbour;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/network")
public class NeighboursResource {

    private NeighbourhoodService neighbourhood;

    public NeighboursResource(NeighbourhoodService neighbourhood) {this.neighbourhood = neighbourhood;}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Neighbour> getNeighbours() throws NeighbourhoodServiceException {
        return neighbourhood.list();
    }
}
