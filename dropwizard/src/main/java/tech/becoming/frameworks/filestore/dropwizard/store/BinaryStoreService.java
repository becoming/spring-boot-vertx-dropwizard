package tech.becoming.frameworks.filestore.dropwizard.store;

import java.io.InputStream;

public interface BinaryStoreService {

    boolean exists(String key) throws BinaryStoreServiceException;

    String type(String key, String name) throws BinaryStoreServiceException, BinaryStreamNotFoundException;

    String put(InputStream is) throws BinaryStoreServiceException;

    long size(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException;

    InputStream get(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException;

    void delete(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException;

    void init();
}
