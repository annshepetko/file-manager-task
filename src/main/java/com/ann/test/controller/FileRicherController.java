package com.ann.test.controller;

import com.ann.test.facade.EnrichServiceCacheFacade;
import com.ann.test.interfaces.EnrichFileFacade;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileRicherController {


    private final  EnrichFileFacade enrichFileService;

    public FileRicherController(@Qualifier("enrichServiceCacheFacade") EnrichFileFacade enrichFileService) {
        this.enrichFileService = enrichFileService;
    }

    @PostMapping(value = "/api/v1/enrich", consumes = {"multipart/form-data"}, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> handleFileUpload(@RequestPart("file") MultipartFile file) {

        Resource resource = enrichFileService.enrichFile(file);
        return ResponseEntity.ok()
                .body(resource);
    }
}
