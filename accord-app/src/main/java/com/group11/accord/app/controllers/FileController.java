package com.group11.accord.app.controllers;

import com.group11.accord.app.services.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/file")
@Tag(name = "File Controller", description = "Handles all operations regarding file retrieval and storage")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

//    @PostMapping
//    @Operation(summary = "Upload a file")
//    public String upload(@RequestBody @NotNull @Valid MultipartFile file){
//
//    }

    @GetMapping("/{fileId}")
    @Operation(summary = "Retrieve a stored image")
    public ResponseEntity<byte[]> getFile(
            @PathVariable @NotNull(message = "Id of the file to retrieve is required") Long fileId
    ) {
        log.debug("Received request for file {}", fileId);
        return fileService.getFile(fileId);
    }
}
