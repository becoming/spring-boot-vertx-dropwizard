package tech.becoming.frameworks.filestore.dropwizard.api.dto;

import tech.becoming.frameworks.filestore.dropwizard.api.validation.Filename;
import org.glassfish.jersey.media.multipart.FormDataParam;
// import org.jboss.resteasy.annotations.providers.multipart.PartType;

import java.io.InputStream;

public class FileUploadForm {

    @FormDataParam("name")
    // @PartType(MediaType.TEXT_PLAIN)
    @Filename
    private String name;

    @FormDataParam("form-data")
    // @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream data = null;

    public FileUploadForm() {
    }

    public FileUploadForm(String name, InputStream data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getData() {
        return data;
    }

    public void setData(InputStream data) {
        this.data = data;
    }
}
