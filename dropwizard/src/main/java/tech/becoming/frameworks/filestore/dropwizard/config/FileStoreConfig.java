package tech.becoming.frameworks.filestore.dropwizard.config;

import java.nio.file.Path;

public interface FileStoreConfig {

    Path getHome();

    void init();
}
