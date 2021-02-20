package fr.miage.filestore.dropwizard.file.metrics;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FileServiceMetricsWorker implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(FileServiceMetricsWorker.class.getName());

    private FileServiceMetrics metrics;

    public FileServiceMetricsWorker(FileServiceMetrics metrics) {
        LOGGER.log(Level.INFO, "Instanciate FileServiceMetricsWorker");
        this.metrics = metrics;
    }

    @Override
    public void run() {
        LOGGER.log(Level.FINE, "FileServiceMetricsWorker is running");
        LOGGER.log(Level.FINE, "Latest downloads: " +  metrics.getLatestDownloads());
        LOGGER.log(Level.FINE, "FileServiceMetricsWorker has worked");
    }
}