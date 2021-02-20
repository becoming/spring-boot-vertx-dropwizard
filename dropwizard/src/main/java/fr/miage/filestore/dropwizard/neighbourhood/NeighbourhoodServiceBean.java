package fr.miage.filestore.dropwizard.neighbourhood;

import fr.miage.filestore.dropwizard.file.FileItem;
// import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;
// import org.wildfly.swarm.topology.AdvertisementHandle;
// import org.wildfly.swarm.topology.Topology;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

public class NeighbourhoodServiceBean implements NeighbourhoodService{

    private static final Logger LOGGER = Logger.getLogger(NeighbourhoodService.class.getName());

    @Inject
    // @ConfigurationValue("filestore.instance.id")
    private String id;

    @Inject
    // @ConfigurationValue("filestore.instance.name")
    private String name;

    // private AdvertisementHandle handle;

    // @PostConstruct
    // public void init() {
    //     try {
    //         LOGGER.log(Level.SEVERE, "Advertising topology...");
    //         handle = Topology.lookup().advertise("filestore-host", "id." + id, "name." + name);
    //     } catch (Exception e) {
    //         LOGGER.log(Level.SEVERE, "Unable to advertise topology", e);
    //     }
    // }

    // @PreDestroy
    // public void teardown() {
    //     if (handle != null) {
    //         handle.unadvertise();
    //     }
    // }

    @Override
    public List<Neighbour> list() throws NeighbourhoodServiceException {
    //     try {
    //         LOGGER.log(Level.INFO, "Listing neighbourhood...");
    //         Map topology = Topology.lookup().asMap();
    //         List<Neighbour> neighbours = Collections.emptyList();
    //         if (topology.containsKey("filestore-host")) {
    //             neighbours = ((List<Topology.Entry>)topology.get("filestore-host")).stream().map(entry -> {
    //                 Optional<String> id = entry.getTags().stream().filter(tag -> tag.startsWith("id.")).map(tag -> tag.substring(3)).findFirst();
    //                 Optional<String> name = entry.getTags().stream().filter(tag -> tag.startsWith("name.")).map(tag -> tag.substring(5)).findFirst();
    //                 return new Neighbour(id.get(), name.get(), entry.getAddress() + ":" + entry.getPort());
    //             }).collect(Collectors.toList());
    //         }
    //         LOGGER.log(Level.FINE, neighbours.stream().map(Neighbour::toString).collect(Collectors.joining(",")));
    //         return neighbours;
    //     } catch (Exception e) {
    //         throw new NeighbourhoodServiceException("Unable to list neighbourhood", e);
    //     }
        return null;
    }

    @Override
    public List<FileItem> browse(String id) throws NeighbourhoodServiceException {
    //     LOGGER.log(Level.INFO, "Browsing neighbour with id: " + id);
    //     throw new NeighbourhoodServiceException("NOT IMPLEMENTED");
        return null;
    }
}
