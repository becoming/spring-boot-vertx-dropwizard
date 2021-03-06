package tech.becoming.frameworks.filestore.dropwizard.api.mapper;

import tech.becoming.frameworks.filestore.dropwizard.file.exception.FileItemAlreadyExistsException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FileAlreadyExistsExceptionMapper implements ExceptionMapper<FileItemAlreadyExistsException> {

    @Override
    public Response toResponse(FileItemAlreadyExistsException e) {
        return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
    }
}