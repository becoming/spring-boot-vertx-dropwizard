package tech.becoming.frameworks.filestore.dropwizard.neighbourhood;

import tech.becoming.frameworks.filestore.dropwizard.file.FileItem;

import java.util.List;

public interface NeighbourhoodService {

    List<Neighbour> list() throws NeighbourhoodServiceException;

    List<FileItem> browse(String id) throws NeighbourhoodServiceException;

}
