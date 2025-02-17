package com.ann.test.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface EnrichFileFacade {
    Resource enrichFile(MultipartFile multipartFile);
}
