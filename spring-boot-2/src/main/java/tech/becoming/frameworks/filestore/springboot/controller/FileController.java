package tech.becoming.frameworks.filestore.springboot.controller;

import tech.becoming.frameworks.filestore.springboot.model.FileMetaData;
import tech.becoming.frameworks.filestore.springboot.repository.FileMetaDataRepository;
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
