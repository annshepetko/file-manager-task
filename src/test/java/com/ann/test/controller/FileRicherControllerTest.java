package com.ann.test.controller;
import com.ann.test.service.EnrichFileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileRicherController.class)
class FileRicherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrichFileService enrichFileService;

    @Test
    void handleFileUpload_ValidFile_ReturnsEnrichedFile() throws Exception {
        // Mock input file
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "productId,date\n123,2023-10-01".getBytes()
        );

        // Mock enriched file content
        byte[] enrichedContent = "productName,date\nTest Product,2023-10-01".getBytes();
        Resource mockResource = new ByteArrayResource(enrichedContent);

        // Mock service response
        when(enrichFileService.enrichFile(any())).thenReturn(mockResource);

        // Perform request and verify response
        mockMvc.perform(multipart("/api/v1/enrich")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(enrichedContent));

        // Verify service interaction
        verify(enrichFileService).enrichFile(any());
    }
}