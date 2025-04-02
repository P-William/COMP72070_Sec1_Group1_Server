package com.group11.accord.app.services;

import com.group11.accord.app.exceptions.ErrorMessages;
import com.group11.accord.app.exceptions.FileProcessingException;
import com.group11.accord.jpa.file.FileJpa;
import com.group11.accord.jpa.file.FileRepository;
import com.group11.accord.jpa.file.FileType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    //String baseUrl = "https://localhost:6204/file/";

    private static final Map<FileType, MediaType> contentOut = Map.of(
            FileType.JPEG, MediaType.IMAGE_JPEG,
            FileType.PNG, MediaType.IMAGE_PNG
    );

    private static final Map<String, FileType> contentIn = Map.of(
            "image/jpeg", FileType.JPEG,
            "image/png", FileType.PNG
    );
    
    public ResponseEntity<byte[]> getFile(Long fileId) {
        FileJpa fileJpa = getValidFile(fileId);


        return ResponseEntity.ok()
                .contentType(contentOut.get(fileJpa.getType()))
                .body(fileJpa.getData());
    }

    public FileJpa getValidFile(Long fileId) {
        Optional<FileJpa> fileJpa = fileRepository.findById(fileId);

        if (fileJpa.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.FILE_NOT_FOUND.formatted(fileId));
        }
        else {
            return fileJpa.get();
        }
    }

    public String saveImage(MultipartFile image) {

        FileJpa fileJpa;
        try {
            fileJpa = FileJpa.create(contentIn.get(image.getContentType()), image.getBytes());
        } catch (IOException e) {
            throw new FileProcessingException(ErrorMessages.FILE_PROCESSING_ERROR, e);
        }

        fileRepository.save(fileJpa);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();

        return baseUrl + "/file/" + fileJpa.getId();
    }
}
