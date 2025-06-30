package utils;

import java.io.File;
import java.io.IOException;

public class FileManager {
    private static final String PATH_TO_DATA_DIRECTORY = "src/data";

    public static File getFile(String fileName) throws IOException {
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
