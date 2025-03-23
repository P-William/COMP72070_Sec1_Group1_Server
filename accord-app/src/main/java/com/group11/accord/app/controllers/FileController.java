package com.group11.accord.app.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/file")
@Tag(name = "File Controller", description = "Handles all operations regarding file retrieval and storage")
@RequiredArgsConstructor
public class FileController {

//    @PostMapping
//    @Operation(summary = "Upload a file")
//    String upload()
}
