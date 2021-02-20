package fr.miage.filestore.dropwizard.file;

import fr.miage.filestore.dropwizard.file.exception.FileItemAlreadyExistsException;
import fr.miage.filestore.dropwizard.file.exception.FileItemNotEmptyException;
import fr.miage.filestore.dropwizard.file.exception.FileItemNotFoundException;
import fr.miage.filestore.dropwizard.file.exception.FileServiceException;

import java.io.InputStream;
import java.util.List;

public interface FileService {

    void init();

    List<FileItem> list(String id) throws FileServiceException, FileItemNotFoundException;

    List<FileItem> path(String id) throws FileServiceException, FileItemNotFoundException;

    FileItem add(String id, String name) throws FileServiceException, FileItemAlreadyExistsException, FileItemNotFoundException;

    FileItem add(String id, String name, InputStream stream) throws FileServiceException, FileItemAlreadyExistsException, FileItemNotFoundException;

    FileItem get(String id) throws FileServiceException, FileItemNotFoundException;

    InputStream getContent(String id) throws FileServiceException, FileItemNotFoundException;

    void remove(String id, String name) throws FileServiceException, FileItemNotFoundException, FileItemNotEmptyException;

}
