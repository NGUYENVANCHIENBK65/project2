package com.example.demo.controller;
import java.io.IOException;
import org.springframework.web.bind.annotation.PostMapping;

public class FileStorageController {
    @PostMapping("/**")
    @PreAuthorize("hasAuthority('**/**')")
    public ResponseEnity<Object> post(HttpServletRequest request) throws BusinessException, IOException{
        return this.fetchInfo(request);
    }

    private ResponseEnity<Object> fetchInfo(HttpServletRequest request) throws BusinessException, IOException{
        String relativeUrl = this.extractRelativeUrl(request);
        Resource resource = this.FileStorageService.loadFileAsResource(relativeUrl);
        HttpHeaders httpHeaders = this.FileStorageService.loadHttpHeaders(resource);
        return new ResponseEnity<>(resource, httpHeaders, HttpStatus.OK);
    }

    private String extractRelativeUrl(HttpServletRequest request){
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE); // files/relativeUrl
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE); // file/**
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);// relativeUrl
    }
}
