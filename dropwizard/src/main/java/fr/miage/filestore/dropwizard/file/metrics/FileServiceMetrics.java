package fr.miage.filestore.dropwizard.file.metrics;

public interface FileServiceMetrics {

    int getNbUploads();

    int getNbDownloads();

    int getLatestUploads();

    int getLatestDownloads();
}
