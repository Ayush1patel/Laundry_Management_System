package com.example.laundry.storage;

import java.io.IOException;
import java.nio.file.*;

/**
 * Provides atomic JSON file writes using:
 *   - write to temporary file
 *   - atomic move to final file
 *
 * Ensures JSON files never become corrupted even if JVM crashes mid-write.
 */
public final class AtomicFileWriter {

    private AtomicFileWriter() {}

    public static void writeAtomically(Path path, byte[] bytes) throws IOException {
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }

        Path tmp = Files.createTempFile(parent, path.getFileName().toString(), ".tmp");

        try {
            Files.write(tmp, bytes, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

            try {
                Files.move(tmp, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
            }

        } finally {
            try { Files.deleteIfExists(tmp); } catch (Exception ignore) {}
        }
    }
}
