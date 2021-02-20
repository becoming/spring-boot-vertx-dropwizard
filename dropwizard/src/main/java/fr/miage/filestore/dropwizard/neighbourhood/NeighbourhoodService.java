package fr.miage.filestore.dropwizard.neighbourhood;

import fr.miage.filestore.dropwizard.file.FileItem;

import java.util.List;

public interface NeighbourhoodService {

    List<Neighbour> list() throws NeighbourhoodServiceException;

    List<FileItem> browse(String id) throws NeighbourhoodServiceException;

}
