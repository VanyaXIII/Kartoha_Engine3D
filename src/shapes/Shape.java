package shapes;

import com.google.gson.Gson;
import geometry.Triangle;
import geometry.objects3D.Point3D;

import java.io.*;
import java.util.ArrayList;

public class Shape {

    private ArrayList<Point3D> points;
    private ArrayList<Triangle> triangles;

    {
        points = new ArrayList<>();
        triangles = new ArrayList<>();
    }

    public Shape(ArrayList<Point3D> points, ArrayList<Triangle> triangles) {
        this.points = points;
        this.triangles = triangles;
    }

    public ArrayList<Point3D> getPoints() {
        return points;
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    public void toJson(String path) throws IOException {
        File jsonFile = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(jsonFile);
        OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);

        Gson gson = new Gson();
        writer.append(gson.toJson(this));
        writer.close();
        fileOutputStream.close();
    }
}
