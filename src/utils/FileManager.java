package utils;

import java.io.File;
import java.io.IOException;

public class FileManager {
    static final String PATH_TO_DATA_DIRECTORY = "src/data";
    private final String fileName;

    public FileManager(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() throws IOException {
        File dataDir = new File(PATH_TO_DATA_DIRECTORY);
        if (!dataDir.exists()) {
            boolean isCreated = dataDir.mkdir();
            if (!isCreated) {
                throw new IOException("Ошибка создания директории");
            }
        }
        File file = new File(dataDir, fileName);
        if (!file.exists()) {
            boolean isCreated = file.createNewFile();
            if (!isCreated) {
                throw new IOException("Ошибка создания файла");
            }
        }
        return file;
    }
}
