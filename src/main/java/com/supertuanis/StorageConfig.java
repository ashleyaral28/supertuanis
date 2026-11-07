package com.supertuanis;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class StorageConfig {

    @Value("${firebase.json.path}")
    private String jsonPath;

    @Value("${firebase.json.file}")
    private String jsonFile;

    // Ruta absoluta del JSON cuando no va dentro del proyecto (por ejemplo en Render, como Secret File).
    // Si está vacía, se lee de src/main/resources/firebase como en tienda.
    @Value("${firebase.json.external:}")
    private String externalJson;

    @Bean
    public Storage storage() throws IOException {
        try (InputStream inputStream = abrirJson()) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
            return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }
    }

    private InputStream abrirJson() throws IOException {
        if (externalJson != null && !externalJson.isBlank()) {
            return new FileInputStream(externalJson);
        }
        return new ClassPathResource(jsonPath + File.separator + jsonFile).getInputStream();
    }

}
