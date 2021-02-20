package tech.becoming.frameworks.filestore.dropwizard.file;

import tech.becoming.frameworks.filestore.dropwizard.file.exception.FileItemAlreadyExistsException;
import tech.becoming.frameworks.filestore.dropwizard.file.exception.FileItemNotEmptyException;
import tech.becoming.frameworks.filestore.dropwizard.file.exception.FileItemNotFoundException;
import tech.becoming.frameworks.filestore.dropwizard.file.exception.FileServiceException;
import tech.becoming.frameworks.filestore.dropwizard.notification.NotificationService;
import tech.becoming.frameworks.filestore.dropwizard.notification.NotificationServiceException;
import tech.becoming.frameworks.filestore.dropwizard.store.BinaryStoreService;
import tech.becoming.frameworks.filestore.dropwizard.store.BinaryStoreServiceException;
import tech.becoming.frameworks.filestore.dropwizard.store.BinaryStreamNotFoundException;
import io.dropwizard.hibernate.UnitOfWork;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// @Interceptors({FileServiceMetricsBean.class})
public class FileServiceBean implements FileService {

    private static final Logger LOGGER = Logger.getLogger(FileService.class.getName());

    private BinaryStoreService store;

    private NotificationService notification;

    private FileDAO fileDAO;

    public FileServiceBean(FileDAO fileDAO, NotificationService notification, BinaryStoreService store) {
        this.store = store;
        this.notification = notification;
        this.fileDAO = fileDAO;}

    @UnitOfWork
    public void init() {
        boolean bootstrap = false;
        try {
            loadItem(FileItem.ROOT_ID);
        } catch (FileItemNotFoundException e ) {
            bootstrap = true;
        }
        synchronized (this) {
            if ( bootstrap ) {
                LOGGER.log(Level.INFO, "Root item does not exists, bootstraping");
                FileItem item = new FileItem();
                item.setName("root");
                item.setId(FileItem.ROOT_ID);
                item.setMimeType(FileItem.FOLDER_MIME_TYPE);
                fileDAO.persist(item);
                LOGGER.log(Level.INFO, "Bootstrap done, root item exists now.");
            }
        }
    }

    @Override
    // @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<FileItem> list(String id) throws FileServiceException, FileItemNotFoundException {
        LOGGER.log(Level.FINE, "List children of item: " + id);
        FileItem item = loadItem(id);
        if ( !item.isFolder() ) {
            throw new FileServiceException("Item is not a folder, unable to list children");
        }

        List<FileItem> items = fileDAO.listChildren(item.getId());
        LOGGER.log(Level.FINEST, "query returned " + items.size() + " results");
        items.sort(new FileItem.NameComparatorAsc());
        return items;
    }

    @Override
    // @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<FileItem> path(String id) throws FileServiceException, FileItemNotFoundException {
        LOGGER.log(Level.FINE, "Get path of item: " + id);
        List<FileItem> items = new ArrayList<>();
        while (!id.equals(FileItem.ROOT_ID)) {
            FileItem node = loadItem(id);
            items.add(node);
            id = node.getParent();
        }
        Collections.reverse(items);
        LOGGER.log(Level.FINE, "path: " + items.stream().map(FileItem::getName).collect(Collectors.joining(" > ")));
        return items;
    }

    @Override
    // @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public FileItem add(String id, String name) throws FileServiceException, FileItemAlreadyExistsException, FileItemNotFoundException {
        LOGGER.log(Level.FINE, "Adding folder with name: " + name + " to folder: " + id);
        try {
            FileItem parent = loadItem(id);
            if (!parent.isFolder()) {
                throw new FileServiceException("Item is not a folder, unable to add children");
            }
            LOGGER.log(Level.FINEST, "parent is folder with name: " + parent.getName());

            if (fileDAO.countChildrenForName(parent.getId(), name) > 0) {
                throw new FileItemAlreadyExistsException("An children with name: " + name + " already exists in item with id: " + id);
            }

            FileItem child = new FileItem();
            child.setId(UUID.randomUUID().toString());
            child.setParent(parent.getId());
            child.setName(name);
            child.setMimeType(FileItem.FOLDER_MIME_TYPE);
            fileDAO.persist(child);
            parent.setModificationDate(new Date());
            fileDAO.persist(parent);
            notification.throwEvent("folder.create", child.getId());
            return child;
        } catch (NotificationServiceException e) {
            throw new FileServiceException("Unable to add folder", e);
        }
    }

    @Override
    // @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public FileItem add(String id, String name, InputStream stream) throws FileServiceException, FileItemAlreadyExistsException, FileItemNotFoundException {
        LOGGER.log(Level.FINE, "Adding file with name: " + name + " to folder: " + id);
        try {
            FileItem parent = loadItem(id);
            if ( !parent.isFolder() ) {
                throw new FileServiceException("Item is not a folder, unable to add children");
            }
            LOGGER.log(Level.FINEST, "parent is folder with name: " + parent.getName());

            if ( fileDAO.countChildrenForName(parent.getId(), name) > 0 ) {
                throw new FileItemAlreadyExistsException("An children with name: " + name + " already exists in item with id: " + id);
            }

            String contentId = store.put(stream);
            stream.close();
            long size = store.size(contentId);
            String type = store.type(contentId, name);
            FileItem child = new FileItem();
            child.setId(UUID.randomUUID().toString());
            child.setParent(parent.getId());
            child.setName(name);
            child.setMimeType(type);
            child.setSize(size);
            child.setContentId(contentId);
            fileDAO.persist(child);
            parent.setModificationDate(new Date());
            fileDAO.persist(parent);
            notification.throwEvent("file.create", child.getId());
            return child;
        } catch (BinaryStoreServiceException | BinaryStreamNotFoundException | IOException | NotificationServiceException e ) {
            throw new FileServiceException("Unable to find binary stream for item", e);
        }
    }

    @Override
    // @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public FileItem get(String id) throws FileItemNotFoundException {
        LOGGER.log(Level.FINE, "Getting item with id: " + id);
        return loadItem(id);
    }

    @Override
    // @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public InputStream getContent(String id) throws FileServiceException, FileItemNotFoundException {
        LOGGER.log(Level.FINE, "Getting content for item with id: " + id);
        try {
            FileItem item = loadItem(id);
            if ( item.isFolder() ) {
                throw new FileServiceException("Item is a folder, unable to get content");
            }
            LOGGER.log(Level.FINEST, "item is file with name: " + item.getName());
            return store.get(item.getContentId());
        } catch (BinaryStoreServiceException | BinaryStreamNotFoundException e ) {
            throw new FileServiceException("Unable to find binary stream for item", e);
        }
    }

    @Override
    // @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void remove(String id, String name) throws FileServiceException, FileItemNotFoundException, FileItemNotEmptyException {
        LOGGER.log(Level.FINE, "Removing item with name: " + name + " from folder: " + id);
        try {
            FileItem parent = loadItem(id);
            if ( !parent.isFolder() ) {
                throw new FileServiceException("Item is not a folder, unable to remove children");
            }
            LOGGER.log(Level.FINEST, "parent is folder with name: " + parent.getName());

            List<FileItem> items = fileDAO.findChildrenForName(parent.getId(), name, 1);

            if (items.size() < 1) {
                throw new FileItemNotFoundException("No children found with name: " + name + " in parent item with id: " + id);
            }

            FileItem children = items.get(0);

            LOGGER.log(Level.FINEST, "children has name: " + children.getName());
            if ( children.isFolder() ) {
                LOGGER.log(Level.FINEST, "children is folder, checking if empty");
                Long folderSize = fileDAO.countChildren(children.getId());
                if ( folderSize > 0 ) {
                    throw new FileItemNotEmptyException("Children is a folder and is not empty, unable to remove, purge before");
                }
            }
            parent.setModificationDate(new Date());
            fileDAO.persist(parent);
            String eventType = "folder.remove";
            if ( !children.isFolder() ) {
                LOGGER.log(Level.FINEST, "children is file, deleting content also");
                store.delete(children.getContentId());
                eventType = "file.remove";
            }
            fileDAO.remove(children);
            notification.throwEvent(eventType, children.getId());
        } catch (BinaryStoreServiceException | BinaryStreamNotFoundException | NotificationServiceException e ) {
            throw new FileServiceException("Unable to find binary stream for item", e);
        }
    }

    //INTERNAL OPERATIONS

    private FileItem loadItem(String id) throws FileItemNotFoundException {
        if ( id == null || id.isEmpty() ) {
            id = FileItem.ROOT_ID;
        }
        FileItem item = fileDAO.find(id);
        if ( item == null ) {
            throw new FileItemNotFoundException("unable to find a item with id: " + id);
        }
        return item;
    }

}
