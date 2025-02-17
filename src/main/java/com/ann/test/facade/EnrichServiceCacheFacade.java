package com.ann.test.facade;

import com.ann.test.exception.exceptions.EmptyFileException;
import com.ann.test.interfaces.EnrichFileFacade;
import com.ann.test.redis.service.FileStorageService;
import com.ann.test.service.EnrichFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * This facade uses for adding caching to redis after we processed a file
 */

@Service
public class EnrichServiceCacheFacade implements EnrichFileFacade {

    private final Logger logger = LoggerFactory.getLogger(EnrichServiceCacheFacade.class);

    private final EnrichFileService enrichFileService;
    private final FileStorageService fileStorageService;

    public EnrichServiceCacheFacade(EnrichFileService enrichFileService, FileStorageService fileStorageService) {
        this.enrichFileService = enrichFileService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Resource enrichFile(MultipartFile multipartFile)  {

        String fileName = multipartFile.getOriginalFilename();

        if (fileName == null || fileName.isEmpty()) {
            logger.warn("File has no name. Cannot process file.");
            throw new EmptyFileException("File name is missing.");
        }

        if (isFileInCache(multipartFile)){
            logger.info("GETTING FILE FROM CACHE at: {}", LocalDateTime.now());
            return  new ByteArrayResource(fileStorageService.getFile(fileName).get());
        }

        Resource resource = enrichFileService.enrichFile(multipartFile);
        trySaveFileToCache(fileName, resource);

        return resource;
    }

    private boolean isFileInCache(MultipartFile multipartFile) {
        return fileStorageService.getFile(multipartFile.getOriginalFilename()).isPresent();
    }

    private void trySaveFileToCache(String key, Resource resource) {
        try {
            fileStorageService.saveFile(key, resource.getContentAsByteArray(), 600);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    private void saveFileToCache(String key, Resource resource) throws IOException {
        fileStorageService.saveFile(key, resource.getContentAsByteArray(), 600);
    }
}
