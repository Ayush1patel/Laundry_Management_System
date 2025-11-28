package com.example.laundry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

/**
 * Application-level configuration beans.
 * - dataDir bean is used by JsonFileStorageService via @Value("#{dataDir}")
 *   so you can change the storage directory centrally.
 */
@Configuration
public class AppConfig {

    /**
     * Returns the directory path where JSON data files are stored.
     * Default is "<project-root>/data". Override via environment or modify this bean.
     */
    @Bean(name = "dataDir")
    public String dataDir() {
        String env = System.getProperty("laundry.data.dir");
        if (env != null && !env.isBlank()) return env;
        String env2 = System.getenv("LAUNDRY_DATA_DIR");
        if (env2 != null && !env2.isBlank()) return env2;
        return Paths.get(System.getProperty("user.dir"), "data").toString();
    }
}
