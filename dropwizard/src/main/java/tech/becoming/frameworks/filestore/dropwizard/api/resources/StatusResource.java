package tech.becoming.frameworks.filestore.dropwizard.api.resources;

import tech.becoming.frameworks.filestore.dropwizard.api.dto.FileStoreStatus;
import tech.becoming.frameworks.filestore.dropwizard.file.metrics.FileServiceMetrics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.management.ManagementFactory;

@Path("/api/status")
public class StatusResource {

    private FileServiceMetrics metrics;

    public StatusResource(FileServiceMetrics metrics) {this.metrics = metrics;}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public FileStoreStatus getStatusJson() {
        return internalGetStatus();
    }

    private FileStoreStatus internalGetStatus() {
        FileStoreStatus status = new FileStoreStatus();
        FileStoreStatus.Server serverStatus = new FileStoreStatus.Server();
        serverStatus.setNbCpus(Runtime.getRuntime().availableProcessors());
        serverStatus.setTotalMemory(Runtime.getRuntime().totalMemory());
        serverStatus.setAvailableMemory(Runtime.getRuntime().freeMemory());
        serverStatus.setMaxMemory(Runtime.getRuntime().maxMemory());
        serverStatus.setUptime(ManagementFactory.getRuntimeMXBean().getUptime());
        status.setServer(serverStatus);

        FileStoreStatus.Store storeStatus = new FileStoreStatus.Store();
        storeStatus.setDownloads(metrics.getNbDownloads());
        storeStatus.setUploads(metrics.getNbUploads());
        storeStatus.setLatestDownloads(metrics.getLatestDownloads());
        storeStatus.setLatestUploads(metrics.getLatestUploads());
        status.setStore(storeStatus);

        return status;
    }
}
