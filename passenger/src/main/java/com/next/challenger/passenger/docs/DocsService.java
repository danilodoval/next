package com.next.challenger.passenger.docs;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.HashMap;

@Service
public class DocsService {

    private static HashMap<String, String> apiDocs = new HashMap<>();
    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    public DocsService() {
        this.readAllFilesFromResources();
    }

    public ResponseEntity getApiDocs(String version) {
        if (apiDocs.containsKey(version)) {
            return ResponseEntity.ok(apiDocs.get(version));
        }
        return ResponseEntity.notFound().build();
    }

    public void readAllFilesFromResources() {
        Resource[] resources;
        try {
            resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("classpath:docs/swagger/*/*.yaml");
            for (int i = 0; i < resources.length; i++) {
                InputStream is = null;
                is = resources[i].getInputStream();
                byte[] encoded = IOUtils.toByteArray(is);
                String content = new String(encoded, Charset.forName("UTF-8"));
                apiDocs.put(resources[i].getURL().getPath().split("docs/swagger/")[1].split("/")[0], content);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
