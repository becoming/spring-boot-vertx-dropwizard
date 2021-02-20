package fr.miage.filestore.springboot.controller;

import fr.miage.filestore.springboot.model.FileMetaData;
import fr.miage.filestore.springboot.repository.FileMetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FileController {

    private FileMetaDataRepository repository;

    public FileController(FileMetaDataRepository repository) {
        this.repository = repository;
    }

    @GetMapping("{id}")
    public FileMetaData hello(@PathVariable String id) {
        final FileMetaData metaData = new FileMetaData();
        metaData.setName(id);
        return metaData;
    }

    @PostMapping
    public Long upload(@RequestBody FileMetaData fileMetaData) {
        final FileMetaData savedMeta = repository.save(fileMetaData);
        return savedMeta.getId();
    }

}
