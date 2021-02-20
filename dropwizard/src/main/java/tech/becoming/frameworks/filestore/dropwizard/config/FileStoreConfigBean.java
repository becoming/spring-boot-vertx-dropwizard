package tech.becoming.frameworks.filestore.dropwizard.config;

import javax.inject.Named;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("config")
public class FileStoreConfigBean implements FileStoreConfig {

    private static final Logger LOGGER = Logger.getLogger(FileStoreConfigBean.class.getName());

    private Path home;

    public void init() {
        LOGGER.log(Level.INFO, "Initialising config");
        if ( System.getenv("FILESTORE_HOME") != null ) {
            home = Paths.get(System.getenv("FILESTORE_HOME"));
        } else if ( System.getProperty("filestoreHome") != null ) {
            home = Paths.get(System.getProperty("filestoreHome"));
        } else {
            home = Paths.get(System.getProperty("user.home"), ".filestore");
        }
        LOGGER.log(Level.INFO, "Filestore home set to : " + home.toString());
    }

    @Override
    public Path getHome() {
        return home;
    }
}
