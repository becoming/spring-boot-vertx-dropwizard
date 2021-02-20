package tech.becoming.frameworks.filestore.dropwizard;

import tech.becoming.frameworks.filestore.dropwizard.config.AppConfig;
import tech.becoming.frameworks.filestore.dropwizard.file.FileItem;
import tech.becoming.frameworks.filestore.dropwizard.person.Person;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;

public class HibernateCustomBundle extends HibernateBundle<AppConfig> {

    HibernateCustomBundle() {
        super(Person.class, FileItem.class);
    }

    @Override
    public DataSourceFactory getDataSourceFactory(AppConfig configuration) {
        return configuration.getDataSourceFactory();
    }
}
