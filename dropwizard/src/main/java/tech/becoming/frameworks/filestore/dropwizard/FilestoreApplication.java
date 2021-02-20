package tech.becoming.frameworks.filestore.dropwizard;

import tech.becoming.frameworks.filestore.dropwizard.api.FilestoreHealthCheck;
import tech.becoming.frameworks.filestore.dropwizard.config.FileStoreConfig;
import tech.becoming.frameworks.filestore.dropwizard.config.FileStoreConfigBean;
import tech.becoming.frameworks.filestore.dropwizard.file.FileDAO;
import tech.becoming.frameworks.filestore.dropwizard.notification.NotificationService;
import tech.becoming.frameworks.filestore.dropwizard.notification.NotificationServiceBean;
import tech.becoming.frameworks.filestore.dropwizard.person.PersonDAO;
import tech.becoming.frameworks.filestore.dropwizard.api.resources.FilesResource;
import tech.becoming.frameworks.filestore.dropwizard.api.resources.NeighboursResource;
import tech.becoming.frameworks.filestore.dropwizard.api.resources.StatusResource;
import tech.becoming.frameworks.filestore.dropwizard.config.AppConfig;
import tech.becoming.frameworks.filestore.dropwizard.file.FileService;
import tech.becoming.frameworks.filestore.dropwizard.file.FileServiceBean;
import tech.becoming.frameworks.filestore.dropwizard.file.metrics.FileServiceMetrics;
import tech.becoming.frameworks.filestore.dropwizard.file.metrics.FileServiceMetricsBean;
import tech.becoming.frameworks.filestore.dropwizard.api.resources.PersonResource;
import tech.becoming.frameworks.filestore.dropwizard.neighbourhood.NeighbourhoodService;
import tech.becoming.frameworks.filestore.dropwizard.neighbourhood.NeighbourhoodServiceBean;
import tech.becoming.frameworks.filestore.dropwizard.store.BinaryStoreService;
import tech.becoming.frameworks.filestore.dropwizard.store.BinaryStoreServiceBean;
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