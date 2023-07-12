package com.example.demo.controller;

import com.example.demo.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletRequest;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

@RestController
@RequestMapping("/files")
public class FileStorageController {

    private final FileStorageService fileStorageService;

    public FileStorageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }


    @PostMapping("/**")
    public ResponseEntity<Object> post(HttpServletRequest request) throws Exception {
        return this.fetchInfo(request);
    }

    private ResponseEntity<Object> fetchInfo(HttpServletRequest request) throws Exception {
        String relativeUrl = this.extractRelativeUrl(request);
        Resource resource = this.fileStorageService.loadFileAsResource(relativeUrl);
        HttpHeaders httpHeaders = this.fileStorageService.loadHttpHeaders(resource);
        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }

    private String extractRelativeUrl(HttpServletRequest request){
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE); // files/relativeUrl
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE); // file/**
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);// relativeUrl
    }
}
