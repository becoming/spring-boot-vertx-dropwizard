package tech.becoming.frameworks.filestore.dropwizard.api.mapper;

import tech.becoming.frameworks.filestore.dropwizard.file.exception.FileItemNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FileNotFoundExceptionMapper implements ExceptionMapper<FileItemNotFoundException> {

    @Override
    public Response toResponse(FileItemNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
    }
}