package fr.miage.filestore.dropwizard;

import fr.miage.filestore.dropwizard.config.AppConfig;
import fr.miage.filestore.dropwizard.file.FileItem;
import fr.miage.filestore.dropwizard.person.Person;
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
