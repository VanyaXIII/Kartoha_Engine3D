package geometry.objects;

import geometry.objects3D.Point3D;
import utils.JsonAble;

import java.util.ArrayList;

/**
 * Трехмерная форма(многогранник)
 */
public class Shape implements JsonAble {

    private final ArrayList<Point3D> points;
    private final ArrayList<Triangle> triangles;


    /** Конструктор формы по вершинам и граням
     * @param points вершины
     * @param triangles треугольники, образующие многогранник
     */
    public Shape(ArrayList<Point3D> points, ArrayList<Triangle> triangles) {
        this.points = points;
        this.triangles = triangles;
    }

    /**
     * @return Вершины
     */
    public ArrayList<Point3D> getPoints() {
        return points;
    }

    /**
     * @return Грани
     */
    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }
}
