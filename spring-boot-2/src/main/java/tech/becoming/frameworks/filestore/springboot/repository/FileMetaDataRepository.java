package tech.becoming.frameworks.filestore.springboot.repository;

import tech.becoming.frameworks.filestore.springboot.model.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetaDataRepository extends JpaRepository<FileMetaData, Long> {

}
