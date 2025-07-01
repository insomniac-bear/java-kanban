package utils;

import java.io.File;
import java.io.IOException;

public final class FileManager {
    private static final String PATH_TO_DATA_DIRECTORY = "src/data";

    public static File getFile(String fileName) throws IOException {
        File dataDir = new File(PATH_TO_DATA_DIRECTORY);
        if (!dataDir.exists() && !dataDir.isDirectory()) {
            throw new IOException("Ошибка создания директории");
        }
        File file = new File(dataDir, fileName);
        if (!file.exists() && !file.isFile()) {
            throw new IOException("Ошибка создания файла");
        }
        return file;
    }
}
