package primitives;

import geometry.objects.Shape;
import geometry.objects.Triangle;
import geometry.objects3D.Point3D;
import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Tools;

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
     * @return Объект, считанный из файла
     * @throws IOException исключение в случае проблем с открытием файла
     */
    public Object read() throws IOException {
        InputStream gsonStream = getClass().getClassLoader().getResourceAsStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gsonStream));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        String jsonString = sb.toString();

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray points = jsonObject.getJSONArray("points");
        ArrayList<Point3D> ps = new ArrayList<>();
        for(int i = 0; i < points.length(); i++){
            JSONObject p = points.getJSONObject(i);
            ps.add(new Point3D(p.getDouble("x"), p.getDouble("y"), p.getDouble("z")));
        }

        JSONArray triangles = jsonObject.getJSONArray("triangles");
        ArrayList<Triangle> ts = new ArrayList<>();
        for(int i = 0; i < triangles.length(); i++){
            JSONObject p = triangles.getJSONObject(i);
            JSONObject a = p.getJSONObject("A");
            Point3D A = new Point3D(a.getDouble("x"), a.getDouble("y"), a.getDouble("z"));
            JSONObject b = p.getJSONObject("B");
            Point3D B = new Point3D(b.getDouble("x"), b.getDouble("y"), b.getDouble("z"));
            JSONObject c = p.getJSONObject("C");
            Point3D C = new Point3D(c.getDouble("x"), c.getDouble("y"), c.getDouble("z"));
            ts.add(new Triangle(A, B, C, p.has("color") ? Color.getColor(p.getString("color")) : Tools.getRandomColor()));
        }

        return new Shape(ps, ts);
    }

}
