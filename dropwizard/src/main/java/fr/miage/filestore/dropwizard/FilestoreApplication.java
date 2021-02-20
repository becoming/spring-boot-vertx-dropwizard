package fr.miage.filestore.dropwizard;

import fr.miage.filestore.dropwizard.api.FilestoreHealthCheck;
import fr.miage.filestore.dropwizard.config.FileStoreConfig;
import fr.miage.filestore.dropwizard.config.FileStoreConfigBean;
import fr.miage.filestore.dropwizard.file.FileDAO;
import fr.miage.filestore.dropwizard.notification.NotificationService;
import fr.miage.filestore.dropwizard.notification.NotificationServiceBean;
import fr.miage.filestore.dropwizard.person.PersonDAO;
import fr.miage.filestore.dropwizard.api.resources.FilesResource;
import fr.miage.filestore.dropwizard.api.resources.NeighboursResource;
import fr.miage.filestore.dropwizard.api.resources.StatusResource;
import fr.miage.filestore.dropwizard.config.AppConfig;
import fr.miage.filestore.dropwizard.file.FileService;
import fr.miage.filestore.dropwizard.file.FileServiceBean;
import fr.miage.filestore.dropwizard.file.metrics.FileServiceMetrics;
import fr.miage.filestore.dropwizard.file.metrics.FileServiceMetricsBean;
import fr.miage.filestore.dropwizard.api.resources.PersonResource;
import fr.miage.filestore.dropwizard.neighbourhood.NeighbourhoodService;
import fr.miage.filestore.dropwizard.neighbourhood.NeighbourhoodServiceBean;
import fr.miage.filestore.dropwizard.store.BinaryStoreService;
import fr.miage.filestore.dropwizard.store.BinaryStoreServiceBean;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class FilestoreApplication extends Application<AppConfig> {

    public static void main(String[] args) throws Exception {
        new FilestoreApplication().run(args);
    }

    private HibernateBundle<AppConfig> hibernateBundle;

    @Override
    public String getName() {
        return "filestore";
    }

    @Override
    public void initialize(Bootstrap<AppConfig> bootstrap) {
        hibernateBundle = new HibernateCustomBundle();

        bootstrap.addBundle(new MigrationsBundle<AppConfig>() {
            @Override
            public DataSourceFactory getDataSourceFactory(AppConfig configuration) {
                return configuration.getDataSourceFactory();
            }
        });

        bootstrap.addBundle(new MultiPartBundle());
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(AppConfig configuration,
                    Environment environment) {
        final PersonDAO personDAO = new PersonDAO(hibernateBundle.getSessionFactory());
        final FileDAO fileDAO = new FileDAO(hibernateBundle.getSessionFactory());

        final FileStoreConfig fileStoreConfig = new FileStoreConfigBean();
        fileStoreConfig.init();

        final NotificationService notificationService = new NotificationServiceBean();
        final BinaryStoreService binaryStoreService = new BinaryStoreServiceBean(fileStoreConfig);
        binaryStoreService.init();

        FileService fileService = new UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(FileServiceBean.class,
                        new Class[]{FileDAO.class, NotificationService.class, BinaryStoreService.class},
                        new Object[]{fileDAO, notificationService, binaryStoreService});
        fileService.init();

        FileServiceMetrics fileServiceMetrics = new FileServiceMetricsBean();
        NeighbourhoodService neighbourhoodService = new NeighbourhoodServiceBean();

        environment.jersey().register(new PersonResource(personDAO));
        environment.jersey().register(new FilesResource(fileService));
        environment.jersey().register(new StatusResource(fileServiceMetrics));
        environment.jersey().register(new NeighboursResource(neighbourhoodService));

        environment.healthChecks().register("health", new FilestoreHealthCheck(configuration.getTemplate()));
    }
}