package fr.miage.filestore.springboot.repository;

import fr.miage.filestore.springboot.model.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetaDataRepository extends JpaRepository<FileMetaData, Long> {

}
