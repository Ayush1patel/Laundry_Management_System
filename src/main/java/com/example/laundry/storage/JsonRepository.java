package com.example.laundry.storage;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Thin wrapper over Jackson to read/write JSON lists and objects.
 * Robust to empty files and whitespace-only files.
 */
public class JsonRepository {

    private final ObjectMapper mapper;

    public JsonRepository() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public synchronized <T> void writeList(Path file, List<T> list, Class<T> elementClass) throws IOException {
        byte[] bytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(list);
        AtomicFileWriter.writeAtomically(file, bytes);
    }

    public synchronized <T> List<T> readList(Path file, Class<T> elementClass) throws IOException {
        if (!Files.exists(file)) return Collections.emptyList();
        long size = Files.size(file);
        if (size == 0L) return Collections.emptyList();

        // Read as String and guard against whitespace-only files
        String text = Files.readString(file);
        if (text == null || text.trim().isEmpty()) return Collections.emptyList();

        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, elementClass);
        // use File-based readValue for robustness
        return mapper.readValue(file.toFile(), type);
    }

    public synchronized <T> void writeObject(Path file, T obj) throws IOException {
        byte[] bytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(obj);
        AtomicFileWriter.writeAtomically(file, bytes);
    }

    public synchronized <T> Optional<T> readObject(Path file, Class<T> clazz) throws IOException {
        if (!Files.exists(file)) return Optional.empty();
        long size = Files.size(file);
        if (size == 0L) return Optional.empty();

        String text = Files.readString(file);
        if (text == null || text.trim().isEmpty()) return Optional.empty();

        T obj = mapper.readValue(file.toFile(), clazz);
        return Optional.ofNullable(obj);
    }
}
