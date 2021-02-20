package tech.becoming.frameworks.filestore.dropwizard.api.resources;

import tech.becoming.frameworks.filestore.dropwizard.api.dto.FileUploadForm;
import tech.becoming.frameworks.filestore.dropwizard.api.template.Template;
import fr.miage.filestore.dropwizard.file.*;
import tech.becoming.frameworks.filestore.dropwizard.file.FileItem;
import tech.becoming.frameworks.filestore.dropwizard.file.FileService;
import tech.becoming.frameworks.filestore.dropwizard.file.exception.FileItemAlreadyExistsException;
import tech.becoming.frameworks.filestore.dropwizard.file.exception.FileItemNotEmptyException;
import tech.becoming.frameworks.filestore.dropwizard.file.exception.FileItemNotFoundException;
import tech.becoming.frameworks.filestore.dropwizard.file.exception.FileServiceException;
import io.dropwizard.hibernate.UnitOfWork;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;
import java.net.URI;

@Path("/api/files")
public class FilesResource {

    private FileService filestore;

    public FilesResource(FileService filestore) {this.filestore = filestore;}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response root(@Context UriInfo uriInfo) throws FileItemNotFoundException, FileServiceException {
        FileItem item = filestore.get("");
        URI root = uriInfo.getRequestUriBuilder().path(item.getId()).build();
        return Response.seeOther(root).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public FileItem get(@PathParam("id") String id) throws FileItemNotFoundException, FileServiceException {
        return filestore.get(id);
    }

    @GET
    @Path("{id}/content")
    @Template(name = "files")
    @Produces(value = MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response content(@PathParam("id") String id) throws FileItemNotFoundException, FileServiceException {
        FileItem item = filestore.get(id);
        if ( item.isFolder() ) {
            return Response.ok(filestore.list(item.getId())).build();
        } else {
            return Response.ok(filestore.getContent(item.getId()))
                    .header("Content-Type", item.getMimeType())
                    .header("Content-Length", item.getSize())
                    .header("Content-Disposition", "attachment; filename=" + item.getName()).build();
        }
    }

    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @UnitOfWork
    public Response add(@PathParam("id") String id, @Valid @FormDataParam("name") String name,
                        @Valid @FormDataParam("data") InputStream data, @Context UriInfo info) throws FileItemAlreadyExistsException, FileServiceException, FileItemNotFoundException {
        FileItem item;
        FileUploadForm form = new FileUploadForm(name, data);
        if ( form.getData() != null ) {
            item = filestore.add(id, form.getName(), form.getData());
        } else {
            item = filestore.add(id, form.getName());
        }
        URI createdUri = info.getBaseUriBuilder().path(FilesResource.class).path(item.getId()).build();
        return Response.created(createdUri).build();
    }

    @PUT
    @Path("{id}/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @UnitOfWork
    public Response update(@PathParam("id") String id, @PathParam("name") String name, FileUploadForm form)
            throws FileItemNotFoundException, FileServiceException, FileItemAlreadyExistsException, FileItemNotEmptyException {
        filestore.remove(id, name);
        filestore.add(id, name, form.getData());
        return Response.noContent().build();
    }


    @DELETE
    @Path("{id}/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response delete(@PathParam("id") String id, @PathParam("name") String name) throws FileItemNotFoundException, FileServiceException, FileItemNotEmptyException {
        filestore.remove(id, name);
        return Response.noContent().build();
    }

}
