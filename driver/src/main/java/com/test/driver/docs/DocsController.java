package com.test.driver.docs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DocsController {

    @Autowired
    private DocsService docsService;

    @GetMapping(value = "/{version}/api-docs", produces = "text/yaml")
    public ResponseEntity getApiDocs(@PathVariable("version") String version) {
        return docsService.getApiDocs(version);
    }
}
