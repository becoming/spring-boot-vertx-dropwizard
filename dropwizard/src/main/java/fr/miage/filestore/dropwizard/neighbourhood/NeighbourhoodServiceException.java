package fr.miage.filestore.dropwizard.neighbourhood;

public class NeighbourhoodServiceException extends Exception {
    public NeighbourhoodServiceException(String message) {
        super(message);
    }

    public NeighbourhoodServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
