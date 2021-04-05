package utils;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Интерфейс, предоставляющий метод для перенесения объекта в JSON
 */
public interface JsonAble {
    /**
     * Метод, записывающий объект в JSON файл
     * @param path путь к JSON файлу, который надо создать
     * @throws IOException исключение в случае проблем с файлом
     */
    default void toJson(String path) throws IOException {
        File jsonFile = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(jsonFile);
        OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);

        Gson gson = new Gson();
        writer.append(gson.toJson(this));
        writer.close();
        fileOutputStream.close();
    }
}
