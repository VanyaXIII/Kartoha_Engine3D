package shapes;

import com.google.gson.Gson;

import java.io.*;

/**
 * Класс для чтения JSON файлов
 */
public final class JsonReader{

    private final String path;

    /**
     * Конструктор по пути к файлу
     * @param path путь
     */
    public JsonReader(String path){
        this.path = path;
    }

    /**
     * Метод, считаывающий объект из JSON файла
     * @param type тип объекта
     * @return Объект, считанный из файла
     * @throws IOException исключение в случае проблем с открытием файла
     */
    public Object read(Class type) throws IOException {

        File gsonFile = new File(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(gsonFile)));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        String jsonString = sb.toString();
        Gson gson = new Gson();

        return gson.fromJson(jsonString, type);
    }

}
