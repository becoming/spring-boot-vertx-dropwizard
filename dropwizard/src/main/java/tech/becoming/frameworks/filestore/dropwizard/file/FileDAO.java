package tech.becoming.frameworks.filestore.dropwizard.file;

import tech.becoming.frameworks.filestore.dropwizard.file.exception.FileItemAlreadyExistsException;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import java.util.List;

public class FileDAO extends AbstractDAO<FileItem> {
    public FileDAO(SessionFactory factory) {
        super(factory);
    }

    public FileItem persist(FileItem fileItem) {
        return super.persist(fileItem);
    }

    void remove(FileItem child) {
        currentSession().delete(child);
    }

    FileItem find(String id) {
        return currentSession().find(FileItem.class, id);
    }

    @SuppressWarnings("unchecked")
    List<FileItem> listChildren(String parent) {
        return list((Query<FileItem>) namedQuery("FileItem.listChildren")
                .setParameter("parent", parent));
    }

    @SuppressWarnings("unchecked")
    Long countChildrenForName(String parentId, String name) throws FileItemAlreadyExistsException {
        TypedQuery<Long> query = currentSession().createNamedQuery("FileItem.countChildrenForName", Long.class)
                .setParameter("parent", parentId)
                .setParameter("name", name);

        return query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    List<FileItem> findChildrenForName(String parentId, String name, int maxResult) {
        return list((Query<FileItem>) namedQuery("FileItem.findChildrenForName")
                .setParameter("parent", parentId)
                .setParameter("name", name)
                .setMaxResults(maxResult));
    }

    @SuppressWarnings("unchecked")
    Long countChildren(String parentId) {
        return currentSession().createNamedQuery("FileItem.countChildren", Long.class).setParameter("parent", parentId).getSingleResult();
    }

}
