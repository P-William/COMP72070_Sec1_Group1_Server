package com.group11.accord.app;

import com.group11.accord.app.services.FileService;
import com.group11.accord.jpa.file.FileJpa;
import com.group11.accord.jpa.file.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileTests {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    @Test
    void getImage_Success() {
        FileJpa fileJpa = mock(FileJpa.class);

        when(fileRepository.findById(any())).thenReturn(Optional.of(fileJpa));

        FileJpa result = fileService.getValidFile(1L);
        assertNotNull(result);
    }
}
