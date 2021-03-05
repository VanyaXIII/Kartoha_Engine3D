package shapes;

import com.google.gson.Gson;

import java.io.*;

public final class ShapeReader {

    private final String path;

    public ShapeReader(String path){
        this.path = path;
    }

    public Shape read() throws IOException {

        File gsonFile = new File(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(gsonFile)));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        String jsonString = sb.toString();
        Gson gson = new Gson();

        return gson.fromJson(jsonString, Shape.class);
    }

}
