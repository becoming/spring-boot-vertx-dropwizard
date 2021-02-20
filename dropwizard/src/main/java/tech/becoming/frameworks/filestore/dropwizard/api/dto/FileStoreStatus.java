package tech.becoming.frameworks.filestore.dropwizard.api.dto;

public class FileStoreStatus {

    private Server server;
    private Store store;

    public FileStoreStatus() {
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public static class Store {

        private long downloads;
        private long uploads;
        private long latestDownloads;
        private long latestUploads;


        public Store() {
        }

        public long getDownloads() {
            return downloads;
        }

        public void setDownloads(long downloads) {
            this.downloads = downloads;
        }

        public long getUploads() {
            return uploads;
        }

        public void setUploads(long uploads) {
            this.uploads = uploads;
        }

        public long getLatestDownloads() {
            return latestDownloads;
        }

        public void setLatestDownloads(long latestDownloads) {
            this.latestDownloads = latestDownloads;
        }

        public long getLatestUploads() {
            return latestUploads;
        }

        public void setLatestUploads(long latestUploads) {
            this.latestUploads = latestUploads;
        }
    }

    public static class Server {

        private long uptime;
        private long totalMemory;
        private long availableMemory;
        private long maxMemory;
        private int nbCpus;

        public Server() {
        }

        public long getUptime() {
            return uptime;
        }

        public void setUptime(long uptime) {
            this.uptime = uptime;
        }

        public long getTotalMemory() {
            return totalMemory;
        }

        public void setTotalMemory(long totalMemory) {
            this.totalMemory = totalMemory;
        }

        public long getAvailableMemory() {
            return availableMemory;
        }

        public void setAvailableMemory(long availableMemory) {
            this.availableMemory = availableMemory;
        }

        public long getMaxMemory() {
            return maxMemory;
        }

        public void setMaxMemory(long maxMemory) {
            this.maxMemory = maxMemory;
        }

        public int getNbCpus() {
            return nbCpus;
        }

        public void setNbCpus(int nbCpus) {
            this.nbCpus = nbCpus;
        }
    }
}
