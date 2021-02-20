package tech.becoming.frameworks.filestore.dropwizard.store;

import tech.becoming.frameworks.filestore.dropwizard.config.FileStoreConfig;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BinaryStoreServiceBean implements BinaryStoreService {

    private static final Logger LOGGER = Logger.getLogger(BinaryStoreService.class.getName());
    public static final String BINARY_DATA_HOME = "binarydata";

    private FileStoreConfig config;

    private Path base;
    private TikaConfig tika;

    public BinaryStoreServiceBean(FileStoreConfig config) {
        this.config = config;
    }

    public void init() {
        this.base = Paths.get(config.getHome().toString(), BINARY_DATA_HOME);
        LOGGER.log(Level.INFO, "Initializing service with base folder: " + base);
        try {
            Files.createDirectories(base);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "unable to initialize binary store", e);
        }
        try {
            tika =  new TikaConfig();
        } catch (TikaException | IOException e) {
            LOGGER.log(Level.SEVERE, "unable to initialize tika", e);
        }
    }

    @Override
    public boolean exists(String key) {
        LOGGER.log(Level.FINE, "Checking if key exists: " + key);
        Path file = Paths.get(base.toString(), key);
        return Files.exists(file);
    }

    @Override
    public long size(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        LOGGER.log(Level.FINE, "Getting size fo key: " + key);
        Path file = Paths.get(base.toString(), key);
        if ( !Files.exists(file) ) {
            throw new BinaryStreamNotFoundException("file not found in storage");
        }
        try {
            return Files.size(file);
        } catch (IOException e) {
            throw new BinaryStoreServiceException("unexpected error during stream size", e);
        }
    }

    @Override
    public String type(String key, String name) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        LOGGER.log(Level.FINE, "Getting mimetype for key: " + key);
        Path file = Paths.get(base.toString(), key);
        if ( !Files.exists(file) ) {
            throw new BinaryStreamNotFoundException("file not found in storage");
        }
        try {
            Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, name);
            InputStream is =TikaInputStream.get(file);
            String mimetype = tika.getDetector().detect(is, metadata).toString();
            is.close();
            return mimetype;
        } catch (IOException e) {
            throw new BinaryStoreServiceException("unexpected error during stream size", e);
        }
    }

    @Override
    public String put(InputStream is) throws BinaryStoreServiceException {
        String key = UUID.randomUUID().toString();
        Path file = Paths.get(base.toString(), key);
        if ( Files.exists(file) ) {
            throw new BinaryStoreServiceException("unable to create file, key already exists");
        }
        try {
            Files.copy(is, file, StandardCopyOption.REPLACE_EXISTING);
            is.close();
        } catch (IOException e) {
            throw new BinaryStoreServiceException("unexpected error during stream copy", e);
        }
        LOGGER.log(Level.FINE, "New content stored with key: " + key);
        return key;
    }

    @Override
    public InputStream get(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        LOGGER.log(Level.FINE, "Getting content with key: " + key);
        Path file = Paths.get(base.toString(), key);
        if ( !Files.exists(file) ) {
            throw new BinaryStreamNotFoundException("file not found in storage");
        }
        try {
            return Files.newInputStream(file, StandardOpenOption.READ);
        } catch (IOException e) {
            throw new BinaryStoreServiceException("unexpected error while opening stream", e);
        }
    }

    @Override
    public void delete(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        LOGGER.log(Level.FINE, "Deleting content with key: " + key);
        Path file = Paths.get(base.toString(), key);
        if ( !Files.exists(file) ) {
            throw new BinaryStreamNotFoundException("file not found in storage");
        }
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new BinaryStoreServiceException("unexpected error while deleting stream", e);
        }
    }

}
